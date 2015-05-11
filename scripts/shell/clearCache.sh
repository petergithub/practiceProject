echo clear cache
cd logs/Documentum
rm -rf datacache
cd $PP/../temp
rm -rf *
cd ../work
rm -rf Catalina

echo done!
cd $PP
restart.sh
