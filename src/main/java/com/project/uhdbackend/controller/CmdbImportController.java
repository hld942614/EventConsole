package com.project.uhdbackend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.uhdbackend.dto.CmdbAssetExcelRowDTO;
import com.project.uhdbackend.dto.ImportResultDTO;
import com.project.uhdbackend.service.CmdbImportService;

import cn.idev.excel.FastExcel;
import cn.idev.excel.read.listener.PageReadListener;

@RestController
@RequestMapping("/api/v1/cmdb")
public class CmdbImportController {

	private final CmdbImportService importService;

	public CmdbImportController(CmdbImportService importService) {
		this.importService = importService;
	}

	/**
	 * 上傳「實體資產清單」Excel，匯入 CMDB_ASSET 及相關表。
	 *
	 */
	@PostMapping("/import")
	public ResponseEntity<ImportResultDTO> importAssets(@RequestParam("file") MultipartFile file) {
		ImportResultDTO result = importService.importFromExcel(file);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/import/vm")
	public ResponseEntity<ImportResultDTO> importVms(@RequestParam("file") MultipartFile file) {
		ImportResultDTO result = importService.importVmFromExcel(file);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/import2")
	public void importAssets2(@RequestParam("file") MultipartFile file) {
//		List<CmdbAssetExcelRowDTO> rows = new ArrayList<>();
//		try (InputStream is = file.getInputStream()) {
//			rows = FastExcel.read(is).head(CmdbAssetExcelRowDTO.class).sheet("大機房").doReadSync();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RowValidationException("讀取 Excel 檔案失敗: " + e.getMessage());
//		}
		List<CmdbAssetExcelRowDTO> rows = new ArrayList<>();

		try {
			FastExcel.read(file.getInputStream(), CmdbAssetExcelRowDTO.class,
					new PageReadListener<CmdbAssetExcelRowDTO>(rows::addAll)).sheet("大機房").doRead();
		} catch (IOException e) {
			throw new RuntimeException("Excel 讀取失敗：" + e.getMessage(), e);
		}
//		List<ServerExcelRowDTO> rows = new ArrayList<>();
//
//		try {
//			FastExcel.read(file.getInputStream(), ServerExcelRowDTO.class,
//					new PageReadListener<ServerExcelRowDTO>(rows::addAll)).sheet("大機房").doRead();
//		} catch (IOException e) {
//			throw new RuntimeException("Excel 讀取失敗：" + e.getMessage(), e);
//		}
		System.out.println("Success");
	}
}
