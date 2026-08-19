package com.project.uhd.dto;

import java.util.Map;

public class EventCategoryStatsDTO {

	private String code;

	private Long id;

	private String title;

	private String content;

	private Map<String, Integer> count;

	public EventCategoryStatsDTO() {
	}

	public EventCategoryStatsDTO(String code, Long id, String title, String content,
			Map<String, Integer> count) {
		this.code = code;
		this.id = id;
		this.title = title;
		this.content = content;
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, Integer> getCount() {
		return count;
	}

	public void setCount(Map<String, Integer> count) {
		this.count = count;
	}
}
