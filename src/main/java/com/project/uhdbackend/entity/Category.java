package com.project.uhdbackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "muhd_category")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_gen")
	@SequenceGenerator(name = "category_id_gen", sequenceName = "category_id_seq", allocationSize = 1)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "category_parentid")
	private int parentId = -1;

	@Column(name = "category_code")
	private String code;
	
	@Column(name = "category_title")
	private String title;
	
	@Column(name = "category_content")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("parentId", parentId);
		jo.put("code", code);
		jo.put("title", title);
		jo.put("content", content);
		return jo.toString();
	}
}
