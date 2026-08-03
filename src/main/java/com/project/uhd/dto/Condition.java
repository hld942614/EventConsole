package com.project.uhd.dto;

import org.json.JSONObject;

public class Condition {
	public Long id;
	public String field;
	public String operator;
	public String value;
	
    public Condition() {
    }
	
	public Condition(Long id, String field, String operator, String value) {
		this.id = id;
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("field", field);
		jo.put("operator", operator);
		jo.put("value", value);
		return jo.toString();
	}
}
