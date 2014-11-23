package demo.javabasics.demo.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @author carlwu
 * refer to JMXIntroduction.txt under doc folder
 */
public class CentralHeaterAgent {
	private static MBeanServer mBeanServer;
	
	// seems that need start tomcat first in eclipse (start in tomcat/bin is not helpful)
//	运行代理层代码，运行时，请加上下面几个 JVM 运行时参数: 
//	-Dcom.sun.management.jmxremote
//	-Dcom.sun.management.jmxremote.port=9999
//	-Dcom.sun.management.jmxremote.ssl=false
//	-Dcom.sun.management.jmxremote.authenticate=false
//	-Djava.rmi.server.hostname=localhost
	public static void main(String[] args) throws Exception {

		ObjectName oname;
		// get the default MBeanServer from Management Factory

		mBeanServer = ManagementFactory.getPlatformMBeanServer();
		// try {
		// create a instance of CentralHeaterImpl class
		CentralHeaterInf centralHeater = new CentralHeaterImpl();

		// assign a Object name to above instance
		oname = new ObjectName("MyHome:name=centralheater");

		// register the instance of CentralHeaterImpl to MBeanServer
		mBeanServer.registerMBean(centralHeater, oname);

		System.out.println("Press any key to end our JMX agent...");
		System.in.read();
	}
}
