#sh shutdown.sh
pid=`ps -ef|grep "/home/xray/local/tomcat-jarvis2-test"|grep -v grep|awk '{print $2}'`
kill -9 ${pid}