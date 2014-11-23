 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  <%@page import="java.util.*"%>
  <%@page import="java.net.InetAddress;"%>
 <html>
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <title>Cluster App Test</title>
 </head>
 <body>
  <%
 InetAddress ip = InetAddress.getLocalHost();
 //out.println(ip.getHostAddress());
 %>
 This is responsed by <font color="red"> <%=ip.getHostAddress() %></font><br>
 Host Name : <font color="red"><%=ip.getHostName() %></font><br>
 Time : <font color="red"><%=new Date() %></font><br>
  <%
 ip = null;
 %>
<br/> <br/> <br/> <br/> 
Server Info:
<%
out.println(request.getLocalAddr() + " : " + request.getLocalPort()+"<br>");%>
<%
  out.println("<br>Session ID " + session.getId()+"<br>");
  // 如果有新的 Session 属性设置
  String dataName = request.getParameter("dataName");
  if (dataName != null && dataName.length() > 0) {
     String dataValue = request.getParameter("dataValue");
     session.setAttribute(dataName, dataValue);
  }
  out.print("<br/> <b>Session 列表</b>");
  Enumeration e = session.getAttributeNames();
  while (e.hasMoreElements()) {
     String name = (String)e.nextElement();
     String value = session.getAttribute(name).toString();
     out.println( name + " = " + value+"<br>");
         System.out.println( name + " = " + value);
   }
%>
  <form action="index.jsp" method="POST">
    name:<input type=text size=20 name="dataName">
     <br>
    &nbsp;&nbsp;value:<input type=text size=20 name="dataValue">
     <br>
    <input type=submit>
   </form>  
 </body>
 </html>