package doing;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Sep 4, 2016 9:58:13 PM
 */

public class PracticeMail {
	private static final Logger log = LoggerFactory.getLogger(PracticeMail.class);

	public static void main(String[] args) {

		// Sending email from host
		String host = "smtp.163.com";
		String port = "25";
		// Recipient's email ID needs to be mentioned.
		String user = "user@163.com";
		String pwd = "password";
		String to = user;
		String subject = "Test email Subject";
		String messageText = "Message";

		// Sender's email ID needs to be mentioned
		String from = user;

		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new Authenticator() {
			 public PasswordAuthentication getPasswordAuthentication() {
		           return new PasswordAuthentication(user, pwd);
		        }
		};
		// Get the default Session object.
		Session session = Session.getDefaultInstance(props, auth);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(messageText);
			// set the sending time
			message.setSentDate(new Date());

			// Send message
			Transport.send(message);
			log.info("Sent message successfully....");
		} catch (MessagingException e) {
			log.error("Exception in PracticeMail.main()", e);
		}
	}

}
