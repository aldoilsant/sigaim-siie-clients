#!/bin/sh

WSDL2JAVA="/Applications/axis2-1.6.2/bin/wsdl2java.sh"
BASE_URI="http://localhost:8080/SIIEWS3/services"

rm src/org/sigaim/siie/ws2/*.java
"$WSDL2JAVA" -uri $BASE_URI/INTSIIE001EQLImplService?wsdl  -p org.sigaim.siie.ws2 -d adb 
"$WSDL2JAVA" -uri $BASE_URI/INTSIIE003TerminologiesImplService?wsdl  -p org.sigaim.siie.ws2 -d adb 
"$WSDL2JAVA" -uri $BASE_URI/INTSIIE004ReportManagementImplService?wsdl  -p org.sigaim.siie.ws2 -d adb 
