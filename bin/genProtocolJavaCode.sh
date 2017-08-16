#!/usr/bin/env bash

BIN_PATH=$(cd $(dirname $0);pwd)
JARVIS_HOME=`dirname "$BIN_PATH"`
JARVIS_PROTOCOL_MAIN="${JARVIS_HOME}/jarvis-protocol/src/main"

protoc --java_out=${JARVIS_PROTOCOL_MAIN}/java --proto_path=${JARVIS_PROTOCOL_MAIN}/resources/protos ${JARVIS_PROTOCOL_MAIN}/resources/protos/*.proto
