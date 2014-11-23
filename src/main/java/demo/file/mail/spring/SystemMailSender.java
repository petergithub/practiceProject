package demo.file.mail.spring;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Email Sender
 */
public class SystemMailSender {
	protected static final Logger logger = Logger
			.getLogger(SystemMailSender.class);
	private JavaMailSender sender;

	private String encode;

	public void send(final String address, final String from,
			final String subject, final String content) {
		Runnable thread = new Runnable() {
			public void run() {
				MimeMessage mimeMsg = sender.createMimeMessage();
				try {
					MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, false, encode);
					if (address.indexOf(";") != -1) {
						helper.setTo(address.split(";"));
					} else {
						helper.setTo(address);
					}
					helper.setFrom(from);
					helper.setSubject(subject);
					helper.setText(content, true);
				} catch (MessagingException e) {
					logger.debug(e);
				}
				sender.send(mimeMsg);
			}
		};
		new Thread(thread).start();
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public JavaMailSender getJavaMailSender() {
		return sender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.sender = javaMailSender;
	}
}
