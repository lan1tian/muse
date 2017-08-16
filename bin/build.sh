#!/usr/bin/env bash


bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

JARVIS_PREFIX=`dirname "$bin"`
JARVIS_TARGET_LIB_DIR="${JARVIS_PREFIX}/lib"

cd $JARVIS_PREFIX
git pull
mvn clean install -Dmaven.test.skip=true -f ${JARVIS_PREFIX}/pom.xml

echo "start copy lib..."

mkdir -p ${JARVIS_TARGET_LIB_DIR}/native
mkdir -p ${JARVIS_TARGET_LIB_DIR}/common
mkdir -p ${JARVIS_TARGET_LIB_DIR}/rest
mkdir -p ${JARVIS_TARGET_LIB_DIR}/server
mkdir -p ${JARVIS_TARGET_LIB_DIR}/worker
mkdir -p ${JARVIS_TARGET_LIB_DIR}/logstorage
find ./jarvis-core/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/common \;
find ./jarvis-dao/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/common \;
find ./jarvis-protocol/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/common \;
find ./jarvis-logstorage/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/logstorage \;
find ./jarvis-rest/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/rest \;
find ./jarvis-server/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/server \;
find ./jarvis-worker/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/worker \;
find ./jarvis-tasks/target -name *.jar -exec cp {} ${JARVIS_TARGET_LIB_DIR}/worker \;

echo "build finished"
