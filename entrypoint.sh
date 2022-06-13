#!/bin/bash
EXPORT_VARS_FILES=${EXPORT_VARS_FILES:-true}

exec java ${JAVA_OPTS} \
 -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
 -jar "/usr/src/app/the-crm-service.jar"
