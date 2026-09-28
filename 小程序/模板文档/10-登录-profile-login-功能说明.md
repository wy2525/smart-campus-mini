# 登录模板 - pages/profile/login

## 页面概述
登录页面是用户登录小程序的入口，支持手机号密码登录和微信一键登录，提供忘记密码和注册账号等功能。

## 页面布局

```
┌─────────────────────────────────┐
│                                 │
│       欢迎回来                   │
│     请登录您的账号               │
│                                 │
├─────────────────────────────────┤
│  手机号                         │
│  ┌─────────────────────────────┐ │
│  │ 请输入手机号                │ │
│  └─────────────────────────────┘ │
│                                 │
│  密码                           │
│  ┌─────────────────────────────┐ │
│  │ 请输入密码        [👁]       │ │
│  └─────────────────────────────┘ │
│                                 │
│       [登录]                    │
│                                 │
│           忘记密码？             │
├─────────────────────────────────┤
│           其他登录方式           │
│  ──────────────────────────    │
│                                 │
│  >WeChat                        │
│  微信一键登录                   │
│                                 │
├─────────────────────────────────┤
│  还没有账号？  [立即注册]        │
└─────────────────────────────────┘
```

## 交互元素

### 1. 标题区域
- **位置：** 顶部
- **组件：**
  - 主标题：欢迎回来
  - 副标题：请登录您的账号
- **样式：** 居中显示，大字体

### 2. 手机号输入
- **位置：** 标题下方
- **组件：**
  - 标签：手机号
  - 输入框：请输入手机号
- **属性：**
  - type: "number"
  - maxlength: 11
  - placeholder: "请输入手机号"
- **事件：** `bindinput="onPhoneInput"`

### 3. 密码输入
- **位置：** 手机号输入下方
- **组件：**
  - 标签：密码
  - 输入框：请输入密码
  - 眼睛图标：显示/隐藏密码
- **属性：**
  - type: "{{showPassword ? 'text' : 'password'}}"
  - maxlength: 20
  - placeholder: "请输入密码"
- **事件：**
  - `bindinput="onPasswordInput"`
  - `bindtap="togglePassword"`

### 4. 登录按钮
- **位置：** 密码输入下方
- **样式：** 青色按钮，全宽
- **状态：**
  - disabled: 手机号和密码都填写时才可点击
  - 样式：disabled时灰色，enabled时青色
- **事件：** `bindtap="onLogin"`

### 5. 忘记密码
- **位置：** 登录按钮下方
- **样式：** 右对齐，灰色文字
- **事件：** `bindtap="onForgotPasswordTap"`

### 6. 第三方登录区域
- **位置：** 中间
- **组件：**
  - 分隔线：其他登录方式
  - 微信登录按钮
- **事件：** `bindtap="onWechatLogin"`

### 7. 注册提示
- **位置：** 底部
- **组件：**
  - 提示文字：还没有账号？
  - 注册链接：立即注册
- **事件：** `bindtap="onRegisterTap"`

## 数据字段

### 1. 输入数据
```javascript
{
  phone: String,              // 手机号
  password: String,           // 密码
  showPassword: Boolean,      // 是否显示密码
  loginButtonDisabled: Boolean // 登录按钮是否禁用
}
```

### 2. 登录状态
```javascript
{
  isSubmitting: Boolean,      // 是否正在提交
  loading: Boolean            // 是否加载中
}
```

## 核心功能

### 1. 手机号输入
```javascript
onPhoneInput(e)
```
- 获取输入的手机号
- 验证手机号格式
- 更新登录按钮状态

### 2. 密码输入
```javascript
onPasswordInput(e)
```
- 获取输入的密码
- 更新登录按钮状态

### 3. 显示/隐藏密码
```javascript
togglePassword()
```
- 切换密码显示状态
- 更新输入框类型
- 切换眼睛图标

### 4. 手机号验证
```javascript
validatePhone(phone)
```
- 验证手机号格式
- 规则：11位数字，1开头
- 返回：true/false

### 5. 登录
```javascript
onLogin()
```
- 验证手机号和密码
- 请求登录API
- 保存token和用户信息
- 跳转到首页
- 错误处理

### 6. 忘记密码
```javascript
onForgotPasswordTap()
```
- 跳转到忘记密码页面
- 或打开忘记密码弹窗

### 7. 微信登录
```javascript
onWechatLogin()
```
- 请求微信登录
- 获取code
- 请求后端登录API
- 保存token和用户信息
- 跳转到首页

### 8. 注册
```javascript
onRegisterTap()
```
- 跳转到注册页面

### 9. 更新登录按钮状态
```javascript
updateLoginButtonStatus()
```
- 检查手机号和密码是否填写
- 更新按钮disabled状态

## API接口

### 1. 手机号密码登录
```
POST /api/user/login
```
**请求：**
```json
{
  "phone": "13800138000",
  "password": "123456"
}
```
**响应：**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "openid": "wx_openid_123456",
      "nickname": "用户昵称",
      "avatar": "/images/avatar.jpg",
      "gender": 1,
      "phone": "13800138000"
    }
  }
}
```

### 2. 微信登录
```
POST /api/user/wechat-login
```
**请求：**
```json
{
  "code": "071AbcDe123456"
}
```
**响应格式同手机号登录**

### 3. 忘记密码
```
POST /api/user/forgot-password
```
**请求：**
```json
{
  "phone": "13800138000"
}
```
**响应：**
```json
{
  "success": true,
  "data": {
    "message": "验证码已发送"
  }
}
```

## 核心验证点 ✅

### 快速验证清单
- [ ] 标题显示正确
- [ ] 手机号输入框正常
- [ ] 手机号只能输入数字
- [ ] 手机号最多11位
- [ ] 密码输入框正常
- [ ] 密码可以显示/隐藏
- [ ] 眼睛图标正常切换
- [ ] 登录按钮disabled状态正确
- [ ] 手机号和密码都填写时登录按钮可点击
- [ ] 点击登录按钮触发登录
- [ ] 登录成功跳转到首页
- [ ] 登录失败显示错误提示
- [ ] 忘记密码可点击
- [ ] 微信登录按钮可点击
- [ ] 注册链接可点击

### 输入验证
1. **手机号格式**
   - 11位数字
   - 1开头
   - 格式错误提示

2. **密码格式**
   - 不为空
   - 长度6-20位
   - 可包含字母、数字、符号

3. **登录按钮状态**
   - 未填写：disabled
   - 填写手机号：disabled
   - 填写密码：disabled
   - 都填写：enabled

### 登录验证
1. **正常登录**
   - 输入正确手机号和密码
   - 点击登录
   - 登录成功
   - 跳转到首页
   - 保存token和用户信息

2. **登录失败**
   - 输入错误手机号或密码
   - 点击登录
   - 登录失败
   - 显示错误提示
   - 保持在登录页

3. **网络错误**
   - 网络断开
   - 点击登录
   - 显示网络错误提示
   - 检查网络连接

### 微信登录验证
1. **微信登录**
   - 点击微信登录
   - 获取微信code
   - 请求后端API
   - 登录成功
   - 跳转到首页

2. **微信登录失败**
   - 微信授权失败
   - 显示错误提示
   - 保持在登录页

## 样式特点

- **整体风格：** 简洁现代，登录导向
- **主色调：** #00bcd4（登录按钮）
- **间距：** 各元素有适当间距
- **圆角：** 输入框和按钮使用圆角
- **字体：** 标题大字体，输入框中等字体
- **对齐：** 居中对齐

## 验证规则

### 手机号验证
```javascript
validatePhone(phone) {
  const regex = /^1[3-9]\d{9}$/
  return regex.test(phone)
}
```

### 密码验证
```javascript
validatePassword(password) {
  return password.length >= 6 && password.length <= 20
}
```

## 密码显示/隐藏

### 实现方式
```javascript
togglePassword() {
  this.setData({
    showPassword: !this.data.showPassword
  })
}
```

### 图标切换
- 显示密码：🙈
- 隐藏密码：👁

## 错误处理

### 手机号错误
```
手机号格式错误
```

### 密码错误
```
密码错误
```

### 账号不存在
```
账号不存在，请先注册
```

### 网络错误
```
网络错误，请检查网络连接后重试
```

### 服务器错误
```
服务器错误，请稍后重试
```

## 登录成功后

### 保存数据
```javascript
wx.setStorageSync('token', res.data.token)
wx.setStorageSync('userInfo', res.data.userInfo)
```

### 更新全局状态
```javascript
const app = getApp()
app.updateLoginStatus(res.data.userInfo, res.data.token)
```

### 跳转
```javascript
wx.switchTab({
  url: '/pages/home/index'
})
```

## 微信登录流程

### 1. 获取微信code
```javascript
wx.login({
  success: (res) => {
    if (res.code) {
      this.wechatLogin(res.code)
    }
  }
})
```

### 2. 请求后端API
```javascript
wechatLogin(code) {
  wx.request({
    url: `${app.globalData.baseUrl}/api/user/wechat-login`,
    method: 'POST',
    data: { code },
    success: (res) => {
      // 保存token和用户信息
      wx.setStorageSync('token', res.data.data.token)
      wx.setStorageSync('userInfo', res.data.data.userInfo)

      // 跳转到首页
      wx.switchTab({
        url: '/pages/home/index'
      })
    }
  })
}
```

## 忘记密码

### 跳转到忘记密码页
```javascript
onForgotPasswordTap() {
  wx.navigateTo({
    url: '/pages/profile/forgot-password'
  })
}
```

### 或打开弹窗
```javascript
onForgotPasswordTap() {
  wx.showModal({
    title: '忘记密码',
    content: '请联系客服重置密码',
    confirmText: '联系客服',
    success: (res) => {
      if (res.confirm) {
        wx.makePhoneCall({
          phoneNumber: '400-123-4567'
        })
      }
    }
  })
}
```

## 注册

### 跳转到注册页
```javascript
onRegisterTap() {
  wx.navigateTo({
    url: '/pages/profile/register'
  })
}
```

## 性能优化

1. **输入防抖**
   - 手机号输入防抖
   - 密码输入防抖

2. **表单验证**
   - 前端验证
   - 减少无效请求

3. **错误提示**
   - 友好提示
   - 引导操作

## 扩展功能

### 1. 记住密码
- 勾选记住密码
- 本地存储
- 自动填充

### 2. 自动登录
- 勾选自动登录
- 检查token有效性
- 自动跳转

### 3. 验证码登录
- 手机号+验证码登录
- 获取验证码
- 倒计时

### 4. 第三方登录
- 支持更多第三方
- QQ登录
- 支付宝登录

### 5. 生物识别
- 指纹登录
- 面容登录

## 注意事项

1. **密码安全**
   - 不明文传输
   - 加密传输
   - HTTPS

2. **Token管理**
   - Token过期刷新
   - Token失效处理
   - 安全存储

3. **错误处理**
   - 友好提示
   - 引导操作
   - 重试机制

4. **隐私保护**
   - 加密存储
   - 敏感信息保护
   - 用户隐私

5. **网络优化**
   - 请求超时
   - 重试机制
   - 离线提示

## 用户场景

### 场景1：手机号密码登录
1. 用户打开登录页面
2. 输入手机号：13800138000
3. 输入密码：123456
4. 点击登录
5. 登录成功
6. 跳转到首页

### 场景2：忘记密码
1. 用户忘记密码
2. 点击"忘记密码？"
3. 跳转到忘记密码页
4. 或联系客服

### 场景3：微信登录
1. 用户点击"微信一键登录"
2. 授权微信登录
3. 登录成功
4. 跳转到首页

### 场景4：新用户注册
1. 用户还没有账号
2. 点击"立即注册"
3. 跳转到注册页面
4. 注册新账号

### 场景5：密码错误
1. 用户输入错误密码
2. 点击登录
3. 登录失败
4. 显示"密码错误"
5. 重新输入密码
6. 登录成功
