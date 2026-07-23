package com.project.uhdbackend.service;

import org.springframework.stereotype.Service;

import com.project.uhdbackend.exception.InvalidEventPayloadException;

@Service
public class ModuleCodeResolver {

	private final CategoryService categoryService;

	public ModuleCodeResolver(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public String resolve(String alertCode) {
		String moduleCode = categoryService.getMainByAlertCode(alertCode).getCode();
		if (moduleCode == null || moduleCode.isBlank()) {
			throw new InvalidEventPayloadException("找不到 alertCode 對應的分類，無法判斷 moduleCode: " + alertCode);
		}
		return moduleCode;
	}
}