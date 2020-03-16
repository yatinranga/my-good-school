package com.nxtlife.mgs.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.nxtlife.mgs.view.Mail;

public interface MailService {
	
    public void sendEmail(Mail mail) throws UnsupportedEncodingException, MessagingException;
}