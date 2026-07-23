package com.project.uhdbackend.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.project.uhdbackend.dto.CmdbAssetExcelRowDTO;
import com.project.uhdbackend.entity.CmdbAsset;
import com.project.uhdbackend.entity.CmdbAssetHardware;
import com.project.uhdbackend.entity.CmdbAssetNetwork;
import com.project.uhdbackend.entity.CmdbAssetOs;
import com.project.uhdbackend.enums.AssetStatus;
import com.project.uhdbackend.enums.AssetType;
import com.project.uhdbackend.exception.RowValidationException;

/**
 * 手動轉換攤平的 CmdbAssetExcelRowDTO -> CmdbAsset / CmdbAssetHardware / CmdbAssetOs /
 * CmdbAssetNetwork(多筆)。
 *
 * 沿用既有的作法（Server List API 的 ServerExcelConverter）：既有 DTO 都有 setter，手動 converter
 * 比引入 MapStruct 簡單直接，不需要額外的框架依賴。
 *
 * 這個 converter 不碰 DB，ASSET_ID 由外部（CmdbImportService + AssetIdGeneratorService）
 * 產生後傳進來，保持職責單純：converter 只做「資料形狀轉換」。
 */
@Component
public class CmdbAssetExcelConverter {

//	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	public ConvertedAssetBundle convert(CmdbAssetExcelRowDTO row, String assetId) {
		validate(row);

		CmdbAsset asset = buildAsset(row, assetId);
		CmdbAssetHardware hardware = buildHardware(row, assetId);
		CmdbAssetOs os = buildOs(row, assetId);
		List<CmdbAssetNetwork> networks = buildNetworks(row, assetId);

		return new ConvertedAssetBundle(asset, hardware, os, networks);
	}

	private void validate(CmdbAssetExcelRowDTO row) {
		if (row.getServerName() == null || row.getServerName().isBlank()) {
			throw new RowValidationException("ServerName 為必填，此列無法識別是哪一台設備");
		}
	}

	private CmdbAsset buildAsset(CmdbAssetExcelRowDTO row, String assetId) {
		CmdbAsset asset = new CmdbAsset();
		asset.setAssetId(assetId);
		asset.setServerName(row.getServerName());
		asset.setAssetName(row.getServerName());
		asset.setAssetNo(row.getAssetNo());
		asset.setServiceTag(row.getServiceTag());

		AssetType assetType = AssetType.fromRawText(row.getAssetTypeRaw());
		asset.setAssetType(assetType);
		asset.setAssetTypeRaw(row.getAssetTypeRaw());

		asset.setModel(row.getModel());
		asset.setIsVirtual(assetType == AssetType.VIRTUAL_MACHINE ? "Y" : "N");

		asset.setEnvironment(EnvironmentMapper.toStandard(row.getEnvironmentRaw()));
		asset.setEnvironmentRaw(row.getEnvironmentRaw());

		asset.setSystemCode(row.getSystemCode());
		asset.setSystemName(row.getSystemCode());
		asset.setFunctionDesc(row.getFunctionDesc());

		asset.setSecCode(row.getSecCode());
		asset.setRackNo(row.getRackNo());
		asset.setUPosition(row.getUPosition());
		asset.setSizeU(row.getSizeU());

		asset.setCustodyDept(row.getCustodyDept());
		asset.setMtaGroupCode(row.getMtaGroupCode());
		asset.setMtaGroupName(row.getMtaGroupName());
		asset.setMtaUser(row.getMtaUser());

		asset.setAcquireDate(DateParseUtil.parse(row.getAcquireDate(), "取得日期"));
		asset.setStatus(AssetStatus.ACTIVE);
		asset.setRemark(row.getRemark());
		asset.setMigrationPlan(row.getMigrationPlan());

		asset.setSourceSystem("EXCEL");
		if (row.getSourceRowNo() != null) {
			asset.setSourceRowNo(row.getSourceRowNo().longValue());
		}
		asset.setRawData(toRawDataJson(row));

		return asset;
	}

	private CmdbAssetHardware buildHardware(CmdbAssetExcelRowDTO row, String assetId) {
		boolean hasHardwareData = row.getCpuModel() != null || row.getCoreDesc() != null || row.getDiskSize() != null
				|| row.getDiskCount() != null || row.getMemorySize() != null || row.getPowerSupply() != null
				|| row.getVoltage() != null || row.getPowerConsumption() != null || row.getVoltageRange() != null
				|| row.getHbaCard() != null || row.getNetworkCard() != null || row.getMaintainVendor() != null
				|| row.getMaintainStartDate() != null || row.getMaintainEndDate() != null
				|| row.getMaintainType() != null;
		if (!hasHardwareData) {
			return null;
		}

		CmdbAssetHardware hw = new CmdbAssetHardware();
		hw.setAssetId(assetId);
		hw.setCpuModel(row.getCpuModel());
		hw.setCoreDesc(row.getCoreDesc());
		hw.setDiskSize(row.getDiskSize());
		hw.setDiskCount(row.getDiskCount());
		hw.setMemorySize(row.getMemorySize());
		hw.setHbaCard(row.getHbaCard());
		hw.setNetworkCard(row.getNetworkCard());
		hw.setPowerSupply(row.getPowerSupply());
		hw.setVoltage(row.getVoltage());
		hw.setPowerConsumption(row.getPowerConsumption());
		hw.setVoltageRange(row.getVoltageRange());
		hw.setMaintainVendor(row.getMaintainVendor());
		hw.setMaintainStartDate(DateParseUtil.parse(row.getMaintainStartDate(), "維護起始日"));
		hw.setMaintainEndDate(DateParseUtil.parse(row.getMaintainEndDate(), "維護到期日"));
		hw.setMaintainType(row.getMaintainType());
		return hw;
	}

	private CmdbAssetOs buildOs(CmdbAssetExcelRowDTO row, String assetId) {
		if (row.getOsName() == null || row.getOsName().isBlank()) {
			return null;
		}
		CmdbAssetOs os = new CmdbAssetOs();
		os.setAssetId(assetId);
		os.setOsName(row.getOsName());
		os.setIsCurrent("Y");
		return os;
	}

	private List<CmdbAssetNetwork> buildNetworks(CmdbAssetExcelRowDTO row, String assetId) {
		List<CmdbAssetNetwork> networks = new ArrayList<>();
		if (row.getUserLanIp() != null) {
			List<String> userLanIpList = Arrays.stream(row.getUserLanIp().split(",")).map(String::trim).toList();
			for (String userLanIp : userLanIpList) {
				addNetworkIfPresent(networks, assetId, userLanIp, true);
			}
		}
		if (row.getBackupIp() != null) {
			List<String> backupIpList = Arrays.stream(row.getBackupIp().split(",")).map(String::trim).toList();
			for (String backupIp : backupIpList) {
				addNetworkIfPresent(networks, assetId, backupIp, false);
			}
		}
		if (row.getIdracIp() != null) {
			List<String> idracIpList = Arrays.stream(row.getIdracIp().split(",")).map(String::trim).toList();
			for (String idracIp : idracIpList) {
				addNetworkIfPresent(networks, assetId, idracIp, false);
			}
		}
		return networks;

//		List<CmdbAssetNetwork> networks = new ArrayList<>();
//        addNetworkIfPresent(networks, assetId, IpType.USER_LAN, row.getUserLanIp(), true);
//        addNetworkIfPresent(networks, assetId, IpType.BACKUP, row.getBackupIp(), false);
//        addNetworkIfPresent(networks, assetId, IpType.IDRAC, row.getIdracIp(), false);
//        return networks;
	}

	private void addNetworkIfPresent(List<CmdbAssetNetwork> networks, String assetId, String ip, boolean primary) {
		if (ip == null || ip.isBlank()) {
			return;
		}
		CmdbAssetNetwork network = new CmdbAssetNetwork();
		network.setAssetId(assetId);
		network.setIpAddress(ip.trim());
		network.setIsPrimary(primary ? "Y" : "N");
		networks.add(network);
	}

	/** 把整列原始資料存成 JSON，符合 CMDB_ASSET.RAW_DATA 的 CHECK (RAW_DATA IS JSON) 限制 */
	private String toRawDataJson(CmdbAssetExcelRowDTO row) {
		JSONObject json = new JSONObject();
		json.put("serverName", row.getServerName());
		json.put("assetNo", row.getAssetNo());
		json.put("serviceTag", row.getServiceTag());
		json.put("assetTypeRaw", row.getAssetTypeRaw());
		json.put("model", row.getModel());
		json.put("environmentRaw", row.getEnvironmentRaw());
		json.put("systemCode", row.getSystemCode());
		json.put("cpuModel", row.getCpuModel());
		json.put("memorySize", row.getMemorySize());
		json.put("osName", row.getOsName());
		json.put("userLanIp", row.getUserLanIp());
		json.put("backupIp", row.getBackupIp());
		json.put("idracIp", row.getIdracIp());
		return json.toString();
	}
}
