package com.project.uhd.dto;

public class CaseCreateRequest {
	private String name;
	private String description;
	private String logic;
	private String conditions;
	private Boolean ruleEnabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public Boolean getRuleEnabled() {
		return ruleEnabled;
	}

	public void setRuleEnabled(Boolean ruleEnabled) {
		this.ruleEnabled = ruleEnabled;
	}

}
