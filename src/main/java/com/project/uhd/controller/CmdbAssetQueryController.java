package com.project.uhd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhd.dto.ApiResponse;
import com.project.uhd.dto.AssetDetailDTO;
import com.project.uhd.dto.AssetSummaryDTO;
import com.project.uhd.service.CmdbAssetQueryService;

@RestController
@RequestMapping("/api/v1/assets")
public class CmdbAssetQueryController {

	private final CmdbAssetQueryService queryService;

	public CmdbAssetQueryController(CmdbAssetQueryService queryService) {
		this.queryService = queryService;
	}

	/**
	 * 依 ASSET_ID 查詢資產完整資料（CMDB_ASSET + HARDWARE + OS + NETWORK + 掛載的 APPLICATION）。
	 * 找不到會由 GlobalExceptionHandler 攔截 NoSuchElementException 轉成 404。
	 */
	@GetMapping("/{assetId}")
	public ApiResponse<AssetDetailDTO> getAssetDetail(@PathVariable String assetId) {
		AssetDetailDTO detail = queryService.getAssetDetail(assetId);
		return new ApiResponse<>(true, "Asset detail fetched!", detail);
	}

	@GetMapping("/all")
	public ApiResponse<List<AssetSummaryDTO>> getAssetSummary() {
		List<AssetSummaryDTO> details = queryService.getAllAssetSummary();
		return new ApiResponse<>(true, "All Asset detail fetched!", details);
	}
}