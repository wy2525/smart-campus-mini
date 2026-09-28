// API基础配置
let baseURL = "http://localhost:8082/api/public";

// 根据不同运行环境配置不同的baseURL
const platform = uni.getSystemInfoSync().platform;
console.log("当前运行环境:", platform);

// 处理不同平台的baseURL
if (platform === "android") {
  // Android模拟器
  baseURL = "http://10.0.2.2:8082/api/public";
} else if (platform === "devtools") {
  // 微信开发者工具
  baseURL = "http://localhost:8082/api/public";
} else if (platform === "ios") {
  // iOS模拟器
  baseURL = "http://localhost:8082/api/public";
} else {
  // 默认使用localhost，适用于网页预览等其他环境
  baseURL = "http://localhost:8082/api/public";
}

const config = {
  // 后端API地址
  baseURL: baseURL,

  // 请求超时时间（毫秒）
  timeout: 30000,
};

/**
 * 统一请求方法
 * @param {string} url - 请求地址
 * @param {object} data - 请求参数
 * @param {string} method - 请求方法
 * @returns {Promise}
 */
function request(url, params = {}, data = {}, method = "GET") {
  return new Promise((resolve, reject) => {
    const header = {
      "Content-Type": "application/json;charset=UTF-8",
    };

    // 从本地存储获取token
    const token = uni.getStorageSync("token");
    if (token) {
      header["Authorization"] = `Bearer ${token}`;
    }

    uni.request({
      url: config.baseURL + url,
      data:
        method === "POST" || method === "PUT" || method === "DELETE"
          ? data
          : params,
      method: method,
      header: header,
      timeout: config.timeout,
      success: (res) => {
        if (res.statusCode === 200) {
          const response = res.data;
          // 判断业务状态码
          if (response.code === 200) {
            resolve(response.data);
          } else {
            uni.showToast({
              title: response.message || "请求失败",
              icon: "none",
              duration: 2000,
            });
            reject(response.message || "请求失败");
          }
        } else {
          uni.showToast({
            title: `请求失败 (${res.statusCode})`,
            icon: "none",
            duration: 2000,
          });
          reject(`请求失败: ${res.statusCode}`);
        }
      },
      fail: (err) => {
        console.error("请求失败:", err);
        uni.showToast({
          title: "网络连接失败",
          icon: "none",
          duration: 2000,
        });
        reject(err);
      },
    });
  });
}

/**
 * GET请求
 */
export function get(url, params = {}) {
  return request(url, params, {}, "GET");
}

/**
 * POST请求
 */
export function post(url, data = {}) {
  return request(url, {}, data, "POST");
}

/**
 * PUT请求
 */
export function put(url, data = {}) {
  return request(url, {}, data, "PUT");
}

/**
 * DELETE请求
 */
export function del(url, data = {}) {
  return request(url, {}, data, "DELETE");
}

export default config;
