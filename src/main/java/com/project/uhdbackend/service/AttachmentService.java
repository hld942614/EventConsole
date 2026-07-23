package com.project.uhdbackend.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.uhdbackend.dto.AttachmentDTO;
import com.project.uhdbackend.dto.AttachmentInfoDTO;
import com.project.uhdbackend.dto.StorageResult;
import com.project.uhdbackend.entity.Attachment;
import com.project.uhdbackend.entity.Event;
import com.project.uhdbackend.repository.AttachmentRepository;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

@Service
public class AttachmentService {

	private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);
	private StorageService storageService;
	private AttachmentRepository attachmentRepository;

	public AttachmentService(StorageService storageService, AttachmentRepository attachmentRepository) {
		this.storageService = storageService;
		this.attachmentRepository = attachmentRepository;
	}

	public List<AttachmentInfoDTO> extractAttachments(MimeMessage mm) throws Exception {

		List<AttachmentInfoDTO> attachments = new ArrayList<>();

		Object content = mm.getContent();

		if (!(content instanceof Multipart)) {
			return attachments;
		}

		Multipart multipart = (Multipart) content;

		parseMultipart(multipart, attachments);

		return attachments;
	}

	@Transactional
	public void storeAttachments(Event event, List<AttachmentInfoDTO> infos) {

		if (infos == null || infos.isEmpty()) {
			return;
		}

		List<Attachment> attachments = new ArrayList<>();

		for (AttachmentInfoDTO info : infos) {
			String eventId = event.getEventId();
			StorageResult result;
			try {
				result = storageService.storeAttachment(eventId, info.getInputStream(), info.getFileName(),
						info.getContentType());
			} catch (Exception e) {
				log.warn("Store attachment failed. eventId={}, fileName={}", eventId, info.getFileName(), e);
				continue;
			}

			Attachment attachment = new Attachment();

			attachment.setEventId(eventId);
			attachment.setOriginalFileName(result.getOriginalFileName());
			attachment.setStoredFileName(result.getStoredFileName());
			attachment.setContentType(info.getContentType());
			attachment.setFileSize(info.getSize());
			attachment.setFilePath(result.getPath());

			attachments.add(attachment);
		}

		attachmentRepository.saveAll(attachments);
	}

	@Transactional
	public void deleteAttachments(String eventId) {

		List<Attachment> attachments = attachmentRepository.findByEventId(eventId);

		for (Attachment attachment : attachments) {
			try {
				storageService.delete(attachment.getFilePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		attachmentRepository.deleteAll(attachments);
	}

	@Transactional(readOnly = true)
	public List<AttachmentDTO> getAttachments(String eventId) {
		return attachmentRepository.findByEventId(eventId).stream().map(AttachmentDTO::new).toList();
	}

	@Transactional(readOnly = true)
	public Attachment getAttachment(Long attachmentId) {
		return attachmentRepository.findById(attachmentId)
				.orElseThrow(() -> new RuntimeException("Attachment not found : " + attachmentId));
	}

	@Transactional
	public void deleteAttachment(Long attachmentId) {
		Attachment attachment = getAttachment(attachmentId);
		try {
			storageService.delete(attachment.getFilePath());
		} catch (Exception e) {

		}

		attachmentRepository.delete(attachment);
	}

	public InputStream loadAttachment(String path) throws IOException {
		Path target = Paths.get(path);
		return Files.newInputStream(target);
	}

	private boolean isAttachment(BodyPart part) throws MessagingException {

		String disposition = part.getDisposition();

		// Outlook / Exchange
		if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
			return true;
		}

		if (part.getFileName() != null) {
			return true;
		}

		return false;
	}

	private void parseMultipart(Multipart multipart, List<AttachmentInfoDTO> attachments) throws Exception {

		for (int i = 0; i < multipart.getCount(); i++) {

			BodyPart part = multipart.getBodyPart(i);

			// ---------- 如果還是 Multipart，繼續往下找 ----------
			Object content = part.getContent();
			if (content instanceof Multipart) {
				parseMultipart((Multipart) content, attachments);
				continue;
			}

			// ---------- 判斷是不是附件 ----------
			if (!isAttachment(part)) {
				continue;
			}

			AttachmentInfoDTO attachment = new AttachmentInfoDTO();

			attachment.setFileName(MimeUtility.decodeText(part.getFileName()));
			attachment.setContentType(part.getContentType());
			attachment.setSize(part.getSize());
			attachment.setInputStream(part.getInputStream());

			attachments.add(attachment);
		}
	}
}
