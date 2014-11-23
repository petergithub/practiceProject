# deploy class.zip and gdms.zip

LOG=log.deploy.log

if [ $# -lt 1 ]; then
	rm -rf ../gdms
	cp -r ../webapps/gdms ../gdms
	echo "Start the deployment"
	#echo "After upload gdms zip file, press enter to update gdms folder?[y/n]"
	#read isUpdate
	isUpdate=y
	if [ $isUpdate = y -o $isUpdate = Y ]; then
		echo "enter update gdms folder"
		cd ../webapps/gdms
		cp $PP/gdms.zip .
		rm -rf gdms
		unzip -q gdms.zip
		cp $PP/bak/app.xml gdms/app.xml
	fi
	
	#echo "After upload classes zip file, update classes folder?[y/n]"
	#read isUpdateClasses
	isUpdateClasses=y
	if [ $isUpdateClasses = y -o $isUpdateClasses = Y ]; then
		echo "enter update class folder"
		cd WEB-INF/
		cp $PP/classes.zip .
		rm -rf classes
		unzip -q classes.zip 
		cp $PP/bak/dfc.properties classes/dfc.properties
		cp $PP/bak/log4j.properties classes/log4j.properties
	fi
	
#	read -p "Press enter to restart tomcat"
	cd $PP
	./restart.sh
elif [ $1 = 235 ];then
	echo "Start the deployment on 235"
	cd ~/gdms-tomcat-6.0.18/
	rm -rf gdms
	cd webapps/
	mv gdms ..
	cp -r $PP/../webapps/gdms .
	cp $PP/bak/dfc.properties.235 gdms/WEB-INF/classes/dfc.properties
	cd $PP
	./restart.sh 235
elif [ $1 = jms ]; then
	echo "Start deploy gdmsSM.jar"
	cp gdmsSM.jar /app/dmfdev08/share/jboss4.2.0_gdms/server/DctmServer_MethodServer/deploy/ServerApps.ear/DmMethods.war/WEB-INF/lib/gdmsSM.jar
	#cd /app/dmfdev08/share/jboss4.2.0_gdms/server/DctmServer_MethodServer/deploy/ServerApps.ear/DmMethods.war/WEB-INF/lib
	#cp $PP/gdmsSM.jar .
	./restart.sh jms
fi
