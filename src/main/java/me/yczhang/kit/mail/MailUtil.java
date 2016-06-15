package me.yczhang.kit.mail;

/**
 * Created by YCZhang on 8/26/15.
 */
public class MailUtil {

	/**
	 * 添加一个Section
	 * @param message 邮件
	 * @param title 标题
	 * @param content 内容
	 */
	public static void addSection(MailMessage message, String title, String content) {
		message.addContent("<h2>").addContent(title).addContent("</h2>")
				.addContent("<p><pre>").addContent(content).addContent("</pre></p>");
	}
}
