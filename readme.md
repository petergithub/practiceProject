This is a tool box and practice project

`mkdir -p src/{main,test}/{java,resources}`

http://localhost:8080/index.html
http://localhost:8080/servlet/ModernServlet
http://localhost:8080/servlet/PrimitiveServlet


-Xms2m -Xmx8m -Djava.rmi.server.hostname=computerName -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=5000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false
读源码的方法:
大大前提,把源码全下载下来,按手册的说明,把test跑通.
1. 堆栈
例如Ioc,你肯定想知道一个bean是如何被创建的. 那么,在你的类的构造方法中,加入一句: new Throwable().printStackTrace();
跟着堆栈一层层了解

http://openextern.googlecode.com/svn/trunk
http://pathtools.googlecode.com/svn/trunk
http://svn.apache.org/repos/asf

:pserver:anonymous@easystruts.cvs.sourceforge.net:/cvsroot/easystruts

https://github.com/springside/springside4.git
https://github.com/SpringSource/spring-mvc-showcase.git

J2SE 8 = 52,
J2SE 7 = 51,
J2SE 6.0 = 50,
J2SE 5.0 = 49,
JDK 1.4 = 48,
JDK 1.3 = 47,
JDK 1.2 = 46,
JDK 1.1 = 45
