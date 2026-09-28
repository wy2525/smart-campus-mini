@echo off
chcp 65001 >nul
echo ====================================
echo 旅游管理系统 - 一键启动脚本
echo ====================================
echo.

echo [1/3] 检查后端进程...
taskkill /F /IM java.exe 2>nul
timeout /t 2 >nul

echo [2/3] 启动后端服务...
cd /d "%~dp0demo\demo"
start "后端服务" cmd /k "mvnw.cmd spring-boot:run"
echo 后端服务启动中，请等待30秒...
timeout /t 30 /nobreak >nul

echo [3/3] 启动前端服务...
cd /d "%~dp0qianduan"
start "前端服务" cmd /k "npm run dev"
timeout /t 5 /nobreak >nul

echo.
echo ====================================
echo 启动完成！
echo 后端地址: http://localhost:8082
echo 前端地址: http://localhost:3000
echo 登录账号: admin / admin123
echo ====================================
echo.
pause
