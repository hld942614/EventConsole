package com.project.uhdbackend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.uhdbackend.dto.CmdbAssetExcelRowDTO;
import com.project.uhdbackend.dto.CmdbVmExcelRowDTO;
import com.project.uhdbackend.dto.ImportResultDTO;
import com.project.uhdbackend.entity.CmdbAsset;
import com.project.uhdbackend.entity.CmdbAssetHardware;
import com.project.uhdbackend.entity.CmdbAssetNetwork;
import com.project.uhdbackend.entity.CmdbAssetOs;
import com.project.uhdbackend.enums.AssetType;
import com.project.uhdbackend.exception.RowValidationException;
import com.project.uhdbackend.repository.CmdbAssetHardwareRepository;
import com.project.uhdbackend.repository.CmdbAssetNetworkRepository;
import com.project.uhdbackend.repository.CmdbAssetOsRepository;
import com.project.uhdbackend.repository.CmdbAssetRepository;
import com.project.uhdbackend.utils.CmdbAssetExcelConverter;
import com.project.uhdbackend.utils.CmdbVmExcelConverter;
import com.project.uhdbackend.utils.ConvertedAssetBundle;

import cn.idev.excel.FastExcel;
import cn.idev.excel.read.listener.PageReadListener;

/**
 * Excel 匯入服務：MultipartFile -> FastExcel -> CmdbAssetExcelRowDTO ->
 * CmdbAssetExcelConverter -> CMDB_ASSET / CMDB_ASSET_HARDWARE / CMDB_ASSET_OS /
 * CMDB_ASSET_NETWORK / IPAM_IP_ADDRESS。
 *
 * Upsert 規則（保持簡單）：用 (SERVER_NAME, ASSET_TYPE) 判斷資產是否已存在。 - 已存在：沿用原本的
 * ASSET_ID，更新其餘欄位（不重新跑序號產生器）。 - 不存在：呼叫 AssetIdGeneratorService 產生新的 ASSET_ID
 * 再新增。 如果之後想改成用財產編號或 ServiceTag 當唯一鍵，只要改 findExisting() 即可。
 *
 * 單列轉換失敗（RowValidationException）不會讓整批匯入中斷， 該列會被記錄進
 * ImportResultDTO.errors，其餘列繼續處理。
 */
@Service
public class CmdbImportService {

	private final CmdbAssetExcelConverter converter;
	private final AssetIdGeneratorService assetIdGeneratorService;
	private final CmdbAssetRepository assetRepository;
	private final CmdbAssetHardwareRepository hardwareRepository;
	private final CmdbAssetOsRepository osRepository;
	private final CmdbAssetNetworkRepository networkRepository;
	private final CmdbVmExcelConverter vmConverter;
	private final CmdbAssetRepository cmdbAssetRepository;
//	private final IpamIpAddressRepository ipAddressRepository;

	public CmdbImportService(CmdbAssetExcelConverter converter, AssetIdGeneratorService assetIdGeneratorService,
			CmdbAssetRepository assetRepository, CmdbAssetHardwareRepository hardwareRepository,
			CmdbAssetOsRepository osRepository, CmdbAssetNetworkRepository networkRepository,
			CmdbVmExcelConverter vmConverter, CmdbAssetRepository cmdbAssetRepository) {
		this.converter = converter;
		this.assetIdGeneratorService = assetIdGeneratorService;
		this.assetRepository = assetRepository;
		this.hardwareRepository = hardwareRepository;
		this.osRepository = osRepository;
		this.networkRepository = networkRepository;
		this.vmConverter = vmConverter;
		this.cmdbAssetRepository = cmdbAssetRepository;
//		this.ipAddressRepository = ipAddressRepository;
	}

	public ImportResultDTO importFromExcel(MultipartFile file) {
		List<CmdbAssetExcelRowDTO> rows = new ArrayList<>();
		try {
			FastExcel.read(file.getInputStream(), CmdbAssetExcelRowDTO.class,
					new PageReadListener<CmdbAssetExcelRowDTO>(rows::addAll)).sheet("大機房").doRead();
		} catch (IOException e) {
			throw new RuntimeException("Excel 讀取失敗：" + e.getMessage(), e);
		}
		ImportResultDTO result = new ImportResultDTO();
		int rowNo = 1; // 對應 Excel 資料列（不含表頭）
		for (CmdbAssetExcelRowDTO row : rows) {
//			if (count >= 10) {
//				break;
//			}
			row.setSourceRowNo(rowNo);
			try {
				importOneRow(row);
				result.addSuccess();
			} catch (RowValidationException e) {
				e.printStackTrace();
				result.addFailure(rowNo, row.getServerName(), e.getMessage());
			} catch (Exception e) {
				// 保留原始例外堆疊，避免吞掉真正的錯誤原因
				result.addFailure(rowNo, row.getServerName(),
						"匯入失敗: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			}
			rowNo++;
		}
		return result;
	}

	/** 單一列的匯入，獨立成一個交易：一列失敗 rollback，不影響其他列已經 commit 的結果 */
	@Transactional
	public void importOneRow(CmdbAssetExcelRowDTO row) {
		AssetType assetType = AssetType.fromRawText(row.getAssetTypeRaw());
		String assetId = resolveAssetId(assetType);

		ConvertedAssetBundle bundle = converter.convert(row, assetId);

		assetRepository.save(bundle.getAsset());

		if (bundle.getHardware() != null) {
			saveHardware(assetId, bundle.getHardware());
		}
		if (bundle.getOs() != null) {
			saveOs(assetId, bundle.getOs());
		}
		for (CmdbAssetNetwork network : bundle.getNetworks()) {
			saveNetwork(assetId, network);
		}
	}

	public ImportResultDTO importVmFromExcel(MultipartFile file) {
		List<CmdbVmExcelRowDTO> rows = new ArrayList<>();
		try {
			FastExcel.read(file.getInputStream(), CmdbVmExcelRowDTO.class,
					new PageReadListener<CmdbVmExcelRowDTO>(rows::addAll)).sheet("虛擬化系統").doRead();
		} catch (IOException e) {
			throw new RuntimeException("Excel 讀取失敗：" + e.getMessage(), e);
		}

		ImportResultDTO result = new ImportResultDTO();
		int rowNo = 1;
		for (CmdbVmExcelRowDTO row : rows) {
			row.setSourceRowNo(rowNo);
			try {
				importOneVmRow(row);
				result.addSuccess();
			} catch (RowValidationException e) {
				result.addFailure(rowNo, row.getVmName(), e.getMessage());
			} catch (Exception e) {
				result.addFailure(rowNo, row.getVmName(),
						"匯入失敗: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			}
			rowNo++;
		}
		return result;
	}

	@Transactional
	public void importOneVmRow(CmdbVmExcelRowDTO row) {
		String host = row.getHost();
		if (host.equals("待上線") || host.equals("已下架")) {
			return;
		}
		String assetId = assetIdGeneratorService.generateAssetId(AssetType.VIRTUAL_MACHINE);

		String parentAssetId = cmdbAssetRepository.findFirstByServerNameOrderByIdDesc(row.getHost())
				.map(CmdbAsset::getAssetId).orElse(null); // 找不到實體機也照樣匯入，只是 PARENT_ASSET_ID 先空著

		ConvertedAssetBundle bundle = vmConverter.convert(row, assetId, parentAssetId);

		assetRepository.save(bundle.getAsset());
		if (bundle.getOs() != null) {
			osRepository.save(bundle.getOs());
		}
		for (CmdbAssetNetwork network : bundle.getNetworks()) {
			networkRepository.save(network);
		}
	}

	private String resolveAssetId(AssetType assetType) {
		// 匯入一律新增：Excel 原始資料沒有唯一鍵可以拿來判斷「這筆是不是已經匯入過」，
		// 所以每次匯入都會產生新的 ASSET_ID。後續要維護（更新/修改）同一筆資產，
		// 走的是另一支用 ASSET_ID 當鍵值的 update API，不會透過這支 Excel 匯入 API 做 upsert。
		return assetIdGeneratorService.generateAssetId(assetType);
	}

	private void saveHardware(String assetId, CmdbAssetHardware incoming) {
		hardwareRepository.save(incoming);
	}

	private void saveOs(String assetId, CmdbAssetOs incoming) {
		osRepository.save(incoming);
	}

	private void saveNetwork(String assetId, CmdbAssetNetwork incoming) {
//		CmdbAssetNetwork saved = networkRepository.save(incoming);
//		String ipId = upsertIpAddress(assetId, saved);
//		saved.setIpId(ipId);
//		networkRepository.save(saved);
		networkRepository.save(incoming);
	}

	/** 建立/更新對應的 IPAM_IP_ADDRESS 紀錄，回傳它的 IP_ID */
//	private String upsertIpAddress(String assetId, CmdbAssetNetwork network) {
//		IpamIpAddress ip = ipAddressRepository.findByIpAddress(network.getIpAddress()).orElseGet(() -> {
//			IpamIpAddress newIp = new IpamIpAddress();
//			// IP_ID 是 NOT NULL，但要等 insert 完成拿到自動產生的 ID 才能組出
//			// 正式格式（IP-00000001），所以先給一個保證唯一的暫時值卡住 NOT NULL/UNIQUE 檢查，
//			// insert 完成後馬上回填正式值。用 IP_ADDRESS 當暫時值是因為它本身也是唯一的，
//			// 不會像固定字串一樣在同一批匯入裡互相撞到。
//			newIp.setIpId("TMP-" + network.getIpAddress());
//			return newIp;
//		});
//		ip.setIpAddress(network.getIpAddress());
//		ip.setIpStatus(IpStatus.USED);
//		ip.setAssetId(assetId);
//		ip.setSourceSystem("EXCEL");
//
//		boolean isNew = ip.getId() == null;
//		IpamIpAddress saved = ipAddressRepository.save(ip);
//		if (isNew) {
//			// IP_ID 沿用資料庫自動產生的流水號組字串，簡單且不需要額外的序號表
//			saved.setIpId(String.format("IP-%08d", saved.getId()));
//			saved = ipAddressRepository.save(saved);
//		}
//		return saved.getIpId();
//	}
}
