# travel-system 旅游分享小程序

基于 **uniapp + Spring Boot + Vue3** 的旅游分享小程序及后台管理系统。

## 项目结构

```
├── 后端管理系统/          # Spring Boot 后端
│   ├── demo/            # 后端主服务
│   └── qianduan/        # Vue3 后台管理系统前端
├── 小程序/               # uniapp 旅游分享小程序
│   └── ly/              # 小程序主目录
├── sql.sql              # 数据库脚本
├── travel_system.sql    # 完整数据库导出
└── *.md                 # 项目文档
```

## 技术栈

- **小程序端**：uniapp + Vue3
- **后端**：Spring Boot + JPA + MySQL
- **后台管理**：Vue3 + Vite

## 快速开始

1. 导入 `travel_system.sql` 到 MySQL
2. 配置 `后端管理系统/demo/demo/src/main/resources/application.properties` 数据库连接
3. 启动 Spring Boot 后端（端口 8082）
4. 启动 Vue3 后台管理系统
5. 用微信开发者工具打开 `小程序/ly` 目录

## 文档

- `项目概述.md` - 项目整体介绍
- `功能模块说明.md` - 功能模块详解
- `API接口文档.md` - 接口文档
- `安装部署指南.md` - 部署指南
- `页面结构说明.md` - 页面结构

## 说明

数据库密码等敏感配置已替换为占位符，请按需修改。