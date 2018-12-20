#!/bin/sh

VERSION=1.6
PROGRAM_NAME=kakyll
JAR_FILE=$PROGRAM_NAME-$VERSION.jar
NEXUS_URL=http://search.maven.org/remotecontent?filepath=com/kennycason/$PROGRAM_NAME/$VERSION/$JAR_FILE
BINARY_DIR=/usr/local/bin/
BINARY=$BINARY_DIR$PROGRAM_NAME
APP_DIR=/usr/local/opt/$PROGRAM_NAME/

if [ -f "$APP_DIR" ]; then
    echo "$APP_DIR already exists, overwriting"
fi
if [ -f "$BINARY" ]; then
    echo "$BINARY already exists, overwriting"
fi

mkdir $APP_DIR

wget $NEXUS_URL -O $APP_DIR/$JAR_FILE
echo $'#!/bin/sh\n java -cp '$APP_DIR$JAR_FILE' com.kennycason.kakyll.KakyllKt "$@"' > $BINARY
chmod +x $BINARY

run version