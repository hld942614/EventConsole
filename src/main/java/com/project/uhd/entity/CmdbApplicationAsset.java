package com.project.uhd.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.uhd.enums.RelationType;

/** Application 與 Asset 的多對多關聯。目前 Excel 匯入流程不涉及，先建好骨架。 */
@Entity
@Table(name = "CMDB_APPLICATION_ASSET")
public class CmdbApplicationAsset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "APPLICATION_ID", nullable = false, length = 80)
	private String applicationId;

	@Column(name = "ASSET_ID", nullable = false, length = 80)
	private String assetId;

	@Enumerated(EnumType.STRING)
	@Column(name = "RELATION_TYPE", nullable = false, length = 50)
	private RelationType relationType;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false, updatable = false)
	private OffsetDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
}
