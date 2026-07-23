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
//import com.project.uhdbackend.dto.NetworkInfoDTO;
//import com.project.uhdbackend.entity.NetworkInfo;
//import com.project.uhdbackend.service.NetworkInfoService;
//
//@RestController
//@RequestMapping("/api/v1/networkinfo")
//public class NetworkInfoController {
//
//    private final NetworkInfoService networkInfoService;
//
//    public NetworkInfoController(NetworkInfoService networkInfoService) {
//        this.networkInfoService = networkInfoService;
//    }
//
//    @GetMapping("/all")
//    public List<NetworkInfo> getAllNetworkInfos() {
//        return networkInfoService.getAllInfos();
//    }
//
//    @PostMapping("/save")
//    public void save(@RequestBody NetworkInfo networkInfo) {
//        networkInfoService.save(networkInfo);
//    }
//
//    @GetMapping("/server/{serverId}")
//    public ResponseEntity<ApiResponse<NetworkInfoDTO>> getByServerId(@PathVariable Long serverId) {
//        try {
//            NetworkInfo entity = networkInfoService.getNetworkInfoByServerId(serverId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "NetworkInfo fetched!", new NetworkInfoDTO(entity)));
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, ex.getMessage(), null));
//        }
//    }
//}
