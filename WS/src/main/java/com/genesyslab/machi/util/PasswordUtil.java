package com.genesyslab.machi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtil.class);

	/**
	 * Get MD5 encrypted password for plain text
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String getEncryptedPassword(String password) {
		if (StringUtils.isNotEmpty(password)) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

				StringBuilder sb = new StringBuilder();
				for (byte b : hashInBytes) {
					sb.append(String.format("%02x", b));
				}
				return sb.toString();
			} catch (NoSuchAlgorithmException ex) {
				LOGGER.error("getEncryptedPassword() | NoSuchAlgorithmException exception caught: ", ex);
			}
		}
		return null;
	}
}
