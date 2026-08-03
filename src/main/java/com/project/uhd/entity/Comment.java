package com.project.uhd.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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

	@ManyToMany(mappedBy = "comments")
	private Set<Case> cases = new HashSet<>();

	@ManyToMany(mappedBy = "comments")
	private Set<Event> events = new HashSet<>();

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

	public void setStatus(CommentStatus status) {
		this.status = status;
	}

	public Set<Case> getCases() {
		return cases;
	}

	public void setCases(Set<Case> cases) {
		this.cases = cases;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public CommentStatus getStatus() {
		return status;
	}
}
