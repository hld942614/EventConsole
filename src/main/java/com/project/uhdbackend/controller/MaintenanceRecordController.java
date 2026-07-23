//package com.project.uhdbackend.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.project.uhdbackend.dto.ApiResponse;
//import com.project.uhdbackend.dto.MaintenanceRecordDTO;
//import com.project.uhdbackend.entity.MaintenanceRecord;
//import com.project.uhdbackend.service.MaintenanceRecordService;
//
//@RestController
//@RequestMapping("/api/v1/maintenancerecord")
//public class MaintenanceRecordController {
//
//    private final MaintenanceRecordService maintenanceRecordService;
//
//    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
//        this.maintenanceRecordService = maintenanceRecordService;
//    }
//
//    @GetMapping("/all")
//    public List<MaintenanceRecord> getAllMaintenanceRecords() {
//        return maintenanceRecordService.getAllMaintenanceRecords();
//    }
//
//    @PostMapping("/save")
//    public void save(@RequestBody MaintenanceRecord maintenanceRecord) {
//        maintenanceRecordService.save(maintenanceRecord);
//    }
//
//    @GetMapping("/server/{serverId}")
//    public ResponseEntity<ApiResponse<MaintenanceRecordDTO>> getByServerId(@PathVariable Long serverId) {
//        try {
//            MaintenanceRecord entity = maintenanceRecordService.getMaintenanceRecordByServerId(serverId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "MaintenanceRecord fetched!", new MaintenanceRecordDTO(entity)));
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, ex.getMessage(), null));
//        }
//    }
//}
