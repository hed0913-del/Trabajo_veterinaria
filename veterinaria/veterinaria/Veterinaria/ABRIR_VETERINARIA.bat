@echo off
chcp 65001 >nul
cd /d "%~dp0"
title VetU - Sistema de Gestion Veterinaria

echo ========================================
echo       VetU - Sistema Veterinario
echo ========================================
echo.

where java >nul 2>nul
if errorlevel 1 (
    echo ERROR: Java no esta instalado o no esta en PATH.
    pause
    exit /b 1
)

where javac >nul 2>nul
if errorlevel 1 (
    echo ERROR: Necesitas instalar un JDK.
    pause
    exit /b 1
)

if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo ERROR: No se encuentra el conector MySQL en la carpeta lib.
    pause
    exit /b 1
)

if not exist "src\Main.java" (
    echo ERROR: No se encuentra src\Main.java.
    echo Revisa que hayas abierto la carpeta correcta.
    pause
    exit /b 1
)

if not exist "build" mkdir build

echo Compilando el proyecto...
javac -encoding UTF-8 -cp "lib\mysql-connector-j-8.0.33.jar;lib\flatlaf.jar" -d build src\DB.java src\Main.java src\PruebaConexion.java src\dao\*.java src\modelo\*.java src\servicio\*.java

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo compilar el proyecto.
    echo Copia el mensaje anterior para revisar el problema.
    pause
    exit /b 1
)

echo.
echo Iniciando VetU...
java -cp "build;lib\mysql-connector-j-8.0.33.jar;lib\flatlaf.jar" Main

if errorlevel 1 (
    echo.
    echo El programa termino con un error.
    pause
)