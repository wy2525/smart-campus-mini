@echo off
chcp 65001 >nul
echo ====================================
echo 启动前端开发服务器
echo ====================================
echo.

cd qianduan

echo 正在启动前端开发服务器...
echo 前端地址: http://localhost:3001
echo 后端API代理: http://localhost:8082
echo.
echo 按 Ctrl+C 可以停止服务器
echo.

npm run dev

pause
