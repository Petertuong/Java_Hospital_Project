@echo off
REM Runner for HMS.jar with explicit JavaFX SDK path.
REM Usage: set JAVA_FX_LIB=C:\path\to\javafx-sdk-21.0.2\lib
REM then run this script. Or edit the JAVAFX variable below.
setlocal
set JAR=target\HMS.jar
if not exist "%JAR%" (
    echo JAR not found. Please run: mvn -DskipTests clean package
    exit /b 1
)















endlocaljava --module-path "%JAVAFX%" --add-modules javafx.controls,javafx.fxml -jar "%JAR%"
necho Running with JavaFX from %JAVAFX%)    exit /b 1    echo Set environment variable JAVA_FX_LIB to your JavaFX SDK lib directory or edit this script.    echo JavaFX lib path not found: %JAVAFX%
nif not exist "%JAVAFX%" ()    set JAVAFX=C:\path\to\javafx-sdk-21.0.2\lib    REM Edit this path if you know where your JavaFX SDK is installed) else (    set JAVAFX=%JAVA_FX_LIB%:: try environment variable first
nif defined JAVA_FX_LIB (