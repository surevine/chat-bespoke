#!/bin/sh
# This script automatically substitutes the install time variables held
# in the properties file 'build.properties' into the release artifcat
# so that it is correctly configured for the target environment.

# Abort on error
set -e

# Set environment variables
CHAT_WAR=chat.war
WARS=(`find . -maxdepth 1 -type f -name "*.war"`)

echo "Please wait..."
if [[ ${#WARS[@]} -ne 1 ]]; then
    echo "You have more war files than I know what to do with. Exiting."
    exit 1
fi

if [[ ${WARS[0]##*/} != "chat.war" ]]; then
    mv ${WARS[0]##*/} $CHAT_WAR
fi

echo "...extracting files"
jar -xf ${CHAT_WAR} chatclient WEB-INF/web.xml index.html

echo "...executing replacements"
if [[ $DEV_MODE -ne 1 ]]; then
	sed -i '5 a <script type="text/javascript" language="javascript">document.domain="%%chat.domain%%";</script>' index.html
fi

sed -i -f build.properties WEB-INF/web.xml index.html

for f in `find chatclient/ -name *.cache.html`; do
	# If BTB hasn't set DEV_MODE=1 and we don't have a document.domain on line 3
	if [[ $DEV_MODE -ne 1 && `sed -n '3p' $f | grep "document\.domain" | wc -l` -eq 0 ]]; then
		# Append one after line 2
    	sed -i '2 a document.domain="%%chat.domain%%";' $f
    fi
    
    sed -i -f build.properties $f
done

echo "...updating application package"
jar -uf ${CHAT_WAR} chatclient WEB-INF/web.xml index.html

echo "Done."

