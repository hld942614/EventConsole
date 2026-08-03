package com.project.uhd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.project.uhd.dto.CmdbVmExcelRowDTO;
import com.project.uhd.entity.CmdbAsset;
import com.project.uhd.entity.CmdbAssetNetwork;
import com.project.uhd.entity.CmdbAssetOs;
import com.project.uhd.enums.AssetStatus;
import com.project.uhd.enums.AssetType;
import com.project.uhd.exception.RowValidationException;

/**
 * VM 清單（Host/Sec/Env/Sys/Name/IP/Notes/Guest OS）轉換， 跟
 * CmdbAssetExcelConverter（實體資產清單）分開維護，因為 Excel 欄位格式完全不同。
 *
 * parentAssetId 由外部（CmdbImportService）查好 CmdbAssetRepository 後傳入， converter
 * 本身不碰 DB，保持職責單純。找不到對應實體機時傳 null 即可， 不視為匯入失敗（HOST_NAME 文字仍會保留，方便日後人工比對補上）。
 */
@Component
public class CmdbVmExcelConverter {

	public ConvertedAssetBundle convert(CmdbVmExcelRowDTO row, String assetId, String parentAssetId) {
		validate(row);

		CmdbAsset asset = buildAsset(row, assetId, parentAssetId);
		CmdbAssetOs os = buildOs(row, assetId);
		List<CmdbAssetNetwork> networks = buildNetworks(row, assetId);

		return new ConvertedAssetBundle(asset, null, os, networks);
	}

	private void validate(CmdbVmExcelRowDTO row) {
		if (row.getVmName() == null || row.getVmName().isBlank()) {
			throw new RowValidationException("Name 為必填，此列無法識別是哪一台 VM");
		}
		if (row.getHost() == null || row.getHost().isBlank()) {
			throw new RowValidationException("Host 為必填，無法判斷此 VM 所屬實體機");
		}
	}

	private CmdbAsset buildAsset(CmdbVmExcelRowDTO row, String assetId, String parentAssetId) {
		CmdbAsset asset = new CmdbAsset();
		asset.setAssetId(assetId);
		asset.setServerName(row.getVmName());
		asset.setAssetName(row.getVmName());

		asset.setAssetType(AssetType.VIRTUAL_MACHINE);
		asset.setAssetTypeRaw("VM");
		asset.setIsVirtual("Y");

		asset.setHostName(row.getHost());
		asset.setParentAssetId(parentAssetId); // 找不到就是 null，之後可人工補

		asset.setEnvironment(EnvironmentMapper.toStandard(row.getEnvironmentRaw()));
		asset.setEnvironmentRaw(row.getEnvironmentRaw());

		asset.setSystemCode(row.getSystemCode());
		asset.setSystemName(row.getSystemCode());
		asset.setFunctionDesc(row.getNotes());

		asset.setSecCode(row.getSecCode());
		asset.setStatus(AssetStatus.ACTIVE);
		asset.setSourceSystem("EXCEL");

		if (row.getSourceRowNo() != null) {
			asset.setSourceRowNo(row.getSourceRowNo().longValue());
		}
		asset.setRawData(toRawDataJson(row));

		return asset;
	}

	private CmdbAssetOs buildOs(CmdbVmExcelRowDTO row, String assetId) {
		if (row.getGuestOs() == null || row.getGuestOs().isBlank()) {
			return null;
		}
		CmdbAssetOs os = new CmdbAssetOs();
		os.setAssetId(assetId);
		os.setOsName(row.getGuestOs());
		os.setIsCurrent("Y");
		return os;
	}

	private List<CmdbAssetNetwork> buildNetworks(CmdbVmExcelRowDTO row, String assetId) {
		List<CmdbAssetNetwork> networks = new ArrayList<>();
		if (row.getIpAddress() == null || row.getIpAddress().isBlank()) {
			return networks;
		}

		List<String> ipList = Arrays.stream(row.getIpAddress().split(",")).map(String::trim).filter(ip -> !ip.isBlank())
				.toList();

		for (String ip : ipList) {
			CmdbAssetNetwork network = new CmdbAssetNetwork();
			network.setAssetId(assetId);
			network.setIpAddress(ip);
			networks.add(network);
		}

		return networks;
	}

	private String toRawDataJson(CmdbVmExcelRowDTO row) {
		JSONObject json = new JSONObject();
		json.put("host", row.getHost());
		json.put("secCode", row.getSecCode());
		json.put("environmentRaw", row.getEnvironmentRaw());
		json.put("systemCode", row.getSystemCode());
		json.put("vmName", row.getVmName());
		json.put("ipAddress", row.getIpAddress());
		json.put("notes", row.getNotes());
		json.put("guestOs", row.getGuestOs());
		return json.toString();
	}
}