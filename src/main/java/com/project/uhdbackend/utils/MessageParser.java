package com.project.uhdbackend.utils;

import org.springframework.stereotype.Component;

@Component
public class MessageParser {
//	public Message transferMailToMsg(String input) throws Exception {
//		Message emailMsg = new Message();
//		Properties props = System.getProperties();
//		Session session = Session.getDefaultInstance(props, null);
//		MimeMessage mm = new MimeMessage(session, new ByteArrayInputStream(input.getBytes()));
//		Multipart mp = (Multipart) mm.getContent();
//		int bodynum = mp.getCount();
//		JSONObject data = new JSONObject();
//
//		// Sender
//		List<String> senderList = new ArrayList<>();
//		for (Address address : mm.getFrom()) {
//			senderList.add(address.toString());
//		}
//		emailMsg.setSender(senderList.toString());
//
//		// 標題
//		emailMsg.setSubject(mm.getSubject());
//
//		for (int partCount = 0; partCount < bodynum; partCount++) {
//			MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(partCount);
//			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
//				// 附件（略）
//			} else {
//				Object content = part.getContent();
//				String contentType = part.getContentType();
//				if (content instanceof Multipart) {
//					Multipart mp2 = (Multipart) content;
//					for (int i = 0; i < mp2.getCount(); i++) {
//						MimeBodyPart part2 = (MimeBodyPart) mp2.getBodyPart(i);
//						String part2Content = part2.getContent().toString();
//						String part2ContentType = part2.getContentType();
//						if (part2ContentType.contains("plain") && isValidJson(part2Content)) {
//							data = new JSONObject(part2Content);
//						}
//					}
//				} else if (content instanceof String && contentType.contains("plain")
//						&& isValidJson(content.toString())) {
//					data = new JSONObject(content.toString());
//				}
//			}
//		}
//
//		// 內容
//		emailMsg.setData(data.toString());
//		// 告警類別
//		emailMsg.setAlertCode(data.has("alert_code") ? data.getString("alert_code") : "");
//		// alert發生時間
//		if (data.has("timestamp")) {
//			emailMsg.setAlertTimestamp(data.getString("timestamp"));
//		}
//		// email寄送時間
//		Date mailDate = mm.getSentDate();
//		String mailDateString = dateFormat(mailDate);
//		emailMsg.setEmailTimestamp(mailDateString);
//		// 寫入資料庫時間
//		Date dbDate = new Date(System.currentTimeMillis());
//		String dbDateString = dateFormat(dbDate);
//		emailMsg.setDbTimestamp(dbDateString);
//		return emailMsg;
//	}
//
//	public Message transferApiToMsg(String input) throws Exception {
//		Message apiMsg = new Message();
//		JSONObject jo = new JSONObject(input);
//		// 告警時間
//		if (jo.has("alert_timestamp") && !jo.isNull("alert_timestamp")) {
//			apiMsg.setAlertTimestamp(jo.getString("alert_timestamp"));
//		}
//		// 告警類別
//		if (jo.has("alert_code") && !jo.isNull("alert_code")) {
//			apiMsg.setAlertCode(jo.getString("alert_code"));
//		}
//		// 標題
//		if (jo.has("subject") && !jo.isNull("subject")) {
//			apiMsg.setSubject(jo.getString("subject"));
//		}
//		// 寄件者（原本就這樣寫，維持不動）
//		if (jo.has("sender") && !jo.isNull("sender")) {
//			apiMsg.setSourceIp(jo.getString("sender"));
//		}
//		// 寫入資料庫時間
//		Date dbDate = new Date(System.currentTimeMillis());
//		String dbDateString = dateFormat(dbDate);
//		apiMsg.setDbTimestamp(dbDateString);
//		// 訊息內容
//		apiMsg.setData(input);
//		return apiMsg;
//	}

//	private boolean isValidJson(String input) {
//		try {
//			new JSONObject(input);
//			return true;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	private String dateFormat(Date date) {
//		return date.toInstant().atZone(ZoneId.of("UTC"))
//				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
//	}
}
