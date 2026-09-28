# 旅游小程序后台管理系统

## 项目简介

这是一个为旅游小程序提供后台管理功能的完整系统，包括前端Vue3管理后台和后端Spring Boot服务。系统支持用户管理、景点管理、订单管理、攻略管理、评论管理、Banner管理和数据统计等功能。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 3.x
- **数据库**: MySQL
- **ORM**: Spring Data JPA
- **构建工具**: Maven

### 前端技术栈
- **框架**: Vue 3 (Composition API)
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **UI组件库**: Element Plus
- **HTTP客户端**: Axios
- **图表库**: ECharts
- **日期处理**: Day.js
- **构建工具**: Vite

## 项目结构

```
旅游/后端管理系统/
├── demo/                          # 后端项目
│   ├── src/
│   │   ├── main/java/com/example/demo/
│   │   │   ├── controller/        # 控制器层
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AttractionController.java
│   │   │   │   ├── CommentController.java
│   │   │   │   ├── GuideController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── PublicController.java
│   │   │   │   ├── StatisticsController.java
│   │   │   │   └── UserController.java
│   │   │   ├── entity/            # 实体类
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Attraction.java
│   │   │   │   ├── Banner.java
│   │   │   │   ├── Comment.java
│   │   │   │   ├── Guide.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── SystemConfig.java
│   │   │   │   ├── TicketType.java
│   │   │   │   └── User.java
│   │   │   ├── repository/        # 数据访问层
│   │   │   │   ├── AdminRepository.java
│   │   │   │   ├── AttractionRepository.java
│   │   │   │   ├── BannerRepository.java
│   │   │   │   ├── CommentRepository.java
│   │   │   │   ├── GuideRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── SystemConfigRepository.java
│   │   │   │   ├── TicketTypeRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/           # 服务层
│   │   │   │   ├── AdminService.java
│   │   │   │   ├── AttractionService.java
│   │   │   │   ├── BannerService.java
│   │   │   │   ├── CommentService.java
│   │   │   │   ├── GuideService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── StatisticsService.java
│   │   │   │   └── UserService.java
│   │   │   ├── common/            # 公共模块
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResultCode.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── pom.xml
│
├── qianduan/                       # 前端项目
│   ├── src/
│   │   ├── api/                    # API接口
│   │   │   ├── request.js          # Axios封装
│   │   │   ├── auth.js
│   │   │   ├── attraction.js
│   │   │   ├── banner.js
│   │   │   ├── comment.js
│   │   │   ├── guide.js
│   │   │   ├── order.js
│   │   │   ├── statistics.js
│   │   │   ├── system.js
│   │   │   └── user.js
│   │   ├── components/            # 公共组件
│   │   │   ├── charts/            # 图表组件
│   │   │   │   ├── BarChart.vue
│   │   │   │   ├── LineChart.vue
│   │   │   │   ├── PieChart.vue
│   │   │   │   └── StatCard.vue
│   │   │   └── common/            # 通用组件
│   │   │       └── StatusTag.vue
│   │   ├── router/                # 路由配置
│   │   │   └── index.js
│   │   ├── store/                 # 状态管理
│   │   │   ├── modules/
│   │   │   │   ├── app.js
│   │   │   │   └── user.js
│   │   │   └── index.js
│   │   ├── utils/                 # 工具函数
│   │   │   ├── format.js
│   │   │   ├── permission.js
│   │   │   └── validate.js
│   │   ├── views/                 # 页面组件
│   │   │   ├── banners/           # Banner管理
│   │   │   ├── comments/          # 评论管理
│   │   │   ├── dashboard/         # 数据总览
│   │   │   ├── guides/            # 攻略管理
│   │   │   ├── layout/            # 布局组件
│   │   │   ├── login/             # 登录页面
│   │   │   ├── orders/            # 订单管理
│   │   │   ├── system/            # 系统管理
│   │   │   ├── attractions/       # 景点管理
│   │   │   └── users/             # 用户管理
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── 后台管理系统设计.md              # 详细设计文档
├── API接口文档.md                  # API接口说明
└── README.md                       # 项目说明文档
```

## 功能模块

### 1. 数据总览 (Dashboard)
- 核心数据统计卡片（用户数、订单数、景点数、攻略数）
- 用户增长趋势图
- 订单统计图
- 景点热度排行
- 最新订单列表
- 最新攻略列表

### 2. 用户管理
- 用户列表展示（分页、搜索、筛选）
- 用户详情查看
- 用户启用/禁用
- 查看用户订单
- 查看用户发布攻略

### 3. 景点管理
- 景点列表（分页、搜索、筛选）
- 景点新增/编辑/删除
- 景点详情查看
- 景点上线/下线
- 门票类型管理
- 门票库存管理

### 4. 订单管理
- 订单列表（分页、搜索、筛选）
- 订单详情查看
- 订单状态更新
- 订单统计分析
- 订单数据导出

### 5. 攻略管理
- 攻略列表（分页、搜索、筛选）
- 攻略审核（通过/拒绝）
- 攻略详情查看
- 攻略删除

### 6. 评论管理
- 评论列表（分页、搜索）
- 评论删除
- 批量删除

### 7. Banner管理
- Banner列表
- Banner新增/编辑/删除
- Banner排序
- Banner上线/下线

### 8. 系统管理
- 系统配置查看和修改
- 管理员管理
- 管理员权限设置

## 快速开始

### 后端启动

1. **配置数据库**
   
   修改 `demo/src/main/resources/application.properties` 文件：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tourism?useSSL=false&serverTimezone=Asia/Shanghai
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE tourism CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **启动后端服务**
   ```bash
   cd demo
   mvn spring-boot:run
   ```

   后端服务将在 `http://localhost:8082` 启动

### 前端启动

1. **安装依赖**
   ```bash
   cd qianduan
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```

   前端服务将在 `http://localhost:3001` 启动

3. **访问系统**

   打开浏览器访问 `http://localhost:3001`
   
   默认管理员账号：
   - 用户名: admin
   - 密码: admin123

## API接口说明

### 认证相关
- `POST /api/admin/login` - 管理员登录
- `POST /api/admin/logout` - 管理员登出

### 用户管理
- `GET /api/admin/users` - 获取用户列表
- `GET /api/admin/users/:id` - 获取用户详情
- `PUT /api/admin/users/:id/status` - 更新用户状态
- `DELETE /api/admin/users/:id` - 删除用户

### 景点管理
- `GET /api/admin/attractions` - 获取景点列表
- `POST /api/admin/attractions` - 创建景点
- `PUT /api/admin/attractions/:id` - 更新景点
- `DELETE /api/admin/attractions/:id` - 删除景点
- `GET /api/admin/attractions/:id/tickets` - 获取景点门票类型

### 订单管理
- `GET /api/admin/orders` - 获取订单列表
- `GET /api/admin/orders/:id` - 获取订单详情
- `PUT /api/admin/orders/:id/status` - 更新订单状态
- `GET /api/admin/orders/statistics` - 订单统计数据

### 攻略管理
- `GET /api/admin/guides` - 获取攻略列表
- `PUT /api/admin/guides/:id/approve` - 审核通过
- `PUT /api/admin/guides/:id/reject` - 审核拒绝
- `DELETE /api/admin/guides/:id` - 删除攻略

### 评论管理
- `GET /api/admin/comments` - 获取评论列表
- `DELETE /api/admin/comments/:id` - 删除评论

### Banner管理
- `GET /api/admin/banners` - 获取Banner列表
- `POST /api/admin/banners` - 创建Banner
- `PUT /api/admin/banners/:id` - 更新Banner
- `DELETE /api/admin/banners/:id` - 删除Banner

### 数据统计
- `GET /api/admin/statistics/overview` - 总览数据统计
- `GET /api/admin/statistics/users` - 用户统计
- `GET /api/admin/statistics/orders` - 订单统计
- `GET /api/admin/statistics/attractions` - 景点统计

### 系统管理
- `GET /api/admin/config` - 获取系统配置
- `PUT /api/admin/config` - 更新系统配置

详细接口文档请参考 `API接口文档.md`

## 订单状态说明

订单状态流转：
```
unpaid (未支付) → toUse (待使用) → used (已使用) → completed (已完成)
                   ↓
                refunded (已退款)
                   ↓
                cancelled (已取消)
```

状态说明：
- **unpaid**: 待支付（用户下单后，未完成支付）
- **toUse**: 待使用（已支付，未使用）
- **used**: 已使用（用户已使用门票）
- **completed**: 已完成（已完成评价或流程结束）
- **refunded**: 已退款
- **cancelled**: 已取消（未支付超时取消）

## 开发注意事项

1. **数据库配置**
   - 确保MySQL服务已启动
   - 创建对应的数据库
   - 配置正确的数据库连接信息

2. **API接口**
   - 前端通过 `/api` 前缀访问后端接口
   - Vite已配置代理，将 `/api` 请求转发到 `http://localhost:8080`

3. **状态管理**
   - 使用Pinia进行全局状态管理
   - 用户信息存储在 `user` 模块
   - 应用状态存储在 `app` 模块

4. **路由权限**
   - 所有路由需要登录验证
   - 未登录用户自动跳转到登录页
   - 使用路由守卫进行权限控制

5. **响应式设计**
   - 系统采用响应式布局
   - 支持不同屏幕尺寸适配

## 项目特色

1. **完整的前后端分离架构**
   - 后端提供RESTful API
   - 前端采用现代化Vue3框架

2. **丰富的数据可视化**
   - 集成ECharts图表库
   - 提供多种图表类型（折线图、柱状图、饼图）

3. **完善的权限管理**
   - 基于Token的身份认证
   - 路由级别的权限控制

4. **友好的用户界面**
   - 采用Element Plus组件库
   - 现代化的UI设计
   - 流畅的交互体验

5. **完整的功能模块**
   - 覆盖旅游小程序后台管理的所有核心功能
   - 模块化设计，易于扩展

## 常见问题

### Q: 后端启动失败？
A: 检查以下几点：
- MySQL服务是否启动
- 数据库是否创建
- 数据库连接配置是否正确
- 端口8080是否被占用

### Q: 前端无法访问后端API？
A: 检查以下几点：
- 后端服务是否正常启动
- Vite代理配置是否正确
- 浏览器控制台是否有CORS错误

### Q: 登录失败？
A: 检查以下几点：
- 数据库中是否有管理员账号
- 用户名和密码是否正确
- 后端日志中是否有错误信息

## 更新日志

### v1.0 (2026-01-18)
- ✅ 完成后端所有功能模块
- ✅ 完成前端所有页面和组件
- ✅ 实现完整的CRUD操作
- ✅ 实现数据统计和图表展示
- ✅ 实现权限管理和路由守卫

## 许可证

本项目仅供学习和研究使用。

## 联系方式

如有问题或建议，请联系项目维护者。

---

**文档生成时间：** 2026-01-18
**项目版本：** v1.0

![image-20260118150415804](C:\Users\王洋\AppData\Roaming\Typora\typora-user-images\image-20260118150415804.png)

![image-20260118150420133](C:\Users\王洋\AppData\Roaming\Typora\typora-user-images\image-20260118150420133.png)
