<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.mail.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.activation.*" %>
<%@ page import="org.pu.MultipartRequest" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>JavaMailAttachment.jsp</title>
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
    	MultipartRequest multi = new MultipartRequest(request, ".", 5*1024*1024,"gb2312");
    	
    	String mailserver = "10.10.10.6";
    	//String mailserver = "pop.gmail.com";
    	String from = multi.getParameter("From");
    	String to = multi.getParameter("To");
    	String subject = multi.getParameter("Subject");
    	String type = multi.getParameter("Type");
    	String messageText = multi.getParameter("Message");
    	String fileName = multi.getFilesystemName("FileName");
    	
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
//	    		Multipart mp = new MimeMultipart();
//  	  		MimeBodyPart mbp = new MimeBodyPart();
//	    		mbp.setContent(messageText, type + ";charset=GB2312");
//	    		mp.addBodyPart(mbp);
//	    		msg.setContent(mp);
	// add for text/plain or text/html 	end  
    		// set the MIME Type of Email
    		//msg.setText(messageText);
    		
    		if (fileName != null) {
    			File file = new File(fileName);
    			
    			// if attachment exists, save it firstly
    			MimeBodyPart mbp1 = new MimeBodyPart();
    			
    			// set the type of content: text/plain or text/html
    			mbp1.setContent(messageText, type + ";charset=gb2312");
    			
    			// handle the attachment
    			MimeBodyPart mbp2 = new MimeBodyPart();
    			FileDataSource fds = new FileDataSource(fileName);
    			mbp2.setDataHandler(new DataHandler(fds));
    			mbp2.setFileName(MimeUtility.encodeText(fds.getName(), "gb2312", "B"));
    			
    			// make them together
    			Multipart mp = new MimeMultipart();
    			mp.addBodyPart(mbp1);
    			mp.addBodyPart(mbp2);
    			msg.setContent(mp);
    		} else {
    			// if there isn't a attachment, save it content only
    			msg.setContent(messageText, type + ";charset=gb2312");
    		}	
    		
    		// send 
    		Transport.send(msg);
    		
    		out.println("the Email send successfully");
    	} catch (MessagingException mex) {
    		mex.printStackTrace();
    	}
    %>
  </body>
</html>
