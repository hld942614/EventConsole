//package com.project.uhdbackend.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.json.JSONObject;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@Table(name = "muhd_group_condition")
//@NoArgsConstructor
//@AllArgsConstructor
//public class ConditionEntity {
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "condition_id")
//    private Long conditionId;
//	@Column(name = "group_id")
//    private Long groupId;
//	@Column(name = "field")
//    private String field;
//	@Column(name = "operator")
//    private String operator;
//	@Column(name = "value")
//    private String value;
//	@Column(name = "match")
//    private String match;
//    
//	public Long getConditionId() {
//		return conditionId;
//	}
//	
//	public void setConditionId(Long conditionId) {
//		this.conditionId = conditionId;
//	}
//	
//	public Long getGroupId() {
//		return groupId;
//	}
//	
//	public void setGroupId(Long groupId) {
//		this.groupId = groupId;
//	}
//	
//	public String getField() {
//		return field;
//	}
//	
//	public void setField(String field) {
//		this.field = field;
//	}
//	
//	public String getOperator() {
//		return operator;
//	}
//	
//	public void setOperator(String operator) {
//		this.operator = operator;
//	}
//	
//	public String getValue() {
//		return value;
//	}
//	
//	public void setValue(String value) {
//		this.value = value;
//	}
//	
//	public String getMatch() {
//		return match;
//	}
//	
//	public void setMatch(String match) {
//		this.match = match;
//	}
//
//	@Override
//	public String toString() {
//		JSONObject jo = new JSONObject();
//		jo.put("conditionId", conditionId);
//		jo.put("groupId", groupId);
//		jo.put("field", field);
//		jo.put("operator", operator);
//		jo.put("value", value);
//		jo.put("match", match);
//		return jo.toString();
//	}
//}
