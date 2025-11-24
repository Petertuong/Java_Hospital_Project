@echo off
REM Edit JAVA_FX path below to point to your JavaFX SDK lib folder
set JAVA_FX="C:\path\to\javafx-sdk-21\lib"
echo Building project...
mvn -DskipTests clean package
if errorlevel 1 goto :err
echo Running app with JavaFX modules...
java --module-path %JAVA_FX% --add-modules javafx.controls,javafx.fxml -jar target\HMS.jar
goto :eof
:err
echo Build failed.
exit /b 1
