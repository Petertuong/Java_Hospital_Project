@echo off
REM Simple runner for HMS.jar — assumes Java and JavaFX available on module path if needed
setlocal
set JAR=target\HMS.jar
if not exist "%JAR%" (
    echo JAR not found. Please run: mvn -DskipTests clean package
    exit /b 1
)

echo Running HMS...
java -jar "%JAR%"
endlocal
