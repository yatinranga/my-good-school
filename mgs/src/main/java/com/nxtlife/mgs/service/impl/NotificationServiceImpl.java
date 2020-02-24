package com.nxtlife.mgs.service.impl;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
//import javax.mail.Message;
import com.google.api.services.gmail.model.Message;
import com.nxtlife.mgs.service.NotificationService;
import com.nxtlife.mgs.view.MailRequest;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final String APPLICATION_NAME = "msil-mobile-application-31872";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tmp/tokens";
	private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
        InputStream in = NotificationService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9000).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
	
	@Async
	@Override
	public void sendEmail(MailRequest request)
			throws AddressException, MessagingException, IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();

		MimeMessage email;
		if (request.getFilePaths() != null && !request.getFilePaths().isEmpty()) {
			List<File> files = new ArrayList<File>();
			for(String filePath : request.getFilePaths()) {
				files.add(new File(filePath));
			}
			email = createEmailWithAttachment(request.getTo(),request.getFrom(),request.getSubject(),request.getContent(),files );
		}
		 email = createEmail(request.getTo(),request.getFrom(),request.getSubject(),request.getContent());
		 email.setSentDate(new Date());
		 email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress("laxminath@nxtlifetechnologies.com"));
		sendMessage(service,request.getFrom(),email);
	}

		// **********************************************************


	public static MimeMessage createEmail(String to, String from, String subject, String bodyText)
			throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setContent(bodyText, "text/html");
		return email;
	}

	public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	public static MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText,
			List<File> files) throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(bodyText, "text/plain");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		mimeBodyPart = new MimeBodyPart();
		for(File file : files) {
			DataSource source = new FileDataSource(file);
			mimeBodyPart.setDataHandler(new DataHandler(source));
			mimeBodyPart.setFileName(file.getName());
		}
		
		multipart.addBodyPart(mimeBodyPart);
		email.setContent(multipart);

		return email;
	}

	public static Message sendMessage(Gmail service, String userId, MimeMessage emailContent)
			throws MessagingException, IOException {
		Message message = createMessageWithEmail(emailContent);
		message = service.users().messages().send(userId, message).execute();

		System.out.println("Message id: " + message.getId());
		System.out.println(message.toPrettyString());
		return message;
	}
}

