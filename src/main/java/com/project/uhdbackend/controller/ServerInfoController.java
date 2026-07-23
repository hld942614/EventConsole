//package com.project.uhdbackend.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
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
//import com.project.uhdbackend.dto.ServerInfoDTO;
//import com.project.uhdbackend.entity.ServerInfo;
//import com.project.uhdbackend.service.ServerInfoService;
//
//@RestController
//@RequestMapping("/api/v1/server/info")
//public class ServerInfoController {
//	@Autowired
//    private ServerInfoService serverInfoService;
//	
//	@GetMapping("/all")
//	public List<ServerInfo> getAllInfos() {
//		return serverInfoService.getAllInfos();
//	}
//	
//	@PostMapping("/save")
//	public void saveInfo(@RequestBody ServerInfo info) {
//		serverInfoService.save(info);
//	}
//	
//	@GetMapping("/{serverId}")
//    public ResponseEntity<ApiResponse<ServerInfoDTO>> getByServerId(@PathVariable Long serverId) {
//        try {
//        	ServerInfo entity = serverInfoService.findById(serverId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "ServerInfo fetched!", new ServerInfoDTO(entity)));
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, ex.getMessage(), null));
//        }
//    }
//}
