if [ $# -lt 1 ]; then
 echo 'execute the tool'
 ./run.sh c
 nohup java -cp .:$CLASSPATH -Djava.ext.dirs=../lib com.pfizer.gdms.r48.CR843_SeqnumInUsedStatistic gdms dmfprd08 652PRDbnhaw7 "select gpseqnum from dm_dbo.pfe_t_compound_data where gpseqnum < 1000000" &
elif [ $1 = new ];then
 echo 'remove cache files'
 mv nohup.out nohup.out.last
 nohup ./run.sh &
elif [ $1 = c ];then
 echo 'Complie class'
 javac -cp .:$CLASSPATH -Djava.ext.dirs=../lib CR843_SeqnumInUsedStatistic.java
 mv CR843_SeqnumInUsedStatistic.class com/pfizer/gdms/r48/CR843_SeqnumInUsedStatistic.class
elif [ $1 = k ];then
 echo 'Kill the process'
 kill `ps -ef |grep CR843_SeqnumInUsedStatistic | grep PRD | grep -v grep | awk '{print $2}'`
elif [ $1 = ps ];then
 echo `ps -ef | grep CR843_SeqnumInUsedStatistic | grep PRD | grep -v grep`
fi
