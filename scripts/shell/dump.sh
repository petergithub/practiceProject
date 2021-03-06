#!/usr/bin/env bash

# Dump useful info for analysis later: jstack, jinfo, jstat, jmap, lsof, netstat, iostat, mpstat, vmstat, free, sar, uptime
# dump.sh PID or dump.sh ProjectName
#
# dump path: SCRIPT_HOME/log/$DATE
# get the script: wget --no-check-certificate -O dump.sh https://raw.githubusercontent.com/petergithub/practiceProject/master/scripts/shell/dump.sh

if [ $# -lt 1 ]; then
    echo "ERROR: Required one parameter! Usage: dump.sh PID or dump.sh ProjectName"
    exit 1
fi
PROJECT_NAME=$1

PRGDIR=$(S=$(readlink "$0"); [ -z "$S" ] && S=$0; dirname $S)
LOGS_DIR=$PRGDIR/log

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PS_CONTENT=`ps --no-heading -C java -f --width 1000 | grep "$PROJECT_NAME"`
echo $PS_CONTENT
PIDS=`echo $PS_CONTENT | tee | awk '{print $2}'`

echo -e "PIDS="$PIDS
if [ -z "$PIDS" ]; then
    echo "ERROR: The $PROJECT_NAME does not started!"
    exit 1
fi

DUMP_DATE=`date +%Y%m%d%H%M%S`
DATE_DIR=$LOGS_DIR/dump/$DUMP_DATE
if [ ! -d $DATE_DIR ]; then
    mkdir -p $DATE_DIR
fi

echo -e "Dumping the pid $PIDS for project $PROJECT_NAME on host $SERVER_NAME to $DATE_DIR ...\c"
for PID in $PIDS ; do
    jstack $PID > $DATE_DIR/jstack-$PID.dump 2>&1
    echo -e ".\c"
    jinfo $PID > $DATE_DIR/jinfo-$PID.dump 2>&1
    echo -e ".\c"
    jstat -gcutil $PID > $DATE_DIR/jstat-gcutil-$PID.dump 2>&1
    echo -e ".\c"
    jstat -gccapacity $PID > $DATE_DIR/jstat-gccapacity-$PID.dump 2>&1
    echo -e ".\c"
    jmap $PID > $DATE_DIR/jmap-$PID.dump 2>&1
    echo -e ".\c"
    jmap -heap $PID > $DATE_DIR/jmap-heap-$PID.dump 2>&1
    echo -e ".\c"
    jmap -histo $PID > $DATE_DIR/jmap-histo-$PID.dump 2>&1
    echo -e ".\c"
    jmap -dump:live,format=b,file=$DATE_DIR/jmap-dump-$PID.bin $PID 2>&1
    echo -e ".\c"
    if [ -r /usr/sbin/lsof ]; then
    /usr/sbin/lsof -p $PID > $DATE_DIR/lsof-$PID.dump
    echo -e ".\c"
    fi
done
if [ -r /bin/netstat ]; then
/bin/netstat -an > $DATE_DIR/netstat.dump 2>&1
echo -e ".\c"
fi
if [ -r /usr/bin/iostat ]; then
/usr/bin/iostat > $DATE_DIR/iostat.dump 2>&1
echo -e ".\c"
fi
if [ -r /usr/bin/mpstat ]; then
/usr/bin/mpstat > $DATE_DIR/mpstat.dump 2>&1
echo -e ".\c"
fi
if [ -r /usr/bin/vmstat ]; then
/usr/bin/vmstat > $DATE_DIR/vmstat.dump 2>&1
echo -e ".\c"
fi
if [ -r /usr/bin/free ]; then
/usr/bin/free -t > $DATE_DIR/free.dump 2>&1
echo -e ".\c"
fi
if [ -r /usr/bin/sar ]; then
/usr/bin/sar > $DATE_DIR/sar.dump 2>&1
echo -e ".\c"
fi
if [ -r /usr/bin/uptime ]; then
/usr/bin/uptime > $DATE_DIR/uptime.dump 2>&1
echo -e ".\c"
fi
echo "OK!"
echo "DUMP: $DATE_DIR"


