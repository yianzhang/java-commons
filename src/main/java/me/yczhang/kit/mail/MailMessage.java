package me.yczhang.kit.mail;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YCZhang on 8/26/15.
 */
public class MailMessage {

	protected InternetAddress from = null;
	protected List<InternetAddress> to = new LinkedList<InternetAddress>();
	protected List<InternetAddress> cc = new LinkedList<InternetAddress>();
	protected List<InternetAddress> bcc = new LinkedList<InternetAddress>();
	protected String subject = null;
	protected StringBuilder content = new StringBuilder();
	protected List<String> contentIds = new LinkedList<String>();
	protected List<File> attachments = new LinkedList<File>();

	public MailMessage setFrom(String from) throws AddressException {
		this.from = new InternetAddress(from);
		return this;
	}

	public MailMessage setTo(List<String> to) throws AddressException {
		this.to.clear();
		addTo(to);
		return this;
	}

	public MailMessage addTo(List<String> to) throws AddressException {
		if (to != null) {
			for (String s : to)
				addTo(s);
		}
		return this;
	}

	public MailMessage addTo(String to) throws AddressException {
		if (to != null)
			this.to.add(new InternetAddress(to));
		return this;
	}

	public MailMessage setCC(List<String> cc) throws AddressException {
		this.cc.clear();
		addCC(cc);
		return this;
	}

	public MailMessage addCC(List<String> cc) throws AddressException {
		if (cc != null) {
			for (String s : cc)
				addCC(s);
		}
		return this;
	}

	public MailMessage addCC(String cc) throws AddressException {
		if (cc != null)
			this.cc.add(new InternetAddress(cc));
		return this;
	}

	public MailMessage setBCC(List<String> bcc) throws AddressException {
		this.bcc.clear();
		addBCC(bcc);
		return this;
	}

	public MailMessage addBCC(List<String> bcc) throws AddressException {
		if (bcc != null) {
			for (String s : bcc)
				addBCC(s);
		}
		return this;
	}

	public MailMessage addBCC(String bcc) throws AddressException {
		if (bcc != null)
			this.bcc.add(new InternetAddress(bcc));
		return this;
	}

	public MailMessage addAttachment(String contentId, File attachment) {
		if (contentId != null && attachment != null && attachment.exists() && attachment.isFile()) {
			this.contentIds.add(contentId);
			this.attachments.add(attachment);
		}
		return this;
	}

	public String addAttachment(File attachment) {
		if (attachment != null && attachment.exists() && attachment.isFile()) {
			String contentId = String.valueOf(System.nanoTime());
			this.contentIds.add(contentId);
			this.attachments.add(attachment);
			return contentId;
		}
		else {
			return null;
		}
	}

	public MailMessage setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public MailMessage setContent(String html) {
		this.content.setLength(0);
		addContent(html);
		return this;
	}

	public MailMessage addContent(String html) {
		if (html != null)
			this.content.append(html);
		return this;
	}

	public MailMessage addContent(Object html) {
		if (html != null) {
			if (html instanceof String)
				addContent((String) html);
			else
				addContent(html.toString());
		}
		return this;
	}

	public void send(MailSender sender) throws MessagingException, UnsupportedEncodingException {
		sender.send(this);
	}
}
