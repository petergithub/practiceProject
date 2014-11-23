<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.mail.*" %>
<%@ page import="java.text.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>JavaMailTemplate.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
    <h2>Send Email with JavaMail</h2>
    <%
    	InternetAddress[] address = null;
    	request.setCharacterEncoding("gb2312");
    	
    	String mailserver = "10.10.10.6";
    	String from = "pu.shang@doublebridge.com.cn";
    	String to = request.getParameter("To");
    	String subject = "Welcome to Google";
    	String name = request.getParameter("Name");
    	String password = request.getParameter("Password");
    	
    	Object[] args = {name, password};
    	// get Mail.properties
    	ResourceBundle messages = ResourceBundle.getBundle("Mail");
    	MessageFormat formatter = new MessageFormat("");
    	formatter.applyPattern(messages.getString("message"));
    	String messageText = formatter.format(args);
    	
    	boolean sessionDebug = false;
    	
    	try {
    		// set the protocal which the server used
    		Properties props = System.getProperties();
    		props.put("mail.host", mailserver);
    		props.put("mail.transport.protocol", "pop3");
    		
    		// create a new session service
    		Session mailSession = Session.getDefaultInstance(props, null);
    		mailSession.setDebug(sessionDebug);
    		
    		Message msg = new MimeMessage(mailSession);
    		
    		// set the sender
    		msg.setFrom(new InternetAddress(from));
    		
    		// set to email
    		address = InternetAddress.parse(to, false);
    		msg.setRecipients(Message.RecipientType.TO, address);
    		
    		//set the subject
    		msg.setSubject(subject);
    		
    		// set the sending time
    		msg.setSentDate(new Date());

    // add for text/html 	begin
	    		Multipart mp = new MimeMultipart();
	  	  		MimeBodyPart mbp = new MimeBodyPart();
	    		mbp.setContent(messageText, "text/html" + ";charset=GB2312");
	    		mp.addBodyPart(mbp);
	    		msg.setContent(mp);
	// add for text/html 	end  
    		// set the MIME Type of Email
    		//msg.setText(messageText);
    		
    		// send 
    		Transport.send(msg);
    		
    		out.println("the Email send successfully");
    	} catch (MessagingException mex) {
    		mex.printStackTrace();
    	}
    %>
  </body>
</html>
