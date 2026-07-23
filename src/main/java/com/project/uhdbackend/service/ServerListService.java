//package com.project.uhdbackend.service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.project.uhdbackend.dto.AssetDTO;
//import com.project.uhdbackend.dto.HardwareSpecDTO;
//import com.project.uhdbackend.dto.MaintenanceRecordDTO;
//import com.project.uhdbackend.dto.MtaGroupDTO;
//import com.project.uhdbackend.dto.NetworkInfoDTO;
//import com.project.uhdbackend.dto.ServerExcelRowDTO;
//import com.project.uhdbackend.dto.ServerInfoDTO;
//import com.project.uhdbackend.dto.ServerListDTO;
//import com.project.uhdbackend.entity.Asset;
//import com.project.uhdbackend.entity.HardwareSpec;
//import com.project.uhdbackend.entity.MaintenanceRecord;
//import com.project.uhdbackend.entity.MtaGroup;
//import com.project.uhdbackend.entity.NetworkInfo;
//import com.project.uhdbackend.entity.ServerInfo;
//import com.project.uhdbackend.repository.AssetRepository;
//import com.project.uhdbackend.repository.HardwareSpecRepository;
//import com.project.uhdbackend.repository.MaintenanceRecordRepository;
//import com.project.uhdbackend.repository.MtaGroupRepository;
//import com.project.uhdbackend.repository.NetworkInfoRepository;
//import com.project.uhdbackend.repository.ServerInfoRepository;
//import com.project.uhdbackend.utils.ServerExcelConverter;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import cn.idev.excel.FastExcel;
//import cn.idev.excel.read.listener.PageReadListener;
//
//@Service
//public class ServerListService {
//
//	private final ServerInfoRepository serverInfoRepository;
//	private final HardwareSpecRepository hardwareSpecRepository;
//	private final MaintenanceRecordRepository maintenanceRecordRepository;
//	private final MtaGroupRepository mtaGroupRepository;
//	private final NetworkInfoRepository networkInfoRepository;
//	private final AssetRepository assetRepository;
//	private final ServerExcelConverter serverExcelConverter;
//	private final ObjectMapper objectMapper = new ObjectMapper();
//
//	public ServerListService(ServerInfoRepository serverInfoRepository, HardwareSpecRepository hardwareSpecRepository,
//			MaintenanceRecordRepository maintenanceRecordRepository, MtaGroupRepository mtaGroupRepository,
//			NetworkInfoRepository networkInfoRepository, AssetRepository assetRepository,
//			ServerExcelConverter serverExcelConverter) {
//		this.serverInfoRepository = serverInfoRepository;
//		this.hardwareSpecRepository = hardwareSpecRepository;
//		this.maintenanceRecordRepository = maintenanceRecordRepository;
//		this.mtaGroupRepository = mtaGroupRepository;
//		this.networkInfoRepository = networkInfoRepository;
//		this.assetRepository = assetRepository;
//		this.serverExcelConverter = serverExcelConverter;
//	}
//
//	@Transactional
//	public void importFromExcel(MultipartFile file) {
//		List<ServerExcelRowDTO> rows = new ArrayList<>();
//
//		try {
//			FastExcel.read(file.getInputStream(), ServerExcelRowDTO.class,
//					new PageReadListener<ServerExcelRowDTO>(rows::addAll)).sheet().doRead();
//		} catch (IOException e) {
//			throw new RuntimeException("Excel 讀取失敗：" + e.getMessage(), e);
//		}
//
//		rows.stream().map(serverExcelConverter::toServerListDTO).forEach(this::saveServerList);
//	}
//
//	@Transactional(readOnly = true)
//	public ServerListDTO getServerListById(Long serverId) {
//
//		ServerInfoDTO serverInfo = new ServerInfoDTO(serverInfoRepository.findById(serverId)
//				.orElseThrow(() -> new IllegalArgumentException("Server not found. id=" + serverId)));
//
//		HardwareSpecDTO hardwareSpec = new HardwareSpecDTO(
//				hardwareSpecRepository.findByServer_Id(serverId).orElse(null));
//
//		MaintenanceRecordDTO maintenanceRecord = new MaintenanceRecordDTO(
//				maintenanceRecordRepository.findByServer_Id(serverId).orElse(null));
//
//		MtaGroupDTO mtaGroup = new MtaGroupDTO(mtaGroupRepository.findByServer_Id(serverId).orElse(null));
//
//		NetworkInfoDTO networkInfo = new NetworkInfoDTO(networkInfoRepository.findByServer_Id(serverId).orElse(null));
//
//		AssetDTO asset = new AssetDTO(assetRepository.findByServer_Id(serverId).orElse(null));
//
//		return new ServerListDTO(serverInfo, hardwareSpec, maintenanceRecord, mtaGroup, networkInfo, asset);
//	}
//
//	@Transactional
//	public ServerListDTO updateServerList(Long serverId, ServerListDTO dto) {
//
//		// 1. ServerInfo
//		ServerInfo serverInfo = serverInfoRepository.findById(serverId)
//				.orElseThrow(() -> new IllegalArgumentException("Server not found. id=" + serverId));
//
//		ServerInfoDTO serverInfoDto = dto.getServerInfo();
//		if (serverInfoDto != null) {
//			serverInfo.updateFrom(serverInfoDto);
//		}
//
//		// 2. HardwareSpec
//		HardwareSpec hardwareSpec = null;
//		HardwareSpecDTO hwDto = dto.getHardwareSpec();
//		if (hwDto != null) {
//			Long hwId = hwDto.getId();
//			if (hwId != null) {
//				hardwareSpec = hardwareSpecRepository.findById(hwId)
//						.orElseThrow(() -> new IllegalArgumentException("HardwareSpec not found. id=" + hwId));
//
//				if (hardwareSpec.getServer() == null || !serverId.equals(hardwareSpec.getServer().getId())) {
//					throw new IllegalArgumentException("HardwareSpec does not belong to server id=" + serverId);
//				}
//			} else {
//				hardwareSpec = new HardwareSpec();
//				hardwareSpec.setServer(serverInfo);
//			}
//			hardwareSpec.updateFrom(hwDto);
//		}
//
//		// 3. MaintenanceRecord
//		MaintenanceRecord maintenanceRecord = null;
//		MaintenanceRecordDTO mrDto = dto.getMaintenanceRecord();
//		if (mrDto != null) {
//			Long mrId = mrDto.getId();
//			if (mrId != null) {
//				maintenanceRecord = maintenanceRecordRepository.findById(mrId)
//						.orElseThrow(() -> new IllegalArgumentException("MaintenanceRecord not found. id=" + mrId));
//
//				if (maintenanceRecord.getServer() == null || !serverId.equals(maintenanceRecord.getServer().getId())) {
//					throw new IllegalArgumentException("MaintenanceRecord does not belong to server id=" + serverId);
//				}
//			} else {
//				maintenanceRecord = new MaintenanceRecord();
//				maintenanceRecord.setServer(serverInfo);
//			}
//			maintenanceRecord.updateFrom(mrDto);
//		}
//
//		// 4. MtaGroup
//		MtaGroup mtaGroup = null;
//		MtaGroupDTO mtaDto = dto.getMtaGroup();
//		if (mtaDto != null) {
//			Long mtaId = mtaDto.getId();
//			if (mtaId != null) {
//				mtaGroup = mtaGroupRepository.findById(mtaId)
//						.orElseThrow(() -> new IllegalArgumentException("MtaGroup not found. id=" + mtaId));
//
//				if (mtaGroup.getServer() == null || !serverId.equals(mtaGroup.getServer().getId())) {
//					throw new IllegalArgumentException("MtaGroup does not belong to server id=" + serverId);
//				}
//			} else {
//				mtaGroup = new MtaGroup();
//				mtaGroup.setServer(serverInfo);
//			}
//			mtaGroup.updateFrom(mtaDto);
//		}
//
//		// 5. NetworkInfo
//		NetworkInfo networkInfo = null;
//		NetworkInfoDTO netDto = dto.getNetworkInfo();
//		if (netDto != null) {
//			Long netId = netDto.getId();
//			if (netId != null) {
//				networkInfo = networkInfoRepository.findById(netId)
//						.orElseThrow(() -> new IllegalArgumentException("NetworkInfo not found. id=" + netId));
//
//				if (networkInfo.getServer() == null || !serverId.equals(networkInfo.getServer().getId())) {
//					throw new IllegalArgumentException("NetworkInfo does not belong to server id=" + serverId);
//				}
//			} else {
//				networkInfo = new NetworkInfo();
//				networkInfo.setServer(serverInfo);
//			}
//			networkInfo.updateFrom(netDto);
//		}
//
//		// 6.Asset
//		Asset asset = null;
//		AssetDTO assetDto = dto.getAsset();
//		if (assetDto != null) {
//			Long assetId = assetDto.getId();
//			if (assetId != null) {
//				asset = assetRepository.findById(assetId)
//						.orElseThrow(() -> new IllegalArgumentException("Asset not found. id=" + assetId));
//
//				if (asset.getServer() == null || !serverId.equals(asset.getServer().getId())) {
//					throw new IllegalArgumentException("Asset does not belong to server id=" + serverId);
//				}
//			} else {
//				asset = new Asset();
//				asset.setServer(serverInfo);
//			}
//			asset.updateFrom(assetDto);
//		}
//
//		ServerInfoDTO savedServerInfo = new ServerInfoDTO(serverInfoRepository.save(serverInfo));
//		if (hardwareSpec != null) {
//			hardwareSpec = hardwareSpecRepository.save(hardwareSpec);
//		}
//		if (maintenanceRecord != null) {
//			maintenanceRecord = maintenanceRecordRepository.save(maintenanceRecord);
//		}
//		if (mtaGroup != null) {
//			mtaGroup = mtaGroupRepository.save(mtaGroup);
//		}
//		if (networkInfo != null) {
//			networkInfo = networkInfoRepository.save(networkInfo);
//		}
//		if (asset != null) {
//			asset = assetRepository.save(asset);
//		}
//
//		return new ServerListDTO(savedServerInfo, hwDto, mrDto, mtaDto, netDto, assetDto);
//	}
//
//	@Transactional
//	public void saveServerList(ServerListDTO dto) {
//		if (dto == null || dto.getServerInfo() == null) {
//			throw new IllegalArgumentException("serverInfo 不可為空");
//		}
//
//		ServerInfo info = new ServerInfo();
//		info.updateFrom(dto.getServerInfo());
//		info = serverInfoRepository.save(info);
//
//		if (dto.getHardwareSpec() != null) {
//			HardwareSpec hardwareSpec = new HardwareSpec();
//			hardwareSpec.updateFrom(dto.getHardwareSpec());
//			hardwareSpec.setServer(info);
//			hardwareSpecRepository.save(hardwareSpec);
//		}
//
//		if (dto.getMaintenanceRecord() != null) {
//			MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
//			maintenanceRecord.updateFrom(dto.getMaintenanceRecord());
//			maintenanceRecord.setServer(info);
//			maintenanceRecordRepository.save(maintenanceRecord);
//		}
//
//		if (dto.getMtaGroup() != null) {
//			MtaGroup mtaGroup = new MtaGroup();
//			mtaGroup.updateFrom(dto.getMtaGroup());
//			mtaGroup.setServer(info);
//			mtaGroupRepository.save(mtaGroup);
//		}
//
//		if (dto.getNetworkInfo() != null) {
//			NetworkInfo networkInfo = new NetworkInfo();
//			networkInfo.updateFrom(dto.getNetworkInfo());
//			networkInfo.setServer(info);
//			networkInfoRepository.save(networkInfo);
//		}
//
//		if (dto.getAsset() != null) {
//			Asset asset = new Asset();
//			asset.updateFrom(dto.getAsset());
//			asset.setServer(info);
//			assetRepository.save(asset);
//		}
//	}
//
//	@Transactional
//	public void deleteServerList(Long serverInfoId) {
//		ServerInfo info = serverInfoRepository.findById(serverInfoId)
//				.orElseThrow(() -> new NoSuchElementException("ServerInfo not found: " + serverInfoId));
//		serverInfoRepository.delete(info);
//	}
//
//	@Transactional(readOnly = true)
//	public List<ServerListDTO> getAllServerList() {
//		List<ServerInfo> list = serverInfoRepository.findAll();
//		return list.stream().map(ServerListDTO::new).toList();
//	}
//}
