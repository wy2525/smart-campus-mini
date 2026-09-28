# API接口文档

## 基础信息

### 接口地址
- **基础URL**：`https://api.travel.com`
- **版本**：v1
- **认证方式**：JWT Token

### 请求格式
- **请求方法**：GET/POST/PUT/DELETE
- **请求头**：
  - `Content-Type: application/json`
  - `Authorization: Bearer {token}`
- **请求参数**：JSON格式
- **响应格式**：JSON格式

### 统一响应格式
```json
{
  "code": 0,
  "message": "成功",
  "data": {},
  "timestamp": 1640995200000
}
```

```mermaid
sequenceDiagram
    participant 用户
    participant 小程序
    participant API网关
    participant 后端服务
    participant 数据库
    
    用户->>小程序： 发起请求
    小程序->>API网关： 转发请求
    API网关->>后端服务： 验证并路由
    后端服务->>数据库： 查询/操作数据
    数据库-->>后端服务： 返回数据
    后端服务-->>API网关： 返回响应
    API网关-->>小程序： 转发响应
    小程序-->>用户： 展示结果
```

## 用户相关接口

### 1. 用户登录
- **接口地址**：`/api/v1/user/login`
- **请求方法**：POST
- **请求参数**：
```json
{
  "phone": "13800138000",
  "code": "123456"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "phone": "13800138000",
      "nickname": "游客",
      "avatar": "https://example.com/avatar.jpg"
    }
  },
  "timestamp": 1640995200000
}
```

### 2. 获取用户信息
- **接口地址**：`/api/v1/user/info`
- **请求方法**：GET
- **请求参数**：无
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "id": 1,
    "phone": "13800138000",
    "nickname": "游客",
    "avatar": "https://example.com/avatar.jpg",
    "gender": "male",
    "age": 25
  },
  "timestamp": 1640995200000
}
```

### 3. 更新用户信息
- **接口地址**：`/api/v1/user/update`
- **请求方法**：PUT
- **请求参数**：
```json
{
  "nickname": "新的昵称",
  "gender": "male",
  "age": 26
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "更新成功",
  "data": {},
  "timestamp": 1640995200000
}
```

## 景点相关接口

### 1. 获取景点列表
- **接口地址**：`/api/v1/attraction/list`
- **请求方法**：GET
- **请求参数**：
```json
{
  "page": 1,
  "size": 10,
  "keyword": "西湖",
  "region": "杭州",
  "category": "自然风光",
  "sortBy": "rating",
  "sortOrder": "desc"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "name": "西湖",
        "region": "杭州",
        "category": "自然风光",
        "rating": 4.8,
        "price": 80,
        "coverImage": "https://example.com/west-lake.jpg",
        "description": "西湖，位于浙江省杭州市西湖区，是中国大陆首批国家重点风景名胜区..."
      }
    ]
  },
  "timestamp": 1640995200000
}
```

### 2. 获取景点详情
- **接口地址**：`/api/v1/attraction/detail`
- **请求方法**：GET
- **请求参数**：
```json
{
  "id": 1
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "id": 1,
    "name": "西湖",
    "region": "杭州",
    "category": "自然风光",
    "rating": 4.8,
    "price": 80,
    "coverImage": "https://example.com/west-lake.jpg",
    "description": "西湖，位于浙江省杭州市西湖区，是中国大陆首批国家重点风景名胜区...",
    "openTime": "08:00-18:00",
    "address": "浙江省杭州市西湖区",
    "tags": ["自然风光", "历史古迹", "5A景区"],
    "images": [
      "https://example.com/west-lake-1.jpg",
      "https://example.com/west-lake-2.jpg"
    ]
  },
  "timestamp": 1640995200000
}
```

### 3. 获取景点门票类型
- **接口地址**：`/api/v1/attraction/ticket-types`
- **请求方法**：GET
- **请求参数**：
```json
{
  "attractionId": 1
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "name": "成人票",
      "price": 80,
      "description": "适用于成人游客"
    },
    {
      "id": 2,
      "name": "儿童票",
      "price": 40,
      "description": "适用于1.2米-1.5米儿童"
    },
    {
      "id": 3,
      "name": "学生票",
      "price": 40,
      "description": "适用于学生群体"
    }
  ],
  "timestamp": 1640995200000
}
```

## 订单相关接口

### 1. 创建订单
- **接口地址**：`/api/v1/order/create`
- **请求方法**：POST
- **请求参数**：
```json
{
  "attractionId": 1,
  "ticketTypeId": 1,
  "quantity": 2,
  "contactName": "张三",
  "contactPhone": "13800138000"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "创建成功",
  "data": {
    "orderId": "ORDER202312280001",
    "totalPrice": 160,
    "orderTime": "2023-12-28 10:00:00"
  },
  "timestamp": 1640995200000
}
```

### 2. 获取订单列表
- **接口地址**：`/api/v1/order/list`
- **请求方法**：GET
- **请求参数**：
```json
{
  "page": 1,
  "size": 10,
  "status": "all"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "total": 5,
    "list": [
      {
        "id": "ORDER202312280001",
        "attractionName": "西湖",
        "totalPrice": 160,
        "status": "待支付",
        "createTime": "2023-12-28 10:00:00",
        "expireTime": "2023-12-28 11:00:00"
      }
    ]
  },
  "timestamp": 1640995200000
}
```

### 3. 获取订单详情
- **接口地址**：`/api/v1/order/detail`
- **请求方法**：GET
- **请求参数**：
```json
{
  "orderId": "ORDER202312280001"
}
```
- **响应示例**：
```json
{
 maid
  "code": 0,
  "message": "获取成功",
  "data": {
    "id": "ORDER202312280001",
    "attractionName": "西湖",
    "attractionImage": "https://example.com/west-lake.jpg",
    "totalPrice": 160,
    "status": "待支付",
    "createTime": "2023-12-28 10:00:00",
    "expireTime": "2023-12-28 11:00:00",
    "contactName": "张三",
    "contactPhone": "13800138000",
    "ticketInfo": [
      {
        "type": "成人票",
        "price": 80,
        "quantity": 2
      }
    ]
  },
  "timestamp": 1640995200000
}
```

### 4. 取消订单
- **接口地址**：`/api/v1/order/cancel`
- **请求方法**：POST
- **请求参数**：
```json
{
  "orderId": "ORDER202312280001"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "取消成功",
  "data": {},
  "timestamp": 1640995200000
}
```

## 攻略相关接口

### 1. 获取攻略列表
- **接口地址**：`/api/v1/guide/list`
- **请求方法**：GET
- **请求参数**：
```json
{
  "page": 1,
  "size": 10,
  "keyword": "西湖",
  "category": "景点攻略",
  "sortBy": "createTime",
  "sortOrder": "desc"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "title": "西湖一日游完整攻略",
        "author": "旅行达人",
        "createTime": "2023-12-28 09:00:00",
        "viewCount": 1000,
        "likeCount": 50,
        "summary": "西湖一日游完整攻略，包含必去景点、最佳路线和美食推荐..."
      }
    ]
  },
  "timestamp": 1640995200000
}
```

### 2. 获取攻略详情
- **接口地址**：`/api/v1/guide/detail`
- **请求方法**：GET
- **请求参数**：
```json
{
  "id": 1
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "西湖一日游完整攻略",
    "author": "旅行达人",
    "createTime": "2023-12-28 09:00:00",
    "viewCount": 1000,
    "likeCount": 50,
    "content": "西湖一日游完整攻略，包含必去景点、最佳路线和美食推荐...",
    "images": [
      "https://example.com/guide-1.jpg",
      "https://example.com/guide-2.jpg"
    ]
  },
  "timestamp": 1640995200000
}
```

### 3. 发布攻略
- **接口地址**：`/api/v1/guide/publish`
- **请求方法**：POST
- **请求参数**：
```json
{
  "title": "西湖一日游完整攻略",
  "content": "西湖一日游完整攻略，包含必去景点、最佳路线和美食推荐...",
  "category": "景点攻略",
  "images": ["https://example.com/guide-1.jpg", "https://example.com/guide-2.jpg"]
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "发布成功",
  "data": {
    "guideId": 1
  },
  "timestamp": 1640995200000
}
```

## AI助手相关接口

### 1. 发送消息
- **接口地址**：`/api/v1/ai/chat`
- **请求方法**：POST
- **请求参数**：
```json
{
  "message": "推荐一些杭州的景点"
}
```
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": {
    "reply": "杭州有很多著名的景点，推荐您去西湖、灵隐寺、宋城等...",
    "suggestions": [
      {
        "title": "西湖",
        "description": "杭州最著名的景点，必去之地",
        "link": "/attraction/1"
      }
    ]
  },
  "timestamp": 1640995200000
}
```

### 2. 获取聊天历史
- **接口地址**：`/api/v1/ai/history`
- **请求方法**：GET
- **请求参数**：无
- **响应示例**：
```json
{
  "code": 0,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "message": "推荐一些杭州的景点",
      "reply": "杭州有很多著名的景点，推荐您去西湖、灵隐寺、宋城等...",
      "createTime": "2023-12-28 10:00:00"
    }
  ],
  "timestamp": 1640995200000
}
```

## 错误码说明

| 错误码 | 说明 | HTTP状态码 |
|--------|------|------------|
| 0 | 成功 | 200 |
| 1001 | 参数错误 | 400 |
| 1002 | 用户未登录 | 401 |
| 1003 | 权限不足 | 403 |
| 1004 | 资源不存在 | 404 |
| 1005 | 请求过于频繁 | 429 |
| 2001 | 用户名或密码错误 | 400 |
| 2002 | 验证码错误 | 400 |
| 3001 | 景点不存在 | 400 |
| 3002 | 门票类型不存在 | 400 |
| 3003 | 库存不足 | 400 |
| 4001 | 订单创建失败 | 400 |
| 4002 | 订单已过期 | 400 |
| 5001 | 系统内部错误 | 500 |

## 注意事项

1. 所有接口都需要在请求头中携带有效的Token
2. 请求参数需要按照接口文档格式传递
3. 响应数据中的timestamp为Unix时间戳（毫秒级）
4. 错误码和错误信息会在响应的code和message字段中返回
5. 对于分页接口，page参数从1开始，size参数建议不超过50