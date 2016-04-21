#
#  PROJECT Build Automation for BOFS and WAR
# 
#  USES:   Apache ANT 1.6.5
#
#  INPUT:  a properites file detailing the following values:
#				buildtype
#				
#				cvs.user
#				cvs.repository
#				cvs.module
#				cvs.module.sm
#				cvs.module.xm.sm
#				
#				keepalive.extra.string
#				wl.appname
#				wl.target
#				
#				dctm.docbaseName
#				dctm.userName
#				dctm.loginTix
#				
#				email.notification
#		
#
#  REQUIREMENTS:
#		all external jars must be placed in ANT_HOME/lib
# 			DDS.jar  (from Documentum Application Builder)
#				ant-contrib-1.0b3.jar 
#				commons-net-1.4.1.jar
#				jdom.jar
#				weblogic.jar

JAVA_HOME=/app/dmio53/bea92/jdk150_12
export JAVA_HOME

DMCL_CONFIG=./dmcl.ini
export DMCL_CONFIG

ANT_HOME=$DOCUMENTUM/tools/ant
export ANT_HOME

ANT_OPTS="-Xmx2048m -Xms512m"
export ANT_OPTS

echo `date` >> build.pid
echo $$ >> build.pid

$ANT_HOME/bin/ant -f ./build.xml -propertyfile build.properties | tee build.output
