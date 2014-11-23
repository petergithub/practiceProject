<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.mail.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>JavaMail.jsp</title>
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
    	request.setCharacterEncoding("utf-8");
    	
    	String mailserver = "10.10.10.6";
    	//String mailserver = "pop.gmail.com";
    	String from = request.getParameter("From");
    	String to = request.getParameter("To");
    	String subject = request.getParameter("Subject");
    	String type = request.getParameter("Type");
    	String messageText = request.getParameter("Message");
    	
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
    // add for text/plain or text/html 	begin
	    		Multipart mp = new MimeMultipart();
	    		MimeBodyPart mbp = new MimeBodyPart();
	    		mbp.setContent(messageText, type + ";charset=GB2312");
	    		mp.addBodyPart(mbp);
	    		msg.setContent(mp);
	// add for text/plain or text/html 	end  
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
