@echo off
chcp 65001 >nul
echo ====================================
echo 启动旅游小程序后台管理系统
echo ====================================
echo.

echo [1/2] 启动后端服务...
start cmd /k "cd demo && start.bat"
timeout /t 3 >nul

echo [2/2] 启动前端开发服务器...
start cmd /k "cd qianduan && npm run dev"
timeout /t 2 >nul

echo.
echo ====================================
echo 系统启动完成！
echo ====================================
echo.
echo 后端服务: http://localhost:8082
echo 前端服务: http://localhost:3001
echo.
echo 默认管理员账号:
echo   用户名: admin
echo   密码: admin123
echo.
echo 按 Ctrl+C 可以停止服务器
echo.

pause
