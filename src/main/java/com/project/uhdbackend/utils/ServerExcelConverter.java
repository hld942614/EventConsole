//package com.project.uhdbackend.utils;
//
//import org.springframework.stereotype.Component;
//
//import com.project.uhdbackend.dto.AssetDTO;
//import com.project.uhdbackend.dto.HardwareSpecDTO;
//import com.project.uhdbackend.dto.MaintenanceRecordDTO;
//import com.project.uhdbackend.dto.MtaGroupDTO;
//import com.project.uhdbackend.dto.NetworkInfoDTO;
//import com.project.uhdbackend.dto.ServerExcelRowDTO;
//import com.project.uhdbackend.dto.ServerInfoDTO;
//import com.project.uhdbackend.dto.ServerListDTO;
//
//@Component
//public class ServerExcelConverter {
//
//    public ServerListDTO toServerListDTO(ServerExcelRowDTO row) {
//
//        ServerInfoDTO serverInfo = buildServerInfo(row);
//        HardwareSpecDTO hardwareSpec = buildHardwareSpec(row);
//        MaintenanceRecordDTO maintenanceRecord = buildMaintenanceRecord(row);
//        NetworkInfoDTO networkInfo = buildNetworkInfo(row);
//        AssetDTO asset = buildAsset(row);
//        MtaGroupDTO mtaGroup = buildMtaGroup(row);
//
//        return new ServerListDTO(serverInfo, hardwareSpec, maintenanceRecord, mtaGroup, networkInfo, asset);
//    }
//
//    private ServerInfoDTO buildServerInfo(ServerExcelRowDTO row) {
//        ServerInfoDTO dto = new ServerInfoDTO();
//        dto.setSec(row.getSec());
//        dto.setRack(row.getRack());
//        dto.setRackU(row.getRackU());
//        dto.setEnv(row.getEnv());
//        dto.setSystem(row.getSystem());
//        dto.setServerName(row.getServerName());
//        dto.setModel(row.getModel());
//        dto.setServiceTag(row.getServiceTag());
//        dto.setSizeU(row.getSizeU());
//        dto.setFunction(row.getFunction());
//        dto.setOs(row.getOs());
//        dto.setRemark(row.getRemark());
//        return dto;
//    }
//
//    private HardwareSpecDTO buildHardwareSpec(ServerExcelRowDTO row) {
//        HardwareSpecDTO dto = new HardwareSpecDTO();
//        dto.setCpuModel(row.getCpuModel());
//        dto.setCoreCount(row.getCoreCount());
//        dto.setHdSize(row.getHdSize());
//        dto.setHdCount(row.getHdCount());
//        dto.setMemorySpec(row.getMemorySpec());
//        dto.setHbaCard(row.getHbaCard());
//        dto.setNicCount(row.getNicCount());
//        dto.setPsuType(row.getPsuType());
//        dto.setVoltage(row.getVoltage());
//        dto.setPowerWatts(row.getPowerWatts());
//        dto.setVoltageRange(row.getVoltageRange());
//        return dto;
//    }
//
//    private MaintenanceRecordDTO buildMaintenanceRecord(ServerExcelRowDTO row) {
//        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
//        dto.setLargeType(row.getLargeType());
//        dto.setMaintVendor(row.getMaintVendor());
//        dto.setMaintStart(row.getMaintStart());
//        dto.setMaintEnd(row.getMaintEnd());
//        dto.setMaintType(row.getMaintType());
//        return dto;
//    }
//
//    private NetworkInfoDTO buildNetworkInfo(ServerExcelRowDTO row) {
//        NetworkInfoDTO dto = new NetworkInfoDTO();
//        dto.setIpUserlan(row.getIpUserlan());
//        dto.setBkIp(row.getBkIp());
//        dto.setiDRACIp(row.getIDRACIp());
//        return dto;
//    }
//
//    private AssetDTO buildAsset(ServerExcelRowDTO row) {
//        AssetDTO dto = new AssetDTO();
//        dto.setAssetNo(row.getAssetNo());
//        dto.setAcquiredDate(row.getAcquiredDate());
//        dto.setCustodianUnit(row.getCustodianUnit());
//        return dto;
//    }
//
//    private MtaGroupDTO buildMtaGroup(ServerExcelRowDTO row) {
//        MtaGroupDTO dto = new MtaGroupDTO();
//        dto.setMtaGroupCode(row.getMtaGroupCode());
//        dto.setMtaGroupName(row.getMtaGroupName());
//        dto.setMtaUser(row.getMtaUser());
//        return dto;
//    }
//}
