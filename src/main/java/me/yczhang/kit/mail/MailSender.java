package me.yczhang.kit.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by YCZhang on 15/3/30.
 */

public class MailSender {

	private class MailAuthenticator extends Authenticator {
		private String username;
		private String password;

		public MailAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return this.username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	private Properties properties;
	private MailAuthenticator authenticator;
	private Session session;
	private InternetAddress from;

	public MailSender(MailSenderConfig config) throws AddressException {
		this.properties = new Properties();
		this.properties.setProperty("mail.smtp.host", config.smtp);
		this.properties.setProperty("mail.smtp.auth", "true");

		this.authenticator = new MailAuthenticator(config.user, config.pass);

		this.session = Session.getInstance(this.properties, this.authenticator);

		this.from = new InternetAddress(config.user);
	}

	public MailSender(String smtp, String username, String password) throws AddressException {
		this(new MailSenderConfig().smtp(smtp).user(username).pass(password));
	}

	public void send(MailMessage message) throws MessagingException, UnsupportedEncodingException {
		MimeMessage mimeMessage = new MimeMessage(session);

		if (message.from!=null)
			mimeMessage.setFrom(message.from);
		else
			mimeMessage.setFrom(this.from);

		if (message.to!=null)
			mimeMessage.setRecipients(Message.RecipientType.TO, message.to.toArray(new InternetAddress[0]));

		if (message.cc!=null)
			mimeMessage.setRecipients(Message.RecipientType.CC, message.cc.toArray(new InternetAddress[0]));

		if (message.bcc!=null)
			mimeMessage.setRecipients(Message.RecipientType.BCC, message.bcc.toArray(new InternetAddress[0]));

		if (message.subject!=null)
			mimeMessage.setSubject(message.subject, "utf-8");

		Multipart multipart = new MimeMultipart();

		if (message.content!=null) {
			BodyPart part = new MimeBodyPart();
			part.setContent(message.content.toString(), "text/html; charset=utf-8");
			multipart.addBodyPart(part);
		}

		if (message.attachments != null) {
			Iterator<String> cit = message.contentIds.iterator();
			Iterator<File> ait = message.attachments.iterator();
			while (cit.hasNext() && ait.hasNext()) {
				String contentId = cit.next();
				File attachment = ait.next();

				MimeBodyPart part = new MimeBodyPart();
				DataSource dataSource = new FileDataSource(attachment);
				DataHandler dataHandler = new DataHandler(dataSource);
				part.setDataHandler(dataHandler);
				part.setContentID(contentId);
				part.setFileName(MimeUtility.encodeText(dataSource.getName()));
				multipart.addBodyPart(part);
			}
		}

		mimeMessage.setContent(multipart);
		mimeMessage.saveChanges();

		Transport.send(mimeMessage);
	}

}
