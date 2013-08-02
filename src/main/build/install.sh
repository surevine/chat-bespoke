#!/bin/sh

#TOMCAT_HOME="/usr/share/tomcat6"
#TOMCAT_SERVICE="tomcat6"
#TOMCAT_USER="root"
#TOMCAT_GROUP="root"
TOMCAT_HOME="/opt/tomcat"
TOMCAT_SERVICE="tomcat"
TOMCAT_USER="tomcat"
TOMCAT_GROUP="tomcat"

WEBAPPS="${TOMCAT_HOME}/webapps"
DATE=`date "+%Y%m%d"`

if [[ $# -ne 2 ]]; then
    echo "Usage e.g.: ./install.sh client-build.zip build.properties"
    exit 1
fi

echo $1
echo $2

service ${TOMCAT_SERVICE} stop

cp -r ${WEBAPPS} "${WEBAPPS}.${DATE}"

if [ ! -d "/tmp/chat.${DATE}" ]; then
        mkdir "/tmp/chat.${DATE}"
fi

unzip -d "/tmp/chat.${DATE}" $1
cd "/tmp/chat.${DATE}/build"

echo "Executing build..."
echo

cp $2 build.properties
sh build.sh

echo
echo "Build executed. Continuing..."

rm -rf ${WEBAPPS}/chat*
cp chat.war ${WEBAPPS}
cd ${WEBAPPS}
mkdir chat
cd chat
jar xf ../chat.war

cp -f "${WEBAPPS}.${DATE}/chat/images/chat_logo.png" images/
chown -R ${TOMCAT_USER}.${TOMCAT_GROUP} ${TOMCAT_HOME}

service ${TOMCAT_SERVICE} start

echo "Done."
