
#upload file to remote server
pscp -i %putty%/putty.ppk C:\Workspace\clonevdoc\* dmfdev08@amrndhl235:peter/clonevdoc/

#run shell in local file on remote server:
plink dmfdev08@amrndhl235 -i %putty%/putty.ppk -m cmdRestart.sh
#run shell in remote file on remote server:
plink dmfdev08@amrndhl235 -i %putty%/putty.ppk export JAVA_HOME=/app/dmfdev08/share/java/1.5.0_12/;export PATH=$JAVA_HOME/bin:$PATH;export PP=/app/dmfdev08/peter/tom-cat-6.0.18/sbin/;cd $PP;./deploy.sh '235'

#check count of thread and send email notification
#!/bin/bash
# send mail if session count if greater than 300
SESSION_COUNT=`ps -ef|grep docbase|grep dmfdev08|grep "gdmsd05 "|wc -l`
echo "gdms docbase sessions exceed the threshold! Open sessions: $SESSION_COUNT" > sessioncount.txt
if [ $SESSION_COUNT -ge 3 ];
then
    cat sessioncount.txt | mail -s "Docbase sessions alert" me@gmail.com
fi
