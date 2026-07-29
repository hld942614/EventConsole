# UHD Console Backend

UHD Console 是一套告警管理與 IT 資產追蹤平台，後端負責即時告警（Event）的接收、生命週期管理，以及 IT 資產／CMDB 資料的匯入與查詢。

---

## 技術棧

| 項目 | 說明 |
|---|---|
| 語言 / 框架 | Java 17、Spring Boot 2.7 |
| 資料庫 | Oracle DB（schema owner: `MUHD`） |
| 持久層 | JPA / Hibernate、`NamedParameterJdbcTemplate`（複雜查詢與 List API） |
| 訊息佇列 | Kafka（告警事件接收） |
| 即時通知 | WebSocket（STOMP，`/ws` 端點，broker prefix `/websocket/v1`） |
| 認證 | LDAP / Active Directory |
| Excel 解析 | FastExcel (`cn.idev.excel`) |
| 套件根目錄 | `com.project.uhdbackend` |

---

## 系統架構

```
Kafka (UHDEvent topic)
    │
    ▼
KafkaConsumerService ─▶ EventService.processNewEvent()
    │                        │
    │                        ├─▶ 解析 .eml / JSON payload
    │                        ├─▶ EventIdGeneratorService 產生 MEVT-xxx 編號
    │                        ├─▶ 附件落地 (AttachmentService / StorageService)
    │                        └─▶ CaseClassifierService 自動分類進 Case
    │
    ▼
MUHD_EVENT ──(MUHD_EVENT_COMMENT)── Comment
    │
    └──(MUHD_CASE_EVENT)── Case ── 狀態機 (OPEN → PROCESSING → RESOLVED → CLOSED)
                                        │
                                        └─▶ WebSocket 即時推播 (RealtimeEventService → RealtimeWsFanout)
```

CMDB 匯入流程（獨立於 Event 流程）：

```
Excel 上傳 (實體資產清單 / VM 清單)
    │
    ▼
CmdbImportService ──▶ CmdbAssetExcelConverter / CmdbVmExcelConverter
    │
    ▼
CMDB_ASSET / CMDB_ASSET_HARDWARE / CMDB_ASSET_OS / CMDB_ASSET_NETWORK
    │
    ▼
CmdbAssetQueryService.getAssetDetail() ──▶ 聚合各表 + VM 子清單，回傳給前端
```

---

## 主要模組

### 1. Event / Alert 系統

- **來源**：Kafka topic `UHDEvent`，內容為 `.eml` 格式，本文為 JSON（`alertCode` / `source.environment` / `occurredAt` / `severity` / `details` 等欄位）。
- **核心表**：`MUHD_EVENT`。
- **eventId 格式**：`MEVT-{moduleCode}-{environment}-{yyyyMMdd}-{6碼流水號}`，同一天 + 同模組 + 同環境重新編號，透過 `MUHD_EVENT_SEQUENCE` 計數器表 + `SELECT ... FOR UPDATE` 產生，避免使用單一 Oracle SEQUENCE 無法動態組合的問題。
- **狀態機**（`EventStatus`）：`UNREAD → ACKNOWLEDGED/CLASSIFIED → PROCESSING → RESOLVED → CLOSED`，狀態轉換皆有明確的 guard，只允許往前推進（見 `EventStatusService`）。`INVALID` 保留於 enum 但目前未被任何流程使用。
- **Case 分類**：新事件進來時由 `CaseClassifierService` 對所有 `ruleEnabled=true` 且狀態為 active 的 Case 跑一次規則比對（`EventPredicateBuilder`），符合者寫入 `MUHD_CASE_EVENT`。**不會**對既有事件做回溯分類，規則異動只影響之後新進的事件。
- **留言 / 附件**：`Comment` 透過 `MUHD_EVENT_COMMENT` 多對多關聯 Event；附件透過 `MUHD_EVENT_ATTACHMENT`，正在擴充為支援富文字內容（`CLOB` + `@Lob`）與檔案上傳（`multipart/form-data`），落地前需經 HTML 消毒（建議 OWASP Java HTML Sanitizer 或 jsoup，尚未實作）。
- **即時通知**：`RealtimeEventService.publish()` 透過 Spring `ApplicationEventPublisher` 發布，`RealtimeWsFanout` 在交易 **commit 後**（`AFTER_COMMIT`）才真正推送到 WebSocket，避免推播了尚未落地的資料。

### 2. CMDB 資產管理系統

- **資料表**（詳見 [`CMDB_Table_schema.md`](./CMDB_Table_schema.md)）：
  - `CMDB_ASSET`：資產主表（實體主機 / VM / 網路設備等）
  - `CMDB_ASSET_HARDWARE`：硬體規格
  - `CMDB_ASSET_OS`：作業系統（可存多筆保留升級歷史）
  - `CMDB_ASSET_NETWORK`：資產與 IP 的多對多關聯
  - `IPAM_SUBNET` / `IPAM_IP_ADDRESS`：IP 位址管理（IPAM，寫入功能目前延後，待與機房確認 IP 格式標準化）
  - `CMDB_APPLICATION` / `CMDB_APPLICATION_ASSET`：應用系統與資產的關聯
  - `CMDB_ASSET_SEQ_NO`：ASSET_ID 流水號輔助表
- **匯入**：`CmdbImportService` 透過 FastExcel 讀取「實體資產清單」與「VM 清單」（欄位格式不同，各自獨立 DTO / Converter），採**名稱對應**（非欄位順序），並保留原始列號與整列 JSON（`RAW_DATA`）供追查。單列失敗不影響整批，錯誤彙整進 `ImportResultDTO.errors`。
- **ASSET_ID 格式**：`CI-{類型短碼}-{8碼流水號}`（如 `CI-SRV-00000001`），由 `AssetIdGeneratorService` + `CmdbAssetSeqNoTxOps`（獨立 bean，確保 `REQUIRES_NEW` 透過 AOP 生效）產生。
- **查詢**：`CmdbAssetQueryService.getAssetDetail()` 聚合 Asset + Hardware + OS + Network + 掛載的 Application + 子 VM 清單，單一 API 回傳完整資產卡片。

---

## API 端點總覽（節錄）

| 分類 | Controller | 說明 |
|---|---|---|
| Event | `EventController` | 查詢 / 搜尋 / 已讀 / 解決 / 結案 |
| Case | `CaseController` | Case CRUD、解決 / 結案（cascade 更新底下 Event 狀態） |
| Case-Event | `CaseEventController` | 手動將 Event 加入 / 移出 Case |
| Comment | `CommentController` | 對 Case / Event 留言 |
| Attachment | `AttachmentController` | 事件附件上傳 / 下載 / 刪除 |
| CMDB Import | `CmdbImportController` | Excel 匯入（實體資產 / VM） |
| CMDB Query | `CmdbAssetQueryController` | 依 ASSET_ID 查詢完整資產詳情 / 全部資產摘要 |
| Category | `CategoryController` | 告警分類（主類別 / 子類別）與統計 |
| Auth | `AdController` | LDAP/AD 登入 |
| File | `FileController` | SOP 檔案（PDF）上傳／下載 |

> 所有回應統一包裝在 `ApiResponse<T>` 中；`NoSuchElementException` 由 `GlobalExceptionHandler` 統一轉為 404。

---

## 開發慣例與重要原則

- **分層架構**：Controller（薄）→ Service（查詢聚合 / 商業邏輯）→ Repository（JPA + `NamedParameterJdbcTemplate`）。List API 一律避免 JPA lazy-load，改用批次查詢（如 `findByAssetIdIn`）防止 N+1。
- **DTO 對應**：手動 field-by-field mapping（未使用 MapStruct），複合回應採用巢狀 static inner class（參考 `AssetDetailDTO`）。
- **業務鍵 vs 內部 PK**：對外 API 一律用字串業務鍵（如 `eventId`、`assetId`）；內部 join table / JPA 關聯用 Long 代理鍵，轉換只在 API 邊界發生。
- **時間欄位**：`TIMESTAMP WITH TIME ZONE` 一律用 `OffsetDateTime` 綁定，不要用格式化字串（避免 Oracle JDBC 的 `ORA-01843`）。
- **FK 必須建索引**：子表的外鍵欄位（如 `CMDB_ASSET_NETWORK.IP_ID`）務必加索引，否則刪除父列時會在 Oracle 產生 table-level share lock，阻塞其他寫入。
- **序號產生**：同類型的流水號產生（Event / CMDB Asset）都採用「計數器表 + `SELECT ... FOR UPDATE` + 獨立 `REQUIRES_NEW` 交易 + 失敗重試」的統一模式，且刻意拆成獨立 bean 避免 Spring AOP self-invocation 問題。
- **Kafka 容錯**：無法解析的 payload 只記錄 log（`log.error`），不落地為 `INVALID` 記錄。

---
