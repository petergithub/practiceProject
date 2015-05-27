<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration PUBLIC
  "-//APACHE//DTD LOG4J 1.2//EN" "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd">
<!-- <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd"> -->
<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>
	<!-- <property name="log.dir" value="C:/Documentum/logs" /> -->
	<variables properties="file://log4j.properties"/>
	<appender name="CONSOLE" class="org.apache.log4j.ConsoleAppender">
		<layout class="org.apache.log4j.PatternLayout">
			<param name="ConversionPattern" value="%d{HH:mm:ss}[%t] %-5p %c - %m%n" />
		</layout>
	</appender>

	<appender name="FILE" class="org.apache.log4j.RollingFileAppender">
		<param name="File" value="${log.dir}/log4j.log" />
		<param name="Append" value="true" />
		<param name="MaxFileSize" value="2MB" />
		<param name="MaxBackupIndex" value="3" />
		<layout class="org.apache.log4j.PatternLayout">
			<param name="ConversionPattern" value="%d{HH:mm:ss}[%t] %-5p %c - %m%n" />
		</layout>
		<filter class="org.apache.log4j.varia.LevelRangeFilter">
			<param name="LevelMin" value="TRACE" />
			<param name="LevelMax" value="ERROR" />
			<param name="AcceptOnMatch" value="true" />
		</filter>
	</appender>

	<appender name="INFO" class="org.apache.log4j.RollingFileAppender">
		<param name="File" value="C:/Documentum/logs/trace.log" />
		<param name="Append" value="true" />
		<param name="MaxFileSize" value="2MB" />
		<param name="MaxBackupIndex" value="3" />
		<layout class="org.apache.log4j.PatternLayout">
			<param name="ConversionPattern" value="%d{HH:mm:ss}[%t] %-5p %c - %m%n" />
		</layout>
		<filter class="org.apache.log4j.varia.LevelRangeFilter">
			<param name="LevelMin" value="INFO" />
		</filter>
	</appender>

	<logger name="test.base">
		<level value="DEBUG" />
	</logger>
	<logger name="test">
		<level value="TRACE" />
	</logger>
	<root>
		<priority value="TRACE" />
		<!-- <priority value="DEBUG" /> -->
		<!-- <priority value="INFO" /> -->
		
		<appender-ref ref="CONSOLE" />
		<appender-ref ref="FILE" />
		<appender-ref ref="INFO" />
	</root>

</log4j:configuration>
