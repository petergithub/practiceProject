JavaMail.html 
	action is JavaMail.jsp: the simple example.
	action is JavaMailAttachment.jsp: send a email with a attachment
JavaMailTemplate.html, Mail.properties
	action is JavaMailTemplate.jsp: use email template.
	
run mail step:
put Mail.properties under class path
cos.jar is from oreilly

classpath file is
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
	<classpathentry kind="con" path="melibrary.com.genuitec.eclipse.j2eedt.core.MYECLIPSE_JAVAEE_5_CONTAINER"/>
	<classpathentry kind="lib" path="lib/activation.jar"/>
	<classpathentry kind="lib" path="lib/mail.jar"/>
	<classpathentry kind="lib" path="lib/cos.jar"/>
	<classpathentry kind="output" path="WebRoot/WEB-INF/classes"/>
</classpath>
