#!/usr/bin/env bash


version='jarvis-2.0-bin'

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

JARVIS_PREFIX=`dirname "$bin"`
JARVIS_DIST_DIR="${JARVIS_PREFIX}/dist"
JARVIS_TARGET_DIR="${JARVIS_DIST_DIR}/${version}"
JARVIS_TARGET_LIB_DIR="${JARVIS_TARGET_DIR}/lib"

cd $JARVIS_PREFIX
mvn clean install -Dmaven.test.skip=true -f ${JARVIS_PREFIX}/pom.xml

echo "Packaging..."

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
mkdir -p ${JARVIS_TARGET_DIR}/bin
cp ${JARVIS_PREFIX}/bin/jarvis-* ${JARVIS_TARGET_DIR}/bin/
cp -r ${JARVIS_PREFIX}/conf ${JARVIS_TARGET_DIR}/conf
mkdir -p ${JARVIS_TARGET_DIR}/logs
cd ${JARVIS_DIST_DIR} && tar zcf ${version}.tar.gz ${version} && rm -rf ${version}


echo "Package finished: ${JARVIS_DIST_DIR}/${version}.tar.gz"
