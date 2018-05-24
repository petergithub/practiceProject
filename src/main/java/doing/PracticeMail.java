package doing;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Shang Pu
 * @version Date: Sep 4, 2016 9:58:13 PM
 * 
 * spring boot debug: spring.mail.properties.mail.debug=true
 *
 */
public class PracticeMail {
    private static final Logger log = LoggerFactory.getLogger(PracticeMail.class);
    private static final String TO = "to@example.com";
    private static final String FROM = "from@example.com";
    private static final String PASSWORD = "password";

	public static void main(String[] args) {
		String host = "smtp.163.com";
		String port = "25";
		String user = FROM;
		String pwd = PASSWORD;
		String from = FROM;
		String to = TO;

		String subject = "Test email Subject";
		String messageText = "Message";

		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		// props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pwd);
			}
		};
		// Get the default Session object.
		Session session = Session.getDefaultInstance(props, auth);

		try {
		    session.setDebug(true);// 输出发送邮件时的调试信息
		    
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from)); // Set From: header field of the header.
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(messageText);
			message.setSentDate(new Date());

			// Send message
			Transport.send(message);
			log.info("Sent message successfully....");
		} catch (MessagingException e) {
			log.error("Exception in PracticeMail.main()", e);
		}
	}

	@Test
	public void send() {
		MessageInfo info = new MessageInfo(FROM, "subject port 25", "msg");
		info.setTo(Arrays.asList(TO));
		EmailAccount ac = new EmailAccount(FROM, PASSWORD, "smtp.exmail.qq.com");
		sendBySpring(info, ac);
	}

	@Test
	public void sslSend() {
		MessageInfo info = new MessageInfo(FROM, "subject port 465 hwsmtp.exmail.qq.com", "msg");
		info.setTo(Arrays.asList(TO));
		EmailAccount ac = new EmailAccount(FROM, PASSWORD, "hwsmtp.exmail.qq.com");
		try {
			sendByJdkWithSSL(info, ac);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO under implement
	 */
	public void sendBySpringWithSSL(MessageInfo msg, EmailAccount emailAccount) {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", emailAccount.getPlace());
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.transport.protocol", "smtps");

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(msg.getFrom());// 设置发送者地址
		List<String> tos = msg.getTo();
		String to[] = new String[tos.size()];
		tos.toArray(to);
		simpleMailMessage.setTo(to);// 设置接受者地址，可多个
		simpleMailMessage.setSubject(msg.getSubject());
		simpleMailMessage.setSentDate(msg.getSendDate());
		simpleMailMessage.setText(msg.getMsg());
		simpleMailMessage.setFrom(emailAccount.getUsername());
		log.debug("simpleMailMessage.toString(): {}", simpleMailMessage.toString());
		JavaMailSenderImpl s = new JavaMailSenderImpl();
		s.setHost(emailAccount.getPlace());
		s.setPort(465);
		s.setUsername(emailAccount.getUsername());
		s.setPassword(emailAccount.getPassword());
		s.send(simpleMailMessage);
	}

	public void sendBySpring(MessageInfo msg, EmailAccount emailAccount) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(msg.getFrom());// 设置发送者地址
		List<String> tos = msg.getTo();
		String to[] = new String[tos.size()];
		tos.toArray(to);
		simpleMailMessage.setTo(to);// 设置接受者地址，可多个
		simpleMailMessage.setSubject(msg.getSubject());
		simpleMailMessage.setSentDate(msg.getSendDate());
		simpleMailMessage.setText(msg.getMsg());
		simpleMailMessage.setFrom(emailAccount.getUsername());
		log.debug("simpleMailMessage: {}", simpleMailMessage.toString());
		JavaMailSenderImpl s = new JavaMailSenderImpl();
		s.setHost(emailAccount.getPlace());
		// s.setPort(25);
		s.setUsername(emailAccount.getUsername());
		s.setPassword(emailAccount.getPassword());
		s.send(simpleMailMessage);
	}

	public boolean sendByJdkWithSSL(MessageInfo msg1, EmailAccount emailAccount)
			throws AddressException, MessagingException, IOException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", emailAccount.getPlace());
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");

		final String username = emailAccount.getUsername();
		final String password = emailAccount.getPassword();
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		Message msg = new MimeMessage(session);

		// 设置发件人和收件人
		msg.setFrom(new InternetAddress(emailAccount.getUsername()));
		List<String> tos = msg1.getTo();
		Address to[] = new InternetAddress[tos.size()];
		for (int i = 0; i < tos.size(); i++) {
			to[i] = new InternetAddress(tos.get(i));
		}
		// 多个收件人地址
		msg.setRecipients(Message.RecipientType.TO, to);
		msg.setSubject(msg1.getSubject()); // 标题
		msg.setText(msg1.getMsg());// 内容
		msg.setSentDate(new Date());
		Transport.send(msg);
		System.out.println("EmailUtil ssl协议邮件发送打印" + msg.toString());
		return true;
	}

	class MessageInfo {
		// 发件人地址"
		private String from;

		// 收件人地址
		private List<String> to;

		// 发送时间
		private Date sendDate;

		// 邮件主题
		private String subject;

		// 消息正文
		private String msg;

		public MessageInfo(String from, String subject, String msg) {
			super();
			this.from = from;
			this.subject = subject;
			this.msg = msg;
		}

		public MessageInfo() {
			super();
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public List<String> getTo() {
			return to;
		}

		public void setTo(List<String> to) {
			this.to = to;
		}

		public Date getSendDate() {
			return sendDate;
		}

		public void setSendDate(Date sendDate) {
			this.sendDate = sendDate;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	class EmailAccount {

		// 邮箱用户
		private String username;

		// 邮箱密码
		private String password;

		// 邮箱服务器
		private String place;

		public EmailAccount(String username, String password, String place) {
			super();
			this.username = username;
			this.password = password;
			this.place = place;
		}

		public EmailAccount() {
			super();
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}

	@Test
	public void sendBySpringSimple() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		String host = "smtp.163.com";
		String user = FROM;
		String password = PASSWORD;
		String to = TO;
		int port = 25;
		Properties props = System.getProperties();
		props.put("mail.debug", "true");

		sender.setHost(host);
		sender.setUsername(user);
		sender.setPassword(password);
		sender.setPort(port);
		SimpleMailMessage mail = new SimpleMailMessage(); // 只发送纯文本
		mail.setFrom(user);
		mail.setSubject("TITLE");// 主题
		mail.setText("email body");// 邮件内容
		mail.setTo(to);
		sender.send(mail);
		log.info("Sent message successfully....");
	}

	@Test
	public void sendByJdkSimple() {
		String host = "smtp.163.com";
		String port = "25";
		String user = "";
		String pwd = "";
		String from = "no-reply@tclalcatelstore.net";
		String to = "user@163.com";

		String subject = "Test email Subject";
		String messageText = "Message";

		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		// props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pwd);
			}
		};
		// Get the default Session object.
		Session session = Session.getDefaultInstance(props, auth);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from)); // Set From: header field of the header.
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(messageText);
			message.setSentDate(new Date());

			// Send message
			Transport.send(message);
			log.info("Sent message successfully....");
		} catch (MessagingException e) {
			log.error("Exception in PracticeMail.main()", e);
		}
	}
}
