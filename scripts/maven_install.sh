#!/bin/sh
LIB=../lib

mvn install:install-file -DgroupId=com.microsoft -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar -Dfile=$LIB/sqljdbc4-4.0.jar
mvn install:install-file -DgroupId=com.cloopen -DartifactId=CCPRestSDK -Dversion=2.6 -Dpackaging=jar -Dfile=$LIB/CCPRestSDK-2.6.jar 



