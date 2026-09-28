/**
 * Kimi API 调用封装
 * 官方文档: https://platform.moonshot.ai/docs/intro
 */

// Kimi API 配置
const KIMI_CONFIG = {
  baseUrl: 'https://api.moonshot.cn/v1',
  apiKey: 'sk-AtZkpE3YtQnZpzOIb50nhgTmrxKn85cCxuwJFoj1CemEhJUu',
  model: 'kimi-k2-turbo-preview',
  timeout: 30000,
  maxRetries: 2
}

// 系统提示词模板
const SYSTEM_PROMPTS = {
  // 通用对话助手
  chat: `你是 Kimi，由 Moonshot AI 提供的智能旅游助手，专精于旅游相关知识的解答。

你的职责：
1. 回答用户关于旅游景点、路线规划、住宿、交通、美食等旅游相关问题
2. 提供准确、实用、有帮助的旅游建议
3. 回答要简洁明了，重点突出
4. 如果遇到超出旅游领域的问题，友好地引导回旅游话题

请用中文回答，保持友好、专业的态度。`,

  // 景点解读
  attraction: `你是一位资深的旅游景点解说专家，擅长用生动的语言解读景点的历史、文化、特色和价值。

你的职责：
1. 根据景点信息，生成深度、有趣的解读内容
2. 突出景点的历史背景、文化内涵、建筑特色、艺术价值
3. 提供实用的游览建议，如最佳游览路线、必看景点、拍照点等
4. 给出参观注意事项和实用贴士

输出格式要求：
- 标题：简洁吸引人
- 历史背景：2-3句话
- 核心亮点：3-5个要点，每个要点30-50字
- 游览建议：具体实用
- 注意事项：提醒用户注意的点

请用生动、专业的语言，让读者对景点产生浓厚兴趣。`,

  // 攻略助写
  guide: `你是一位经验丰富的旅游攻略写作专家，擅长撰写高质量、实用的旅游攻略。

你的职责：
1. 根据用户提供的信息（景点名称、游玩天数、特色需求等），生成完整的旅游攻略
2. 攻略内容应包含：行程安排、景点介绍、美食推荐、住宿建议、交通指南、注意事项
3. 内容要真实、实用、有参考价值
4. 语言要生动有趣，能够引起读者的旅游兴趣

输出格式要求：
- 精彩标题：吸引人的攻略标题
- 行程概览：简要介绍行程安排
- 每日行程：详细的景点、活动、餐饮安排
- 实用贴士：交通、住宿、预算等建议
- 注意事项：安全、天气、必备物品等

攻略要真实可信，避免虚假信息，为读者提供真正有用的旅游参考。`,

  // 行程规划
  itinerary: `你是一位专业的行程规划师，擅长根据用户需求制定科学、合理、高效的旅游行程。

你的职责：
1. 根据用户提供的出发地、目的地、时间、预算、偏好等信息，规划最优行程
2. 合理安排景点游览顺序，避免路线迂回，节省时间和成本
3. 考虑景点的开放时间、最佳游览时间、交通方式等因素
4. 提供时间安排、路线优化、预算估算等具体建议

输出格式要求：
- 行程概览：出发地、目的地、时间、预算等基本信息
- 每日安排：
  - 上午：景点/活动（含到达方式和游览时长）
  - 中午：餐饮推荐（具体餐厅/区域，特色菜品）
  - 下午：景点/活动（含到达方式和游览时长）
  - 晚上：晚餐和晚间活动
- 交通指南：各景点之间的交通方式和时间
- 预算估算：交通、门票、餐饮、住宿等费用
- 实用贴士：天气、必备物品、注意事项等

行程要科学合理，张弛有度，确保用户获得最佳的旅游体验。`
}

/**
 * 核心 API 调用函数
 * @param {Array} messages - 对话消息数组 [{role, content}]
 * @param {Number} temperature - 温度参数 0-1
 * @param {Number} maxTokens - 最大返回 token 数
 * @returns {Promise} 返回 API 响应
 */
async function callKimiAPI(messages, temperature = 0.7, maxTokens = 2000) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${KIMI_CONFIG.baseUrl}/chat/completions`,
      method: 'POST',
      header: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${KIMI_CONFIG.apiKey}`
      },
      data: {
        model: KIMI_CONFIG.model,
        messages: messages,
        temperature: temperature,
        max_tokens: maxTokens,
        stream: false
      },
      timeout: KIMI_CONFIG.timeout,
      success: (res) => {
        console.log('[Kimi API] 请求成功:', res.statusCode, res.data)
        
        if (res.statusCode === 200 && res.data && res.data.choices && res.data.choices.length > 0) {
          resolve({
            success: true,
            content: res.data.choices[0].message.content,
            usage: res.data.usage
          })
        } else {
          console.error('[Kimi API] 响应格式错误:', res.data)
          reject(new Error('API 响应格式错误'))
        }
      },
      fail: (err) => {
        console.error('[Kimi API] 请求失败:', err)
        reject(err)
      }
    })
  })
}

/**
 * 带重试机制的 API 调用
 * @param {Array} messages - 对话消息数组
 * @param {Number} temperature - 温度参数
 * @param {Number} maxTokens - 最大返回 token 数
 * @returns {Promise} 返回 API 响应
 */
async function callWithRetry(messages, temperature = 0.7, maxTokens = 2000) {
  let lastError = null
  
  for (let i = 0; i <= KIMI_CONFIG.maxRetries; i++) {
    try {
      return await callKimiAPI(messages, temperature, maxTokens)
    } catch (error) {
      lastError = error
      console.warn(`[Kimi API] 第 ${i + 1} 次请求失败，准备重试...`)
      
      if (i < KIMI_CONFIG.maxRetries) {
        // 延迟重试
        await new Promise(resolve => setTimeout(resolve, 1000 * (i + 1)))
      }
    }
  }
  
  throw lastError || new Error('Kimi API 调用失败')
}

/**
 * 构建消息数组
 * @param {String} systemPrompt - 系统提示词
 * @param {Array} conversationHistory - 对话历史
 * @param {String} userMessage - 用户消息
 * @returns {Array} 完整的消息数组
 */
function buildMessages(systemPrompt, conversationHistory = [], userMessage) {
  const messages = [
    { role: 'system', content: systemPrompt }
  ]
  
  // 添加历史对话（限制最近10轮）
  const limitedHistory = conversationHistory.slice(-10)
  messages.push(...limitedHistory)
  
  // 添加当前用户消息
  messages.push({ role: 'user', content: userMessage })
  
  return messages
}

/**
 * 通用对话接口
 * @param {String} message - 用户消息
 * @param {Array} history - 对话历史 [{role, content}]
 * @returns {Promise} 返回 AI 回复
 */
export async function chatWithKimi(message, history = []) {
  try {
    const messages = buildMessages(SYSTEM_PROMPTS.chat, history, message)
    const response = await callWithRetry(messages)
    
    console.log('[Kimi AI] 对话成功，消耗 tokens:', response.usage.total_tokens)
    return response.content
  } catch (error) {
    console.error('[Kimi AI] 对话失败:', error)
    throw error
  }
}

/**
 * 景点 AI 解读
 * @param {Object} attractionInfo - 景点信息 {name, description, tags, address, openTime}
 * @returns {Promise} 返回 AI 解读内容
 */
export async function interpretAttraction(attractionInfo) {
  try {
    const prompt = `请为我解读以下景点：

景点名称：${attractionInfo.name}
景点介绍：${attractionInfo.description || '暂无详细介绍'}
景点标签：${attractionInfo.tags || '无'}
景点地址：${attractionInfo.address || '未知'}
开放时间：${attractionInfo.openTime || '未知'}

请生成一个生动、有趣、实用的景点解读。`
    
    const messages = buildMessages(SYSTEM_PROMPTS.attraction, [], prompt)
    const response = await callWithRetry(messages, 0.8, 2500)
    
    console.log('[Kimi AI] 景点解读成功，消耗 tokens:', response.usage.total_tokens)
    return response.content
  } catch (error) {
    console.error('[Kimi AI] 景点解读失败:', error)
    throw error
  }
}

/**
 * 攻略 AI 助写
 * @param {Object} guideInfo - 攻略信息 {attractionName, days, style, keywords}
 * @returns {Promise} 返回生成的攻略内容
 */
export async function generateGuide(guideInfo) {
  try {
    const prompt = `请帮我生成一份旅游攻略：

关联景点：${guideInfo.attractionName || '未指定'}
游玩天数：${guideInfo.days || '1'}天
游玩风格：${guideInfo.style || '自由行'}
特殊需求：${guideInfo.keywords || '无'}

请生成一份详细、实用的旅游攻略。`
    
    const messages = buildMessages(SYSTEM_PROMPTS.guide, [], prompt)
    const response = await callWithRetry(messages, 0.7, 3000)
    
    console.log('[Kimi AI] 攻略生成成功，消耗 tokens:', response.usage.total_tokens)
    return response.content
  } catch (error) {
    console.error('[Kimi AI] 攻略生成失败:', error)
    throw error
  }
}

/**
 * 行程规划
 * @param {Object} itineraryInfo - 行程信息 {destination, days, budget, travelers, preferences}
 * @returns {Promise} 返回行程规划内容
 */
export async function planItinerary(itineraryInfo) {
  try {
    const prompt = `请帮我规划旅游行程：

目的地：${itineraryInfo.destination || '未指定'}
游玩天数：${itineraryInfo.days || '1'}天
预算范围：${itineraryInfo.budget || '不限'}
出行人数：${itineraryInfo.travelers || '1'}人
偏好：${itineraryInfo.preferences || '无'}

请生成一份科学、合理、详细的行程规划。`
    
    const messages = buildMessages(SYSTEM_PROMPTS.itinerary, [], prompt)
    const response = await callWithRetry(messages, 0.6, 3500)
    
    console.log('[Kimi AI] 行程规划成功，消耗 tokens:', response.usage.total_tokens)
    return response.content
  } catch (error) {
    console.error('[Kimi AI] 行程规划失败:', error)
    throw error
  }
}

/**
 * 自定义系统提示词对话
 * @param {String} systemPrompt - 自定义系统提示词
 * @param {String} message - 用户消息
 * @param {Array} history - 对话历史
 * @param {Number} temperature - 温度参数
 * @returns {Promise} 返回 AI 回复
 */
export async function customChat(systemPrompt, message, history = [], temperature = 0.7) {
  try {
    const messages = buildMessages(systemPrompt, history, message)
    const response = await callWithRetry(messages, temperature)
    
    console.log('[Kimi AI] 自定义对话成功，消耗 tokens:', response.usage.total_tokens)
    return response.content
  } catch (error) {
    console.error('[Kimi AI] 自定义对话失败:', error)
    throw error
  }
}

// 导出配置
export { KIMI_CONFIG }
