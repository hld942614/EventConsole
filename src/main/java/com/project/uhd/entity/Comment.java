package com.project.uhd.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.project.uhd.util.CommentStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "muhd_comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_gen")
	@SequenceGenerator(name = "comment_seq_gen", sequenceName = "muhd_comment_seq", allocationSize = 1)
	@Column(name = "COMMENT_ID")
	private Long commentId;

	@Lob
	@Column(name = "COMMENT_CONTENT", nullable = false)
	private String commentContent;

	@Column(name = "COMMENT_AUTHOR", length = 100)
	private String commentAuthor;

	@Column(name = "COMMENT_TIMESTAMP")
	private LocalDateTime commentTimestamp;

	@Column(name = "UPDATED_AT")
	private OffsetDateTime updatedAt;

	@Column(name = "COMMENT_AUTHOR_ID", length = 100)
	private String commentAuthorId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CASE_ID")
	private Case caze;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_PK")
	private Event event;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 30)
	private CommentStatus status;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentAuthor() {
		return commentAuthor;
	}

	public void setCommentAuthor(String commentAuthor) {
		this.commentAuthor = commentAuthor;
	}

	public LocalDateTime getCommentTimestamp() {
		return commentTimestamp;
	}

	public void setCommentTimestamp(LocalDateTime commentTimestamp) {
		this.commentTimestamp = commentTimestamp;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}

	public Case getCaze() {
		return caze;
	}

	public void setCaze(Case caze) {
		this.caze = caze;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public String getCommentAuthorId() {
		return commentAuthorId;
	}

	public void setCommentAuthorId(String commentAuthorId) {
		this.commentAuthorId = commentAuthorId;
	}
}
