Param()
Write-Host "Building project..."
mvn -DskipTests clean package
if ($LASTEXITCODE -ne 0) { Write-Error "Build failed."; exit 1 }

Write-Host "Edit the variable `$javafxLib` below to point to your JavaFX SDK lib folder if needed."
$javafxLib = 'C:\path\to\javafx-sdk-21\lib'
Write-Host "Running app with JavaFX modules..."
Start-Process -NoNewWindow -Wait -FilePath java -ArgumentList "--module-path $javafxLib --add-modules javafx.controls,javafx.fxml -jar target\HMS.jar"
