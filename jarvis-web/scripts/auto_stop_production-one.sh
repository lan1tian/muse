#sh shutdown.sh
pid=`ps -ef|grep "/home/data/programs/tomcat-jarvis2-one"|grep -v grep|awk '{print $2}'`
kill -9 ${pid}