package com.genesyslab.machi.service.communication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.genesyslab.machi.helper.MachiTestConstants;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

	@MockBean
	JavaMailSender mockJavaMailSender;
	@MockBean
	MimeMessage mockMimeMessage;
	@MockBean
	MimeMessageHelper mockMimeMessageHelper;

	@InjectMocks
	EmailService emailService;

	@BeforeEach
	void setUp() throws Exception {
		when(mockJavaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
	}

	@Test
	void testSendMail() {
		emailService.sendMail(MachiTestConstants.TEST_SUBJECT, MachiTestConstants.TEST_BODY,
				MachiTestConstants.TEST_FROM_ID, MachiTestConstants.TEST_TO_ID, MachiTestConstants.TEST_CC_ID, null);

		verify(mockJavaMailSender).send(mockMimeMessage);
	}
}