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
//import com.project.uhdbackend.dto.HardwareSpecDTO;
//import com.project.uhdbackend.entity.HardwareSpec;
//import com.project.uhdbackend.service.HardwareSpecService;
//
//@RestController
//@RequestMapping("/api/v1/hardwarespec")
//public class HardwareSpecController {
//
//	private HardwareSpecService hardwareSpecService;
//
//	public HardwareSpecController(HardwareSpecService hardwareSpecService) {
//		this.hardwareSpecService = hardwareSpecService;
//	}
//
//	@GetMapping("/all")
//	public List<HardwareSpec> getAllHardwareSpecs() {
//		return hardwareSpecService.getAllHardwareSpec();
//	}
//
//	@PostMapping("/save")
//	public void save(@RequestBody HardwareSpec hardwareSpec) {
//		hardwareSpecService.save(hardwareSpec);
//	}
//
//	@GetMapping("/server/{serverId}")
//	public ResponseEntity<ApiResponse<HardwareSpecDTO>> getByServerId(@PathVariable Long serverId) {
//		try {
//			HardwareSpec spec = hardwareSpecService.getHardwareSpecByServerId(serverId);
//			return ResponseEntity.ok(new ApiResponse<>(true, "HardwareSpec fetched!", new HardwareSpecDTO(spec)));
//		} catch (Exception ex) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null));
//		}
//	}
//
////	@PostMapping("/test")
////	public void test(@RequestBody String input) {
////		JSONArray ja = new JSONArray(input);
////		for (int i = 0; i < ja.length(); i++) {
////			JSONObject jo = ja.getJSONObject(i);
////			Map<String, Object> map = jo.toMap();
////			ServerInfo serverInfo = mapper.convertValue(map, ServerInfo.class);
////			String serverName = serverInfo.getServerName();
////			String rack = serverInfo.getRack();
////			String model = serverInfo.getModel();
////			serverInfo = serverInfoService.getByServerName(serverName, rack, model);
////			// HardwareSpec
////			HardwareSpec hardwareSpec = mapper.convertValue(map, HardwareSpec.class);
////			hardwareSpec.setServer(serverInfo);
////			System.out.println("HardwareSpec : " + hardwareSpec);
////			hardwareSpecService.save(hardwareSpec);
////			// MaintenanceRecord
////			MaintenanceRecord maintenanceRecord = mapper.convertValue(map, MaintenanceRecord.class);
////			maintenanceRecord.setServer(serverInfo);
////			System.out.println("MaintenanceRecord : " + maintenanceRecord);
////			maintenanceRecordService.save(maintenanceRecord);
////			// NetworkInfo
////			NetworkInfo networkInfo = mapper.convertValue(map, NetworkInfo.class);
////			networkInfo.setServer(serverInfo);
////			System.out.println("NetworkInfo : " + networkInfo);
////			networkInfoService.save(networkInfo);
////			// MTA Group
////			MtaGroup mtaGroup = mapper.convertValue(map, MtaGroup.class);
////			mtaGroup.setServer(serverInfo);
////			System.out.println("MtaGroup : " + mtaGroup);
////			mtaGroupService.save(mtaGroup);
////		}
////	}
//}
