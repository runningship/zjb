#!/bin/sh
LIB=../lib

mvn install:install-file -DgroupId=com.microsoft -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar -Dfile=$LIB/sqljdbc4-4.0.jar
mvn install:install-file -DgroupId=com.cloopen -DartifactId=CCPRestSDK -Dversion=2.6 -Dpackaging=jar -Dfile=$LIB/CCPRestSDK-2.6.jar 
mvn install:install-file -DgroupId=org.java-websocket -DartifactId=bc-Java-WebSocket -Dversion=0.1 -Dpackaging=jar -Dfile=$LIB/bc-Java-WebSocket-0.1.jar



