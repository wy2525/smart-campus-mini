# 旅游小程序API对接文档

## 1. 项目概述

本旅游小程序前端基于uni-app框架开发，需要与后端RESTful API进行数据交互。本文档详细列出了小程序所需的所有API接口及其使用方法。

## 2. 基础配置

### 2.1 接口域名
- **开发环境**: `http://localhost:8082/api/public`
- **生产环境**: 待配置

### 2.2 请求头
- `Content-Type`: `application/json;charset=UTF-8`
- `Authorization`: `Bearer {token}` (部分接口需要认证)

### 2.3 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-18 10:00:00"
}
```

## 3. 用户相关接口

### 3.1 用户登录
- **接口**: `POST /user/login`
- **描述**: 用户登录获取token
- **请求参数**:
```json
{
  "username": "用户名或手机号",
  "password": "密码"
}
```
- **响应参数**:
```json
{
  "token": "JWT令牌",
  "userInfo": {
    "id": 1,
    "nickname": "用户名",
    "avatar": "头像URL",
    "phone": "手机号"
  }
}
```

### 3.2 用户注册
- **接口**: `POST /user/register`
- **描述**: 用户注册
- **请求参数**:
```json
{
  "nickname": "用户名",
  "phone": "手机号",
  "password": "密码",
  "confirmPassword": "确认密码"
}
```
- **响应参数**:
```json
{
  "message": "注册成功"
}
```

### 3.3 获取用户信息
- **接口**: `GET /user/info`
- **描述**: 获取指定用户信息
- **请求参数**:
```json
{
  "userId": "用户ID"
}
```
- **响应参数**:
```json
{
  "id": 1,
  "nickname": "用户名",
  "avatar": "头像URL",
  "phone": "手机号",
  "gender": 1,
  "created_at": "创建时间"
}
```

### 3.4 更新用户信息
- **接口**: `PUT /user/info`
- **描述**: 更新用户信息
- **请求参数**:
```json
{
  "nickname": "用户名",
  "avatar": "头像URL",
  "phone": "手机号",
  "gender": 1
}
```
- **响应参数**:
```json
{
  "message": "更新成功"
}
```

## 4. 景点相关接口

### 4.1 获取景点列表
- **接口**: `GET /attractions`
- **描述**: 获取景点列表（支持分页和筛选）
- **请求参数**:
```json
{
  "page": 1,
  "limit": 10,
  "type": "景点类型（cultural/natural/theme等）",
  "keyword": "关键词搜索"
}
```
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "name": "景点名称",
      "cover_image": "封面图URL",
      "min_price": 40.00,
      "average_rating": 4.8,
      "tags": ["标签1", "标签2"],
      "address": "地址",
      "category": "分类"
    }
  ],
  "total": 100,
  "page": 1,
  "limit": 10
}
```

### 4.2 获取热门景点
- **接口**: `GET /attractions/hot`
- **描述**: 获取热门景点列表
- **请求参数**:
```json
{
  "limit": 8
}
```
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "name": "景点名称",
      "cover_image": "封面图URL",
      "min_price": 40.00,
      "average_rating": 4.8,
      "tags": ["标签1", "标签2"]
    }
  ]
}
```

### 4.3 获取推荐景点
- **接口**: `GET /attractions/recommend`
- **描述**: 获取推荐景点列表
- **请求参数**:
```json
{
  "limit": 10
}
```
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "name": "景点名称",
      "cover_image": "封面图URL",
      "min_price": 40.00,
      "average_rating": 4.8,
      "tags": ["标签1", "标签2"]
    }
  ]
}
```

### 4.4 获取景点详情
- **接口**: `GET /attractions/{id}`
- **描述**: 获取单个景点详情
- **请求参数**: URL路径参数 `id` - 景点ID
- **响应参数**:
```json
{
  "id": 1,
  "name": "景点名称",
  "description": "景点介绍",
  "cover_image": "封面图URL",
  "images": ["图片URL1", "图片URL2"],
  "address": "地址",
  "latitude": 39.9163,
  "longitude": 116.3972,
  "min_price": 40.00,
  "average_rating": 4.8,
  "tags": ["标签1", "标签2"],
  "opening_hours": "开放时间",
  "notices": "注意事项",
  "phone": "联系电话",
  "category": "分类",
  "view_count": 1234
}
```

### 4.5 获取景点门票类型
- **接口**: `GET /attractions/{id}/tickets`
- **描述**: 获取景点门票类型列表
- **请求参数**: URL路径参数 `id` - 景点ID
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "name": "门票名称",
      "description": "门票描述",
      "price": 40.00,
      "stock": 100,
      "status": 1
    }
  ]
}
```

## 5. 订单相关接口

### 5.1 创建订单
- **接口**: `POST /orders`
- **描述**: 创建新订单
- **请求参数**:
```json
{
  "attraction_id": 1,
  "ticket_type_id": 1,
  "quantity": 2,
  "unit_price": 40.00,
  "total_price": 80.00,
  "visit_date": "2024-01-20",
  "passenger_name": "游客姓名",
  "passenger_id_card": "身份证号",
  "passenger_phone": "手机号"
}
```
- **响应参数**:
```json
{
  "order_no": "订单号",
  "message": "创建成功"
}
```

### 5.2 获取我的订单
- **接口**: `GET /orders/my`
- **描述**: 获取当前用户的订单列表
- **请求参数**:
```json
{
  "page": 1,
  "limit": 10,
  "status": "订单状态（unpaid/paid/used/completed/refunded/cancelled）"
}
```
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "order_no": "订单号",
      "attraction_id": 1,
      "attraction_name": "景点名称",
      "attraction_image": "景点图片",
      "ticket_type": "门票类型",
      "quantity": 2,
      "total_price": 80.00,
      "order_time": "订单时间",
      "status": "unpaid",
      "status_text": "待支付"
    }
  ],
  "total": 10,
  "page": 1,
  "limit": 10
}
```

### 5.3 获取订单详情
- **接口**: `GET /orders/{id}`
- **描述**: 获取订单详情
- **请求参数**: URL路径参数 `id` - 订单ID
- **响应参数**:
```json
{
  "id": 1,
  "order_no": "订单号",
  "attraction_name": "景点名称",
  "attraction_image": "景点图片",
  "ticket_type": "门票类型",
  "quantity": 2,
  "unit_price": 40.00,
  "total_price": 80.00,
  "visit_date": "游玩日期",
  "passenger_name": "游客姓名",
  "passenger_id_card": "身份证号",
  "passenger_phone": "手机号",
  "status": "unpaid",
  "status_text": "待支付",
  "created_at": "创建时间"
}
```

### 5.4 取消订单
- **接口**: `PUT /orders/{id}/cancel`
- **描述**: 取消订单
- **请求参数**: URL路径参数 `id` - 订单ID
- **响应参数**:
```json
{
  "message": "取消成功"
}
```

## 6. 攻略相关接口

### 6.1 获取攻略列表
- **接口**: `GET /guides`
- **描述**: 获取攻略列表
- **请求参数**:
```json
{
  "page": 1,
  "limit": 10,
  "attraction_id": "关联景点ID（可选）",
  "keyword": "关键词搜索（可选）"
}
```
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "title": "攻略标题",
      "summary": "摘要",
      "cover_image": "封面图",
      "user_id": 1,
      "user_name": "用户名",
      "user_avatar": "用户头像",
      "post_time": "发布时间",
      "likes": 128,
      "comments": 36,
      "favorites": 89,
      "liked": false,
      "favorited": false
    }
  ],
  "total": 100,
  "page": 1,
  "limit": 10
}
```

### 6.2 获取攻略详情
- **接口**: `GET /guides/{id}`
- **描述**: 获取攻略详情
- **请求参数**: URL路径参数 `id` - 攻略ID
- **响应参数**:
```json
{
  "id": 1,
  "title": "攻略标题",
  "content": "攻略内容",
  "cover_image": "封面图",
  "images": ["图片1", "图片2"],
  "user_id": 1,
  "user_name": "用户名",
  "user_avatar": "用户头像",
  "attraction_id": 1,
  "attraction_name": "关联景点名称",
  "view_count": 123,
  "like_count": 128,
  "comment_count": 36,
  "collect_count": 89,
  "liked": false,
  "favorited": false,
  "post_time": "发布时间"
}
```

### 6.3 发布攻略
- **接口**: `POST /guides`
- **描述**: 发布新攻略
- **请求参数**:
```json
{
  "title": "攻略标题",
  "summary": "攻略摘要",
  "content": "攻略内容",
  "cover_image": "封面图URL",
  "images": ["图片URL1", "图片URL2"],
  "attraction_id": "关联景点ID（可选）"
}
```
- **响应参数**:
```json
{
  "message": "发布成功"
}
```

### 6.4 点赞攻略
- **接口**: `POST /guides/{id}/like`
- **描述**: 点赞攻略
- **请求参数**: URL路径参数 `id` - 攻略ID
- **响应参数**:
```json
{
  "message": "点赞成功",
  "like_count": 129
}
```

### 6.5 收藏攻略
- **接口**: `POST /guides/{id}/favorite`
- **描述**: 收藏攻略
- **请求参数**: URL路径参数 `id` - 攻略ID
- **响应参数**:
```json
{
  "message": "收藏成功",
  "collect_count": 90
}
```

## 7. 评论相关接口

### 7.1 获取攻略评论
- **接口**: `GET /comments`
- **描述**: 获取攻略评论列表
- **请求参数**:
```json
{
  "guideId": 1,
  "page": 1,
  "limit": 10
}
```
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "guide_id": 1,
      "user_id": 1,
      "user_name": "用户名",
      "user_avatar": "用户头像",
      "content": "评论内容",
      "parent_id": 0,
      "like_count": 5,
      "liked": false,
      "created_at": "创建时间"
    }
  ],
  "total": 10,
  "page": 1,
  "limit": 10
}
```

### 7.2 发表评论
- **接口**: `POST /comments`
- **描述**: 发表评论
- **请求参数**:
```json
{
  "guide_id": 1,
  "content": "评论内容",
  "parent_id": 0
}
```
- **响应参数**:
```json
{
  "message": "评论成功"
}
```

### 7.3 点赞评论
- **接口**: `POST /comments/{id}/like`
- **描述**: 点赞评论
- **请求参数**: URL路径参数 `id` - 评论ID
- **响应参数**:
```json
{
  "message": "点赞成功"
}
```

## 8. Banner相关接口

### 8.1 获取轮播图
- **接口**: `GET /banners`
- **描述**: 获取首页轮播图
- **响应参数**:
```json
{
  "list": [
    {
      "id": 1,
      "title": "标题",
      "image_url": "图片URL",
      "attraction_id": 1,
      "url": "跳转链接",
      "sort_order": 1
    }
  ]
}
```

## 9. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，请登录 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 10. 前后端联调说明

### 10.1 跨域配置
后端需要配置CORS允许以下域名访问：
- `http://localhost:8080` (HBuilderX调试域名)
- 微信开发者工具域名

### 10.2 Token处理
- 登录成功后，前端将token存储到本地缓存
- 后续请求自动携带Authorization头部
- token过期时返回401状态码，前端引导用户重新登录

### 10.3 数据格式约定
- 时间格式统一使用 `YYYY-MM-DD HH:mm:ss`
- 金额统一使用DECIMAL类型，保留两位小数
- 图片URL统一使用HTTPS协议