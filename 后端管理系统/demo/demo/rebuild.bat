@echo off
echo Cleaning and rebuilding project...
call mvnw.cmd clean compile
if %ERRORLEVEL% EQU 0 (
    echo Build successful!
) else (
    echo Build failed!
    pause
)

