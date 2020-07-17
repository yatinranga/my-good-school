package com.nxtlife.mgs.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.nxtlife.mgs.view.MailRequest;

public interface NotificationService {

	void sendEmail(MailRequest request)
			throws IOException, GeneralSecurityException, AddressException, MessagingException;

}
