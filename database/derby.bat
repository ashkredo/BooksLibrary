@echo off
set DERBY_HOME=C:\Program Files (x86)\Java\jdk1.7.0_55\db
set DERBY_SYSTEM_HOME=C:\Users\s15444\Desktop\DerbyDbF

set LOCALCLASSPATH=%DERBY_HOME%/lib/derby.jar;%DERBY_HOME%/lib/derbynet.jar;%DERBY_HOME%/lib/derbyclient.jar;%DERBY_HOME%/lib/derbytools.jar

@if "%1" == "start" goto startServer
@if "%1" == "stop" goto stopServer
@if "%1" == "ij" goto ij
goto invArgs 

:ij
java -Dderby.system.home=%DERBY_SYSTEM_HOME% -cp "%LOCALCLASSPATH%;%CLASSPATH%" -Dij.protocol=jdbc:derby: org.apache.derby.tools.ij %2
goto end

:startServer
java -Dderby.system.home=%DERBY_SYSTEM_HOME% -cp "%LOCALCLASSPATH%;%CLASSPATH%" org.apache.derby.drda.NetworkServerControl start
goto end

:stopServer
java -Dderby.system.home=%DERBY_SYSTEM_HOME% -cp "%LOCALCLASSPATH%;%CLASSPATH%" org.apache.derby.drda.NetworkServerControl shutdown
goto end

:invArgs
echo Invalid arguments
:end