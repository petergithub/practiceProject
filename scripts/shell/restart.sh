#!/bin/bash
shutdown() {
	echo "restart tomcat"
	echo "shutdown"
	~/peter/tom-cat-6.0.18/bin/shutdown.sh > /dev/null
	
#	cp $PP/bak/web.xml ../webapps/project/WEB-INF/web.xml
	echo sleep $SLEEPSECS
	sleep $SLEEPSECS
}

JAVA_OPTS="$JAVA_OPTS -Xms1024m -Xmx1024m -XX:+UseParallelOldGC -XX:MaxPermSize=256m -verbose:gc"
#JAVA_OPTS="$JAVA_OPTS -Xms2048m -Xmx2048m -XX:+UseParallelOldGC -XX:MaxPermSize=256m -verbose:gc"
#JAVA_OPTS="$JAVA_OPTS -server -Xms1024m -Xmx1024m -XX:+PrintGCTimeStamps -XX:+PrintClassHistogram -XX:+PrintGCDetails  -XX:+UseConcMarkSweepGC -verbose:gc" 
#JAVA_OPTS="$JAVA_OPTS -server -Xms1024m -Xmx1024m -XX:NewSize=512m -XX:MaxPermSize=512m -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC -verbose:gc" 
#JAVA_OPTS="$JAVA_OPTS -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:logs/gc.log"
export JAVA_OPTS

SLEEPSECS=5
if [ $# -lt 1 ]; then
	shutdown
	echo "startup"
	~/peter/tom-cat-6.0.18/bin/startup.sh
	tail -f ~/peter/tom-cat-6.0.18/logs/catalina.out
elif [ $1 = jpda ];then 
	shutdown
	JAVA_OPTS="$JAVA_OPTS -Ddfc.development.bof.registry_file=/app/username/peter/tom-cat-6.0.18/project/WEB-INF/classes/bof_registry_file.properties" 
        export JAVA_OPTS
	echo "startup in debug"
	../bin/catalina.sh jpda start
	tail -f ../logs/catalina.out
elif [ $1 = jpdavm ];then 
	shutdown 
	JAVA_OPTS="$JAVA_OPTS -Ddfc.development.bof.registry_file=/app/username/peter/tom-cat-6.0.18/project/WEB-INF/classes/bof_registry_file.properties"
	JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=hostname -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=5000 -Dcom.sun.management.jmxremote.authenticate=true  -Dcom.sun.management.jmxremote.ssl=false  -Dcom.sun.management.jmxremote.password.file=./bak/jmxremote.password  -Dcom.sun.management.jmxremote.access.file=./bak/jmxremote.access" 	
    export JAVA_OPTS
	echo "startup in debug"
	../bin/catalina.sh jpda start
	tail -f ../logs/catalina.out
elif [ $1 = 235 ];then
	echo "restart 235 tomcat"
	cd 235/bin
	echo "shutdown"
	./shutdown.sh > /dev/null
	sleep $SLEEPSECS
	echo "startup"
	./startup.sh
	tail -f ../logs/catalina.out
elif [ $1 = jms ];then
	cd /app/username/share/jboss4.2.0_project/server/
	./stopMethodServer.sh
        sleep $SLEEPSECS

	cd $PP
	tail launch/jmsLogs/server.log | grep 'Shutdown complete'
	shutdownComplete=$?
	while [ $shutdownComplete -eq 1 ]; do
	        echo "still on, please wait a second, i need more time to shutdown it"
		sleep 2
	        tail launch/jmsLogs/server.log | grep 'Shutdown complete'
	        shutdownComplete=$?
	done
	echo "shutdow complete"

	cd /app/username/share/jboss4.2.0_project/server/
	echo "restart jms"
	if [ $# -gt 1 ];then
		if [ $2 = jpda ];then
			echo "in debug mode"
        		JAVA_OPTS="$JAVA_OPTS:-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
        		export JAVA_OPTS
		fi
	else
		echo "in normal mode"
	fi
	nohup ./startMethodServer.sh &
	cd $PP
	tail -f launch/jmsLogs/server.log
else
	echo incorrect parameter! no parameter or jpda / jms / 235 is correct
	exit
fi
