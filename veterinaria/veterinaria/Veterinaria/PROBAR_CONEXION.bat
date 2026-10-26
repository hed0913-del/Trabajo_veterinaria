@echo off
chcp 65001 >nul
cd /d "%~dp0"
title Prueba de conexión VetU
where javac >nul 2>nul
if errorlevel 1 (echo ERROR: Instala un JDK y configura PATH.& pause & exit /b 1)
if not exist "build" mkdir build
javac -encoding UTF-8 -cp "lib\mysql-connector-j-8.0.33.jar" -d build src\DB.java src\PruebaConexion.java
if errorlevel 1 (echo ERROR al compilar prueba de conexión.& pause & exit /b 1)
java -cp "build;lib\mysql-connector-j-8.0.33.jar" PruebaConexion
pause
