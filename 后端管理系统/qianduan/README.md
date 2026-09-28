# 旅游小程序后台管理系统 - 前端

基于 Vue 3 + TypeScript + Element Plus + Pinia 构建的旅游小程序后台管理系统前端。

## 技术栈

- **Vue 3**: 使用 Composition API
- **TypeScript**: 类型安全
- **Element Plus**: UI 组件库
- **Vue Router**: 路由管理
- **Pinia**: 状态管理
- **Axios**: HTTP 请求库
- **Vue ECharts**: 数据可视化
- **Tailwind CSS**: 原子化 CSS 框架

## 项目结构

```
qianduan/
├── src/
│   ├── api/              # API 接口封装
│   │   ├── auth.ts       # 认证接口
│   │   ├── user.ts       # 用户接口
│   │   ├── attraction.ts # 景点接口
│   │   ├── ticket.ts     # 门票接口
│   │   ├── order.ts      # 订单接口
│   │   ├── guide.ts      # 攻略接口
│   │   ├── comment.ts    # 评论接口
│   │   ├── banner.ts     # Banner 接口
│   │   ├── statistics.ts # 统计接口
│   │   └── system.ts     # 系统配置接口
│   ├── assets/           # 静态资源
│   ├── components/       # 公共组件
│   ├── layout/           # 布局组件
│   │   └── index.vue     # 主布局（侧边栏+顶部栏）
│   ├── router/           # 路由配置
│   │   └── index.ts      # 路由定义
│   ├── store/            # 状态管理
│   │   ├── auth.ts       # 认证状态
│   │   ├── app.ts        # 应用状态
│   │   └── index.ts      # Store 导出
│   ├── types/            # TypeScript 类型定义
│   │   └── index.ts      # 全局类型
│   ├── utils/            # 工具函数
│   │   └── request.ts    # Axios 封装
│   ├── views/            # 页面组件
│   │   ├── login/        # 登录页面
│   │   ├── dashboard/    # 数据总览
│   │   ├── users/        # 用户管理
│   │   ├── attractions/  # 景点管理
│   │   ├── orders/       # 订单管理
│   │   ├── guides/       # 攻略管理
│   │   ├── comments/     # 评论管理
│   │   ├── banners/      # Banner 管理
│   │   └── system/       # 系统管理
│   ├── App.vue           # 根组件
│   ├── index.css         # 全局样式
│   └── main.ts           # 入口文件
├── public/               # 公共资源
├── index.html            # HTML 模板
├── package.json          # 项目配置
├── tailwind.config.js    # Tailwind 配置
├── postcss.config.js     # PostCSS 配置
├── tsconfig.json         # TypeScript 配置
├── vite.config.ts        # Vite 配置
└── README.md             # 说明文档
```

## 功能模块

### 1. 认证模块
- 管理员登录
- 权限验证
- Token 管理

### 2. 用户管理
- 用户列表（分页、搜索、筛选）
- 用户详情查看
- 用户状态管理
- 用户订单查询

### 3. 景点管理
- 景点列表（分页、搜索、筛选）
- 景点创建/编辑
- 景点详情查看
- 景点状态管理
- 景点排序

### 4. 订单管理
- 订单列表（分页、搜索、筛选）
- 订单详情查看
- 订单状态更新
- 订单统计

### 5. 攻略管理
- 攻略列表（分页、搜索、筛选）
- 攻略详情查看
- 攻略审核（通过/拒绝）
- 攻略状态管理

### 6. 评论管理
- 评论列表（分页、搜索、筛选）
- 评论状态管理
- 批量删除评论

### 7. Banner 管理
- Banner 列表
- Banner 创建/编辑
- Banner 状态管理
- Banner 排序

### 8. 数据统计
- 数据总览（用户、订单、景点、攻略）
- 用户增长趋势图
- 订单统计图
- 各模块数据统计

### 9. 系统管理
- 系统配置管理
- 管理员管理
- 密码重置

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

前端服务将运行在 `http://localhost:3001`

### 访问系统

打开浏览器访问 `http://localhost:3001`，默认管理员账号：
- 用户名: `admin`
- 密码: `admin123`

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 配置说明

### 环境配置

在 `vite.config.ts` 中配置了代理，所有 `/api` 请求将转发到后端服务器：

```typescript
server: {
  host: '0.0.0.0',
  port: 3001,
  proxy: {
    '/api': {
      target: 'http://localhost:8082',
      changeOrigin: true,
    },
  },
}
```

### 后端 API

后端服务应该运行在 `http://localhost:8082`

**重要**: 确保后端服务已启动，在项目根目录运行：

```bash
cd demo
./start.bat  # Windows
# 或
./start.sh   # Linux/Mac
```

## API 接口

所有 API 接口都封装在 `src/api/` 目录下，每个模块对应一个文件：

- `auth.ts`: 认证相关接口
- `user.ts`: 用户管理接口
- `attraction.ts`: 景点管理接口
- `ticket.ts`: 门票管理接口
- `order.ts`: 订单管理接口
- `guide.ts`: 攻略管理接口
- `comment.ts`: 评论管理接口
- `banner.ts`: Banner 管理接口
- `statistics.ts`: 统计接口
- `system.ts`: 系统配置接口

## 类型定义

所有 TypeScript 类型定义都在 `src/types/index.ts` 文件中，包括：

- `ApiResponse<T>`: 统一 API 响应格式
- `PageRequest`: 分页请求参数
- `PageData<T>`: 分页响应数据
- `User`: 用户实体
- `Attraction`: 景点实体
- `TicketType`: 门票类型实体
- `Order`: 订单实体
- `Guide`: 攻略实体
- `Comment`: 评论实体
- `Banner`: Banner 实体
- `Admin`: 管理员实体
- `SystemConfig`: 系统配置实体

## 状态管理

使用 Pinia 进行状态管理，定义了两个主要的 Store：

### Auth Store (`src/store/auth.ts`)
- 用户登录/登出
- Token 管理
- 用户信息管理

### App Store (`src/store/app.ts`)
- 侧边栏折叠状态
- 主题切换
- 全局加载状态

## 路由配置

路由配置在 `src/router/index.ts` 文件中，包含了所有页面的路由定义和权限控制。

## 开发说明

### 添加新页面

1. 在 `src/views/` 下创建新的页面组件
2. 在 `src/router/index.ts` 中添加路由配置
3. 如需 API 调用，在 `src/api/` 下创建对应的 API 文件

### 添加新类型

在 `src/types/index.ts` 中添加新的类型定义。

### 添加新 API

在 `src/api/` 目录下创建对应的 API 文件，使用封装好的请求方法（`get`, `post`, `put`, `del`）。

## 注意事项

1. 所有 API 请求都会自动添加 Token（如果已登录）
2. Token 存储在 localStorage 中
3. 401 错误会自动跳转到登录页
4. 所有页面都有权限控制，需要登录才能访问
5. **确保后端服务运行在 http://localhost:8082**
6. **前端服务运行在 http://localhost:3001**

## 快速启动脚本

项目根目录提供了两个批处理脚本：

- `start.bat`: 启动后端服务
- `start-frontend.bat`: 启动前端开发服务器

依次运行这两个脚本即可启动完整系统。

## 许可证

MIT
