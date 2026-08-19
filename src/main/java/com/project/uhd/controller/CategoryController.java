package com.project.uhd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.CategoryContentUpdateRequest;
import com.project.uhd.dto.CategoryCreateRequest;
import com.project.uhd.dto.EventCategoryStatsDTO;
import com.project.uhd.dto.UpdateParentDto;
import com.project.uhd.entity.Category;
import com.project.uhd.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@PostMapping("/save")
	public ResponseEntity<ApiResponse<Category>> saveCategory(@RequestBody CategoryCreateRequest request) {
		if (request.getCode() == null || request.getCode().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Code is null or empty", null));
		}
		if (service.getId(request.getCode()) != -1) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Code already exists", null));
		}
		if (request.getParentId() < 0) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "ParentId is invalid", null));
		}

		Category saved = service.save(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Category saved successfully", saved));
	}

	@GetMapping("/alert/{parentId}")
	public ResponseEntity<ApiResponse<List<Category>>> getAlertByParentId(@PathVariable int parentId) {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Alert categories retrieved", service.getAlertByParentId(parentId)));
	}

	@GetMapping("/sub/{parentId}")
	public ResponseEntity<ApiResponse<List<Category>>> getSubByParentId(@PathVariable int parentId) {
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Sub categories retrieved", service.getSubByParentId(parentId)));
	}
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<Category>>> getAllCategory() {
		return ResponseEntity.ok(new ApiResponse<>(true, "All categories retrieved", service.getAll()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted", null));
	}

	@GetMapping("/count/event")
	public ResponseEntity<ApiResponse<List<EventCategoryStatsDTO>>> getEventCategoryStats() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Category stats (event)", service.getEventCategoryStats()));
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<ApiResponse<Category>> getDetailById(@PathVariable Long id) {
		Optional<Category> optional = service.findById(id);
		return optional.map(category -> ResponseEntity.ok(new ApiResponse<>(true, "Category detail found", category)))
				.orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, "Category not found", null)));
	}

	@PostMapping("/update/content")
	public ResponseEntity<ApiResponse<Void>> updateContent(@RequestBody CategoryContentUpdateRequest request) {
		if (request.getId() == null) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "id is required", null));
		}
		service.updateContent(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Content updated", null));
	}

	@PostMapping("/update/type")
	public ResponseEntity<ApiResponse<Void>> updateType(@RequestBody UpdateParentDto dto) {
		service.updateParentId(dto);
		return ResponseEntity.ok(new ApiResponse<>(true, "Parent ID updated", null));
	}
}
