//package com.project.uhdbackend.controller;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.project.uhdbackend.dto.ApiResponse;
//import com.project.uhdbackend.dto.ServerListDTO;
//import com.project.uhdbackend.service.ServerListService;
//
//@RestController
//@RequestMapping("/api/v1/serverList")
//public class ServerListController {
//	@Autowired
//	private ServerListService serverListService;
//
//	@GetMapping("/{id}")
//	public ResponseEntity<ApiResponse<ServerListDTO>> getById(@PathVariable Long id) {
//		try {
//			ServerListDTO entity = serverListService.getServerListById(id);
//			return ResponseEntity.ok(new ApiResponse<>(true, "ServerInfo fetched!", entity));
//		} catch (Exception ex) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null));
//		}
//	}
//
//	@PostMapping("/save")
//	public ResponseEntity<ApiResponse<Void>> saveServerDetail(@RequestBody ServerListDTO dto) {
//		serverListService.saveServerList(dto);
//		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Success.", null));
//	}
//
//	@PutMapping("/{id}")
//	public ResponseEntity<ApiResponse<ServerListDTO>> update(@PathVariable Long id,
//			@RequestBody ServerListDTO request) {
//
//		try {
//			ServerListDTO updated = serverListService.updateServerList(id, request);
//			return ResponseEntity.ok(new ApiResponse<>(true, "ServerInfo updated!", updated));
//		} catch (IllegalArgumentException ex) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null));
//		} catch (Exception ex) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(new ApiResponse<>(false, "Update failed: " + ex.getMessage(), null));
//		}
//	}
//
//	@DeleteMapping("/{serverInfoId}")
//	public ResponseEntity<ApiResponse<Void>> deleteServerDetail(@PathVariable Long serverInfoId) {
//		try {
//			serverListService.deleteServerList(serverInfoId);
//			return ResponseEntity.ok(new ApiResponse<>(true, "Delete successfully", null));
//		} catch (NoSuchElementException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
//		}
//	}
//	
//	@GetMapping("/all")
//    public ResponseEntity<ApiResponse<List<ServerListDTO>>> getAllServerDetails() {
//        List<ServerListDTO> list = serverListService.getAllServerList();
//        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
//    }
//	
//	@PostMapping("/import")
//	public ResponseEntity<Void> importExcel(@RequestParam("file") MultipartFile file) {
//	    if (file.isEmpty()) {
//	        return ResponseEntity.badRequest().build();
//	    }
//
//	    String filename = file.getOriginalFilename();
//	    if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
//	        return ResponseEntity.badRequest().build();
//	    }
//
//	    serverListService.importFromExcel(file);
//	    return ResponseEntity.ok().build();
//	}
//}
