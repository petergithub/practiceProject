# deploy class.zip and projectName.zip

LOG=log.deploy.log

if [ $# -lt 1 ]; then
	rm -rf ../projectName
	cp -r ../webapps/projectName ../projectName
	echo "Start the deployment"
	#echo "After upload projectName zip file, press enter to update projectName folder?[y/n]"
	#read isUpdate
	isUpdate=y
	if [ $isUpdate = y -o $isUpdate = Y ]; then
		echo "enter update projectName folder"
		cd ../webapps/projectName
		cp $PP/projectName.zip .
		rm -rf projectName
		unzip -q projectName.zip
		cp $PP/bak/app.xml projectName/app.xml
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
	cd ~/projectName-tomcat-6.0.18/
	rm -rf projectName
	cd webapps/
	mv projectName ..
	cp -r $PP/../webapps/projectName .
	cp $PP/bak/dfc.properties.235 projectName/WEB-INF/classes/dfc.properties
	cd $PP
	./restart.sh 235
elif [ $1 = jms ]; then
	echo "Start deploy projectSM.jar"
	cp projectSM.jar /app/username/share/jboss4.2.0_project/server/DctmServer_MethodServer/deploy/ServerApps.ear/DmMethods.war/WEB-INF/lib/projectSM.jar
	#cd /app/username/share/jboss4.2.0_project/server/DctmServer_MethodServer/deploy/ServerApps.ear/DmMethods.war/WEB-INF/lib
	#cp $PP/projectSM.jar .
	./restart.sh jms
fi
