package com.nxtlife.mgs.service.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.MailService;
import com.nxtlife.mgs.view.Mail;

@Service
public class MailServiceImpl extends BaseService implements MailService {

	@Autowired
	JavaMailSender mailSender;

	public void sendEmail(Mail mail) throws UnsupportedEncodingException, MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "my-good-school.com"));
			mimeMessageHelper.setTo(mail.getMailTo());
//			mimeMessageHelper.setText(mail.getMailContent());
			mimeMessageHelper.setText(mail.getMailContent(), mail.getHtml());
//			mimeMessage.setFileName("");

			mailSender.send(mimeMessageHelper.getMimeMessage());

	}

}
