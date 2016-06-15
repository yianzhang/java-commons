package me.yczhang.kit.mail;

import java.util.Map;

/**
 * Created by YCZhang on 11/4/15.
 */
public class MailSenderConfig {

	protected String smtp;
	protected String user;
	protected String pass;

	public MailSenderConfig() {

	}

	public MailSenderConfig smtp(String smtp) {
		this.smtp = smtp;
		return this;
	}

	public MailSenderConfig user(String user) {
		this.user = user;
		return this;
	}

	public MailSenderConfig pass(String pass) {
		this.pass = pass;
		return this;
	}

	public static MailSenderConfig parseMap(Map<String, String> props) {
		return MailSenderConfig.parseMap(props, "");
	}

	public static MailSenderConfig parseMap(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (prefix.length() > 0)
			prefix = prefix + ".";

		MailSenderConfig config = new MailSenderConfig();

		String tmp;

		tmp = props.get(prefix + "SMTP");
		if (tmp != null) config.smtp(tmp);

		tmp = props.get(prefix + "User");
		if (tmp != null) config.user(tmp);

		tmp = props.get(prefix + "Pass");
		if (tmp != null) config.pass(tmp);

		return config;
	}
}