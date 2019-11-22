package com.genesyslab.machi.service.communication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.helper.MachiConstants;

@Service
public class EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Value("${mail.fromId}")
	private String senderMailId;
	@Value("${mail.test.toId}")
	private String testToId;
	@Value("${mail.test.ccId}")
	private String testCcId;
	@Value("${mail.signature}")
	private String signature;

	@Value("${test.env}")
	private boolean testEnvironment;

	@Autowired
	JavaMailSender sender;

	public void sendMail(String subject, String body) {
		sendMail(subject, body, senderMailId, testToId, testCcId);
	}

	public void sendMail(String subject, String body, String toId) {
		sendMail(subject, body, toId, null);
	}

	public void sendMail(String subject, String body, String toId, String ccId) {
		sendMail(subject, body, senderMailId, toId, ccId);
	}

	public void sendMail(String subject, String body, String fromId, String toId, String ccId) {
		sendMail(subject, body, fromId, toId, ccId, null);
	}

	public void sendMail(String subject, String body, String fromId, String toId, String ccId,
			String attachmentFilePath) {
		LOGGER.info("sendMail() | fromId: {}, toId: {}, ccId: {}, subject: {}, attachmentFilePath: {}", fromId, toId,
				ccId, subject, attachmentFilePath);
		LOGGER.debug("sendMail() | body: {}", body);
		if (StringUtils.isEmpty(subject) && StringUtils.isEmpty(body)) {
			return;
		}
		try {
			fromId = StringUtils.isEmpty(fromId) ? senderMailId : fromId;
			toId = getToId(toId);

			MimeMessage message = sender.createMimeMessage();
			message.setFrom(fromId);
			message.setSubject(subject);
			message.addRecipients(RecipientType.TO, toId);
			if (StringUtils.isNotEmpty(ccId)) {
				message.addRecipients(RecipientType.CC, getCcId(ccId));
			}
			if (StringUtils.isNotEmpty(attachmentFilePath)) {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				byte[] content = Files.readAllBytes(Paths.get(attachmentFilePath));
				helper.addAttachment(getFileNameFromAttachment(attachmentFilePath), new ByteArrayResource(content));
				helper.setText(getBodyWithSignature(body));
			} else {
				message.setText(getBodyWithSignature(body));
			}
			sender.send(message);
		} catch (MessagingException | IOException ex) {
			LOGGER.error("sendMail() | Exception caught: ", ex);
		}
	}

	private String getFileNameFromAttachment(String attachmentFilePath) {
		if (attachmentFilePath.contains("/")) {
			return attachmentFilePath.substring(attachmentFilePath.lastIndexOf("/"));
		}
		return attachmentFilePath.substring(attachmentFilePath.lastIndexOf("\\"));
	}

	private String getBodyWithSignature(String body) {
		if (StringUtils.isEmpty(body)) {
			body = "";
		}
		return body + MachiConstants.NEW_LINE + MachiConstants.NEW_LINE + signature;
	}

	private String getToId(String toId) {
		if (testEnvironment || StringUtils.isEmpty(toId)) {
			return testToId;
		}
		return toId;
	}

	private String getCcId(String ccId) {
		if (testEnvironment) {
			return testCcId;
		}
		return ccId;
	}
}