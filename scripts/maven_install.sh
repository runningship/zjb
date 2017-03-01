#!/bin/sh
LIB=../lib

mvn install:install-file -DgroupId=org.bc -DartifactId=bc-ueditor -Dversion=1.0 -Dpackaging=jar -Dfile=$LIB/bc-ueditor-1.0.jar
mvn install:install-file -DgroupId=org.bc -DartifactId=proxool-bc -Dversion=0.9.1-bc-1 -Dpackaging=jar -Dfile=$LIB/proxool-bc-0.9.1-bc-1.jar
mvn install:install-file -DgroupId=org.bc -DartifactId=sdak -Dversion=makesite1.0 -Dpackaging=jar -Dfile=$LIB/sdak-makesite1.0.jar
mvn install:install-file -DgroupId=com.microsoft -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar -Dfile=$LIB/sqljdbc4-4.0.jar
mvn install:install-file -DgroupId=com.cloopen -DartifactId=CCPRestSDK -Dversion=2.6 -Dpackaging=jar -Dfile=$LIB/CCPRestSDK-2.6.jar 
mvn install:install-file -DgroupId=org.java-websocket -DartifactId=bc-Java-WebSocket -Dversion=0.1 -Dpackaging=jar -Dfile=$LIB/bc-Java-WebSocket-0.1.jar



