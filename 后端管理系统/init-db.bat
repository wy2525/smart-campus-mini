@echo off
chcp 65001 >nul
echo ====================================
echo 数据库初始化脚本
echo ====================================
echo.

echo 请确保MySQL服务已启动
echo.

set MYSQL_ROOT_PASSWORD=wy123456
set DATABASE_NAME=travel_system

echo [1/4] 删除旧数据库...
mysql -uroot -p%MYSQL_ROOT_PASSWORD% -e "DROP DATABASE IF EXISTS %DATABASE_NAME%;" 2>nul
if errorlevel 1 (
    echo 错误：无法连接到MySQL或删除数据库失败
    echo 请检查MySQL服务是否启动，密码是否正确
    pause
    exit /b 1
)

echo [2/4] 创建新数据库...
mysql -uroot -p%MYSQL_ROOT_PASSWORD% -e "CREATE DATABASE %DATABASE_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

echo [3/4] 执行初始化SQL...
mysql -uroot -p%MYSQL_ROOT_PASSWORD% %DATABASE_NAME% < "%~dp0database_init.sql"

echo [4/4] 执行测试数据...
mysql -uroot -p%MYSQL_ROOT_PASSWORD% %DATABASE_NAME% < "%~dp0demo\demo\src\main\resources\data.sql"

echo.
echo ====================================
echo 数据库初始化完成！
echo 数据库名称: %DATABASE_NAME%
echo ====================================
echo.
pause
