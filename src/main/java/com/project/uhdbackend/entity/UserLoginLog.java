package com.project.uhdbackend.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "muhd_user_login_log")
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loginlog_id_gen")
	@SequenceGenerator(name = "loginlog_id_gen", sequenceName = "loginlog_id_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false, length = 50)
    private String userId;

    @Column(name = "ACTION_NAME", nullable = false, length = 50)
    private String actionName;

    @Column(name = "RTN_MSG", length = 1000)
    private String rtnMsg;
    
    @Column(name = "ERROR_MSG", length = 1000)
    private String errorMsg;

    @Column(name = "CREATE_TIME", insertable = false, updatable = false)
    private LocalDateTime createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getRtnMsg() {
		return rtnMsg;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
