package demo.file.mail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.pu.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import com.sun.mail.pop3.POP3Store;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * To test sending email, I'd like to keep things fairly simple and send an
 * email to an email server on my local development machine. So, the first step
 * is to acquire an email server for Windows XP. A quick Google search led me to
 * download the free Standard Edition of MailEnable from
 * http://www.mailenable.com/. I'm not a Windows administrator, but MailEnable
 * was extremely easy for me to set up and use, which is definitely a plus.
 * <p>
 * refer to <a href=
 * 'http://www.avajava.com/tutorials/lessons/how-do-i-send-an-email-in-java.html'>How
 * do I send an email in Java?</a> and <a href=
 * 'http://www.avajava.com/tutorials/lessons/how-do-i-receive-email-in-java.html'>How
 * do I receive email in Java?</a>
 */
public class EmailTest {
	private final static Logger log = LoggerFactory.getLogger(EmailTest.class);
	
	public void parseMsgFile() {
		com.auxilii.msgparser.MsgParser msgp = new com.auxilii.msgparser.MsgParser();
		com.auxilii.msgparser.Message msg = null;
		try {
			String msgFile = "mail.msg";
			msg = msgp.parseMsg(msgFile);

			// The resulting msg object contains all necessary information
			// (e.g., from, to, subject).
			String fromEmail = msg.getFromEmail();
			String fromName = msg.getFromName();
			String subject = msg.getSubject();
			log.info("fromEmail=" + fromEmail + "fromName=" + fromName
					+ "subject=" + subject);

			// Attachments are stored in Attachment objects.
			List<com.auxilii.msgparser.attachment.Attachment> atts = msg
					.getAttachments();
			for (com.auxilii.msgparser.attachment.Attachment att : atts) {
				// do something with attachment
				log.info(att.toString());
			}
		} catch (UnsupportedOperationException e) {
			log.error("UnsupportedOperationException in testMsgPaser()", e);
		} catch (IOException e) {
			log.error("IOException in testMsgPaser()", e);
		}
	}

	public void testSendMail() {
		String smtpHost = "10.10.10.6";
		String userToAddr = "pu.shang@doublebridge.com.cn";
		String userCcAddr = "pu.shang@doublebridge.com.cn";
		String userFromAddr = "me@here.there.everywhere";
		String subject = "Email from Java";
		String message = "This is an email from Java";

		sendMail(userToAddr, userCcAddr, userFromAddr, subject, message, smtpHost);
	}

	@org.junit.Test
	public void testReceiveMail() {
		String mailPop3Host = "10.10.10.6";
		String mailStoreType = "pop3";
		String mailUser = "pu.shang@doublebridge.com.cn";
		String mailPassword = "p";

		receiveMail(mailPop3Host, mailStoreType, mailUser, mailPassword);
	}

	public static void sendMail(String userToAddr, String userCcAddr, String userFromAddr,
			String subject, String message, String smtpHost) {
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtpHost);
			Session mailSession = Session.getDefaultInstance(properties);

			Address addressFrom = new InternetAddress(userFromAddr);	
			Address addressTo = new InternetAddress(userToAddr);
			Address addressCc = new InternetAddress(userCcAddr);
			
			Message mimeMsg = new MimeMessage(mailSession);
			mimeMsg.addRecipient(Message.RecipientType.TO, addressTo);
			mimeMsg.addRecipient(Message.RecipientType.CC, addressCc);
			mimeMsg.setFrom(addressFrom);
			mimeMsg.setSubject(subject);
			mimeMsg.setText(message);
			
			MimeBodyPart mimeBP = new MimeBodyPart();
			mimeBP.setContent(message, "text/html");
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mimeBP);
			mimeMsg.setContent(mp);
			
			Transport.send(mimeMsg);
		} catch (AddressException e) {
			log.error("Exception in EmailTest.sendMail()", e);
		} catch (MessagingException e) {
			log.error("Exception in EmailTest.sendMail()", e);
		}
	}

	/**
	 * 使用Spring封装的JavaMailSenderImpl
	 */
	public void sendMailWithSpring() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("smtp.163.com");
		sender.setPassword("passowrd");
		sender.setUsername("username");
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		sender.setJavaMailProperties(prop);
		MimeMessage mimeMsg = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
			String address = "aaa@163.com";
			if (address.indexOf(";") != -1) {
				helper.setTo(address.split(";"));
			} else {
				helper.setTo(address);
			}
			
			helper.setFrom(address);
			helper.setSubject("邮件测试");
			helper.setText(buildMailContent(), true);
			helper.addInline("a", new File("E:/xiezi.jpg"));
			helper.addInline("b", new File("E:/logo.png"));
			File file = new File("E:/Favorites中文文件.rar");
			helper.addAttachment(MimeUtility.encodeWord(file.getName()),
					file);
		} catch (UnsupportedEncodingException e) {
			log.error("Exception in EmailTest.sendMailWithSpring()", e);
		} catch (MessagingException e) {
			log.error("Exception in EmailTest.sendMailWithSpring()", e);
		}
		sender.send(mimeMsg);
	}
	
	/**
	 * build mail content
	 */
	private String buildMailContent() {
		String content = "<html><head><META http- equiv=Content-Type content='text/html; charset=GBK'></HEAD>< title>test</title></head><body>dear 小燕子 \n "
		 + " < red > This is Text! </ red >  pic  < img   src = 'cid:a' > </ img > < br > hello < img   src = 'cid:b' > </ img > </ body > </ html > ";
		
		String systemUrl = "";
		String templateFile = "templateFilePath";
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("currentTime", DateUtils.dateToString(new Date()));
		propMap.put("systemUrl", systemUrl);
		String htmlFileName = "htmlPreview.html";
		preview(propMap, htmlFileName , templateFile);
		content = generateMailContent(propMap, templateFile);
		return content;
	}
	
	/**
	 * mail content with Free Marker template
	 */
	private String generateMailContent(Map<String, String> propMap, String templateFile) {
		String content = "";
		try {
			Configuration fmCfg = getFreemarkerCfg(templateFile);
			Template template = fmCfg.getTemplate(templateFile);
			content = FreeMarkerTemplateUtils.processTemplateIntoString(template,
					propMap);
		} catch (TemplateException e) {
			log.error("Error while processing FreeMarker template ", e);
		} catch (IOException e) {
			log.error("Error while generate Email Content ", e);
		}
		return content;
	}

	/**
	 * get the configuration file of free marker.
	 */
	private Configuration getFreemarkerCfg(String templateDir) {
		File file = new File(templateDir);
		if (file.isFile()) templateDir = file.getParent();
		Configuration freemarkerCfg = new Configuration();
		freemarkerCfg.setClassForTemplateLoading(getClass(),
				templateDir);
		return freemarkerCfg;
	}

	private String preview(Map<String, String> propMap, String htmlFileName,
			String templateFile) {
		if (geneHtmlFile(templateFile, propMap, htmlFileName)) {
			return htmlFileName;
		} else {
			return null;
		}
	}

	private boolean geneHtmlFile(String templateName, Map<String, String> propMap,
			String htmlName) {
		try {
			Template template = getFreemarkerCfg(templateName).getTemplate(templateName);
			File file = new File(htmlName);
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			template.process(propMap, out);
		} catch (TemplateException e) {
			log.error("Error while processing FreeMarker template "
					+ templateName, e);
			return false;
		} catch (IOException e) {
			log.error("Error while generate Static Html File " + htmlName, e);
			return false;
		}
		return true;
	}

	public static void receiveMail(String pop3Host, String storeType,
			String user, String password) {
		try {
			Properties properties = new Properties();
			properties.put("mail.pop3.host", pop3Host);
			Session emailSession = Session.getDefaultInstance(properties);

			POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
			emailStore.connect(user, password);

			Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				System.out.println("==============================");
				System.out.println("Email #" + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
			}

			emailFolder.close(false);
			emailStore.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}