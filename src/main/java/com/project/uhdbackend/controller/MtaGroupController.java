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
//import com.project.uhdbackend.dto.MtaGroupDTO;
//import com.project.uhdbackend.entity.MtaGroup;
//import com.project.uhdbackend.service.MtaGroupService;
//
//@RestController
//@RequestMapping("/api/v1/mtagroup")
//public class MtaGroupController {
//
//    private final MtaGroupService mtaGroupService;
//
//    public MtaGroupController(MtaGroupService mtaGroupService) {
//        this.mtaGroupService = mtaGroupService;
//    }
//
//    @GetMapping("/all")
//    public List<MtaGroup> getAllMtaGroups() {
//        return mtaGroupService.getAllMtaGroups();
//    }
//
//    @PostMapping("/save")
//    public void save(@RequestBody MtaGroup mtaGroup) {
//        mtaGroupService.save(mtaGroup);
//    }
//
//    @GetMapping("/server/{serverId}")
//    public ResponseEntity<ApiResponse<MtaGroupDTO>> getByServerId(@PathVariable Long serverId) {
//        try {
//            MtaGroup entity = mtaGroupService.getMtaGroupByServerId(serverId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "MtaGroup fetched!", new MtaGroupDTO(entity)));
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, ex.getMessage(), null));
//        }
//    }
//}
