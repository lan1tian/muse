#!/usr/bin/env bash

usage="Usage: jarvis-daemon.sh (start|stop|restart) <jarvis-command>"

# if no args specified, show usage
if [ $# -le 1 ]; then
  echo $usage
  exit 1
fi

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

JARVIS_PREFIX=`dirname "$bin"`

export JARVIS_CONF_DIR="${JARVIS_PREFIX}/conf"
export JARVIS_LIB_DIR="${JARVIS_PREFIX}/lib"
export JARVIS_LOG_DIR="${JARVIS_PREFIX}/logs"

# get arguments
action=$1
shift
command=$1
shift


if [ ! -f "/tmp/${USER}/jarvis/" ]; then
    mkdir -p /tmp/${USER}/jarvis/
fi
PID="/tmp/${USER}/jarvis/${command}.pid"

if [ -f "${JARVIS_CONF_DIR}/jarvis-env.sh" ]; then
    . "${JARVIS_CONF_DIR}/jarvis-env.sh"
fi


if [ -x "${JAVA_HOME}/bin/java" ]; then
    JAVA="${JAVA_HOME}/bin/java"
else
    JAVA=`which java`
fi

if [ ! -x "${JAVA}" ]; then
    echo "Could not find any executable java binary. Please install java in your PATH or set JAVA_HOME"
    exit 1
fi


if [ "${command}" == "server" ]; then
    MAIN_CLASS="com.mogujie.jarvis.server.JarvisServer"
    JAVA_OPTS=${JARVIS_SERVER_OPTS}
elif [ "${command}" == "worker" ] ; then
    MAIN_CLASS="com.mogujie.jarvis.worker.JarvisWorker"
    JAVA_OPTS=${JARVIS_WORKER_OPTS}
elif [ "${command}" == "rest" ] ; then
    MAIN_CLASS="com.mogujie.jarvis.rest.JarvisRest"
    JAVA_OPTS=${JARVIS_RESTSERVER_OPTS}
elif [ "${command}" == "logstorage" ] ; then
    MAIN_CLASS="com.mogujie.jarvis.logstorage.JarvisLogstorage"
    JAVA_OPTS=${JARVIS_LOGSERVER_OPTS}
else
    echo "Invalid command. Valid commands: server, worker, rest, logstorage"
    exit 1
fi


start()
{
    export LD_LIBRARY_PATH=${JARVIS_LIB_DIR}/native
    nohup ${JAVA} -Dlog_dir=${JARVIS_LOG_DIR} -Drole=${command} ${JAVA_OPTS} -cp ${JARVIS_CONF_DIR}:${JARVIS_LIB_DIR}/common/*:${JARVIS_LIB_DIR}/${command}/* ${MAIN_CLASS} > ${JARVIS_LOG_DIR}/${command}.out 2>&1 &
    echo $! > ${PID}
    echo "${command} started."
}

stop()
{
    if [ -f ${PID} ]; then
        TARGET_PID=`cat ${PID}`
        kill -9 ${TARGET_PID}
        echo "${command} killed."
    else
        echo "pid file not exist."
    fi
}


if [ "${action}" == "start" ]; then
    start
elif [ "${action}" == "stop" ] ; then
    stop
elif [ "${action}" == "restart" ] ; then
    stop
    start
else
    echo "Invalid action. Valid action: start, stop, restart"
    exit 1
fi
