package demo.file.mail.spring;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pu.utils.DateUtils;


public class MailServiceImpl {
	String emailOfSupporter;
	String mailSubject;
	String systemUrl;
	String templateFile;
	String address;
	String emailContent;
	private EmailContentGenerator emailContentGenerator;
	private SystemMailSender systemMailSender;

	public void sendOrder(List<UserBean> list) {
		emailContent = construtEmailContent();
		address = "";
		for (UserBean u : list) {
			address += (u.getEmail() + ";");
		}
		systemMailSender.send(address, emailOfSupporter, mailSubject,
				emailContent);
	}

	private String construtEmailContent() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentTime", DateUtils.dateToString(new Date()));
		map.put("systemUrl", systemUrl);
		return emailContentGenerator.generateEmailContent(map, templateFile);
	}

	public String getEmailOfSupporter() {
		return emailOfSupporter;
	}

	public void setEmailOfSupporter(String emailOfSupporter) {
		this.emailOfSupporter = emailOfSupporter;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getSystemUrl() {
		return systemUrl;
	}

	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public EmailContentGenerator getEmailContentGenerator() {
		return emailContentGenerator;
	}

	public void setEmailContentGenerator(
			EmailContentGenerator emailContentGenerator) {
		this.emailContentGenerator = emailContentGenerator;
	}

	public SystemMailSender getSystemMailSender() {
		return systemMailSender;
	}

	public void setSystemMailSender(SystemMailSender systemMailSender) {
		this.systemMailSender = systemMailSender;
	}

}

class UserBean {
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String password;
	private String email;
}
