package com.project.uhdbackend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.dto.AssetDetailDTO;
import com.project.uhdbackend.dto.AssetDetailDTO.ApplicationInfo;
import com.project.uhdbackend.dto.AssetDetailDTO.AssetInfo;
import com.project.uhdbackend.dto.AssetDetailDTO.HardwareInfo;
import com.project.uhdbackend.dto.AssetDetailDTO.NetworkInfo;
import com.project.uhdbackend.dto.AssetDetailDTO.OsInfo;
import com.project.uhdbackend.dto.AssetSummaryDTO;
import com.project.uhdbackend.entity.CmdbApplicationAsset;
import com.project.uhdbackend.entity.CmdbAsset;
import com.project.uhdbackend.entity.CmdbAssetHardware;
import com.project.uhdbackend.entity.CmdbAssetNetwork;
import com.project.uhdbackend.entity.CmdbAssetOs;
import com.project.uhdbackend.repository.CmdbApplicationAssetRepository;
import com.project.uhdbackend.repository.CmdbApplicationRepository;
import com.project.uhdbackend.repository.CmdbAssetHardwareRepository;
import com.project.uhdbackend.repository.CmdbAssetNetworkRepository;
import com.project.uhdbackend.repository.CmdbAssetOsRepository;
import com.project.uhdbackend.repository.CmdbAssetRepository;

/**
 * 依 ASSET_ID 聚合 CMDB_ASSET / CMDB_ASSET_HARDWARE / CMDB_ASSET_OS /
 * CMDB_ASSET_NETWORK / CMDB_APPLICATION_ASSET，組成單一 AssetDetailDTO 回傳。
 *
 * 沿用既有作法：只讀查詢用 @Transactional(readOnly = true)， 找不到資產時丟
 * NoSuchElementException，交給 GlobalExceptionHandler 轉成 404。
 */
@Service
public class CmdbAssetQueryService {

	private final CmdbAssetRepository assetRepository;
	private final CmdbAssetHardwareRepository hardwareRepository;
	private final CmdbAssetOsRepository osRepository;
	private final CmdbAssetNetworkRepository networkRepository;
	private final CmdbApplicationAssetRepository applicationAssetRepository;
	private final CmdbApplicationRepository applicationRepository;

	public CmdbAssetQueryService(CmdbAssetRepository assetRepository, CmdbAssetHardwareRepository hardwareRepository,
			CmdbAssetOsRepository osRepository, CmdbAssetNetworkRepository networkRepository,
			CmdbApplicationAssetRepository applicationAssetRepository,
			CmdbApplicationRepository applicationRepository) {
		this.assetRepository = assetRepository;
		this.hardwareRepository = hardwareRepository;
		this.osRepository = osRepository;
		this.networkRepository = networkRepository;
		this.applicationAssetRepository = applicationAssetRepository;
		this.applicationRepository = applicationRepository;
	}

	@Transactional(readOnly = true)
	public List<AssetSummaryDTO> getAllAssetSummary() {

		List<CmdbAsset> assets = assetRepository.findAll();

		List<String> assetIds = assets.stream().map(CmdbAsset::getAssetId).toList();

		Map<String, List<String>> ipMap = networkRepository.findByAssetIdIn(assetIds).stream()
				.collect(Collectors.groupingBy(CmdbAssetNetwork::getAssetId,
						Collectors.mapping(CmdbAssetNetwork::getIpAddress, Collectors.toList())));

		List<AssetSummaryDTO> result = new ArrayList<>();

		for (CmdbAsset asset : assets) {

			AssetSummaryDTO dto = toAssetSummaryDTO(asset);

			dto.setIpList(ipMap.getOrDefault(asset.getAssetId(), Collections.emptyList()));

			result.add(dto);
		}

		return result;
	}

	@Transactional(readOnly = true)
	public AssetDetailDTO getAssetDetail(String assetId) {
		CmdbAsset asset = assetRepository.findByAssetId(assetId)
				.orElseThrow(() -> new NoSuchElementException("找不到資產，ASSET_ID=" + assetId));

		AssetDetailDTO dto = toAssetDetailDTO(assetId, asset);

		return dto;
	}

	private AssetSummaryDTO toAssetSummaryDTO(CmdbAsset asset) {

		AssetSummaryDTO dto = new AssetSummaryDTO();

		dto.setAssetId(asset.getAssetId());
		dto.setAssetName(asset.getAssetName());
		dto.setFunctionDesc(asset.getFunctionDesc());
		dto.setSecCode(asset.getSecCode());
		dto.setRackNo(asset.getRackNo());
		dto.setSizeU(asset.getSizeU());
		dto.setEnv(asset.getEnvironmentRaw());
		dto.setSystemCode(asset.getSystemCode());
		dto.setServiceTag(asset.getServiceTag());
		dto.setUPosition(asset.getUPosition());

		return dto;
	}

	private AssetDetailDTO toAssetDetailDTO(String assetId, CmdbAsset asset) {
		AssetDetailDTO dto = new AssetDetailDTO();
		dto.setAsset(toAssetInfo(asset));

		hardwareRepository.findByAssetId(assetId).ifPresent(hw -> dto.setHardware(toHardwareInfo(hw)));

		// 一個 Asset 可能有多筆 OS 歷史紀錄，這裡只取目前使用中的那一筆給前端顯示
		osRepository.findByAssetIdAndIsCurrent(assetId, "Y").ifPresent(os -> dto.setOs(toOsInfo(os)));

		List<CmdbAssetNetwork> networks = networkRepository.findByAssetId(assetId);
		dto.setNetworkList(networks.stream().map(this::toNetworkInfo).toList());

		List<CmdbApplicationAsset> relations = applicationAssetRepository.findByAssetId(assetId);
		dto.setApplications(relations.stream().map(this::toApplicationInfo).toList());

		List<CmdbAsset> vms = assetRepository.findByParentAssetId(assetId);
		dto.setVmList(buildVmSummaries(vms));
		dto.setVmCount(vms.size());

		return dto;
	}

	private AssetInfo toAssetInfo(CmdbAsset asset) {
		AssetInfo info = new AssetDetailDTO.AssetInfo();
		info.setAssetId(asset.getAssetId());
		info.setAssetCode(asset.getAssetCode());
		info.setAssetName(asset.getAssetName());
		info.setServerName(asset.getServerName());
		info.setAssetNo(asset.getAssetNo());
		info.setServiceTag(asset.getServiceTag());
		info.setAssetType(asset.getAssetType() != null ? asset.getAssetType().name() : null);
		info.setAssetTypeRaw(asset.getAssetTypeRaw());
		info.setModel(asset.getModel());
		info.setIsVirtual(asset.getIsVirtual());
		info.setHostName(asset.getHostName());
		info.setParentAssetId(asset.getParentAssetId());
		info.setEnvironment(asset.getEnvironment());
		info.setEnvironmentRaw(asset.getEnvironmentRaw());
		info.setSystemCode(asset.getSystemCode());
		info.setFunctionDesc(asset.getFunctionDesc());
		info.setSecCode(asset.getSecCode());
		info.setRackNo(asset.getRackNo());
		info.setuPosition(asset.getUPosition());
		info.setSizeU(asset.getSizeU());
		info.setCustodyDept(asset.getCustodyDept());
		info.setMtaGroupCode(asset.getMtaGroupCode());
		info.setMtaGroupName(asset.getMtaGroupName());
		info.setMtaUser(asset.getMtaUser());
		info.setAcquireDate(asset.getAcquireDate());
		info.setStatus(asset.getStatus() != null ? asset.getStatus().name() : null);
		info.setRemark(asset.getRemark());
		info.setMigrationPlan(asset.getMigrationPlan());
		info.setLocation(asset.getLocation());
		info.setRoom(asset.getRoom());
		return info;
	}

	private HardwareInfo toHardwareInfo(CmdbAssetHardware hw) {
		HardwareInfo info = new AssetDetailDTO.HardwareInfo();
		info.setCpuModel(hw.getCpuModel());
		info.setCoreDesc(hw.getCoreDesc());
		info.setDiskSize(hw.getDiskSize());
		info.setDiskCount(hw.getDiskCount());
		info.setMemorySize(hw.getMemorySize());
		info.setHbaCard(hw.getHbaCard());
		info.setNetworkCard(hw.getNetworkCard());
		info.setPowerSupply(hw.getPowerSupply());
		info.setVoltage(hw.getVoltage());
		info.setPowerConsumption(hw.getPowerConsumption());
		info.setVoltageRange(hw.getVoltageRange());
		info.setMaintainVendor(hw.getMaintainVendor());
		info.setMaintainStartDate(hw.getMaintainStartDate());
		info.setMaintainEndDate(hw.getMaintainEndDate());
		info.setMaintainType(hw.getMaintainType());
		info.setGpuModel(hw.getGpuModel());
		info.setGpuCount(hw.getGpuCount());
		info.setRemark(hw.getRemark());
		return info;
	}

	private OsInfo toOsInfo(CmdbAssetOs os) {
		OsInfo info = new AssetDetailDTO.OsInfo();
		info.setOsName(os.getOsName());
		info.setOsVersion(os.getOsVersion());
		info.setOsFamily(os.getOsFamily());
		info.setIsCurrent(os.getIsCurrent());
		info.setRemark(os.getRemark());
		return info;
	}

	private NetworkInfo toNetworkInfo(CmdbAssetNetwork network) {
		NetworkInfo info = new AssetDetailDTO.NetworkInfo();
		info.setIpAddress(network.getIpAddress());
//		info.setMacAddress(network.getMacAddress());
		info.setDnsName(network.getDnsName());
		info.setIsPrimary(network.getIsPrimary());
		info.setDescription(network.getDescription());
		info.setInterfaceName(network.getInterfaceName());
		info.setRemark(network.getRemark());
		return info;
	}

	private ApplicationInfo toApplicationInfo(CmdbApplicationAsset relation) {
		ApplicationInfo info = new AssetDetailDTO.ApplicationInfo();
		info.setRelationType(relation.getRelationType() != null ? relation.getRelationType().name() : null);
		applicationRepository.findByApplicationId(relation.getApplicationId()).ifPresentOrElse(app -> {
			info.setApplicationCode(app.getApplicationCode());
			info.setApplicationName(app.getApplicationName());
		}, () -> info.setApplicationCode(relation.getApplicationId()));
		return info;
	}

	private List<AssetDetailDTO.VmSummary> buildVmSummaries(List<CmdbAsset> vms) {
		if (vms.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> vmAssetIds = vms.stream().map(CmdbAsset::getAssetId).toList();

		// 批次撈 IP：一個 VM 可能多筆 network，全部收集成 List
		Map<String, List<String>> ipMap = networkRepository.findByAssetIdIn(vmAssetIds).stream()
				.collect(Collectors.groupingBy(CmdbAssetNetwork::getAssetId,
						Collectors.mapping(CmdbAssetNetwork::getIpAddress, Collectors.toList())));

		// 批次撈 Guest OS：只取 isCurrent='Y' 那筆
		Map<String, String> osMap = osRepository.findByAssetIdInAndIsCurrent(vmAssetIds, "Y").stream()
				.collect(Collectors.toMap(CmdbAssetOs::getAssetId, CmdbAssetOs::getOsName, (a, b) -> a));

		return vms.stream().map(vm -> {
			AssetDetailDTO.VmSummary summary = new AssetDetailDTO.VmSummary();
			summary.setAssetId(vm.getAssetId());
			summary.setHost(vm.getHostName());
			summary.setSec(vm.getSecCode());
			summary.setEnv(vm.getEnvironmentRaw());
			summary.setSys(vm.getSystemCode());
			summary.setName(vm.getServerName());
			summary.setIpList(ipMap.getOrDefault(vm.getAssetId(), Collections.emptyList()));
			summary.setNotes(vm.getFunctionDesc());
			summary.setGuestOs(osMap.get(vm.getAssetId()));
			summary.setStatus(vm.getStatus() != null ? vm.getStatus().name() : null);
			return summary;
		}).toList();
	}
}