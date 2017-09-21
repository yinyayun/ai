#!/bin/sh
ORIGIN_DIR=`pwd`
EXE="$0"
FILENAME=`basename "$EXE"`
MAINCLASS="org.yinyayun.nlp.TokenizerTrain"

EXEDIR=`dirname "$EXE"`

LIB="$EXEDIR/lib"
CONF="$EXEDIR/conf"

echo "Usage: $0 trainFile modelSavePath"

echo $1
echo $2

java -Xms8000m -Xmx10000m -Djava.ext.dirs=$LIB:$JAVA_HOME/jre/lib/ext $MAINCLASS -propertiesPath $CONF -trainFile $1 -modelSavePath $2
