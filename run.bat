@echo off
echo Starting Hospital Management System...
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.9.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
set DB_URL=jdbc:mysql://127.0.0.1:3306/hospital?serverTimezone=UTC
set DB_USER=hospitaluser
set DB_PASS=hospital_password

mvn clean compile
mvn javafx:run
