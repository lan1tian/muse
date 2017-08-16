export JAVA_HOME=${JAVA_HOME}

export JARVIS_SERVER_OPTS="-Xms8G -Xmx8G -Xmn512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"
export JARVIS_WORKER_OPTS="-Xms4G -Xmx4G -Xmn512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"
export JARVIS_RESTSERVER_OPTS="-Xms4G -Xmx4G -Xmn512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"
export JARVIS_LOGSERVER_OPTS="-Xms4G -Xmx4G -Xmn512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"