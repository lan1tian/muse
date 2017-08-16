#!/usr/bin/env bash
BIN_PATH=$(cd $(dirname $0);pwd)
JARVIS_HOME=`dirname "$BIN_PATH"`
JARVIS_DAO_HOME=${JARVIS_HOME}/jarvis-dao
MYBATIS_CONFIG_FILE=${JARVIS_DAO_HOME}/src/main/resources/mybatis_generator.xml

cd ${JARVIS_DAO_HOME}
rm ${JARVIS_DAO_HOME}/src/main/resources/com/mogujie/jarvis/dao/generate/*.xml

#mvn org.mybatis.generator:mybatis-generator-maven-plugin:generate

java -cp ${BIN_PATH}/mybatis-generator-core-1.3.3.jar:${BIN_PATH}/mysql-connector-java-5.1.37.jar  org.mybatis.generator.api.ShellRunner -configfile ${MYBATIS_CONFIG_FILE} -overwrite -verbose
