/**
 * AI 对话上下文管理工具
 * 用于管理多轮对话的上下文记忆，支持本地存储持久化
 */

// 存储键名前缀
const STORAGE_PREFIX = 'kimi_ai_context_'
const MAX_HISTORY_SIZE = 20 // 最大保留历史消息数

/**
 * AI 上下文管理器类
 */
class AIContextManager {
  constructor() {
    this.currentSession = 'default'
  }

  /**
   * 获取对话历史
   * @param {String} sessionId - 会话 ID（可选，默认使用当前会话）
   * @returns {Array} 对话历史 [{role, content, time}]
   */
  getContext(sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const storageKey = STORAGE_PREFIX + sessionKey
    
    try {
      const contextData = uni.getStorageSync(storageKey)
      if (!contextData) {
        return []
      }
      
      return JSON.parse(contextData)
    } catch (error) {
      console.error('[AI Context] 获取上下文失败:', error)
      return []
    }
  }

  /**
   * 添加消息到对话历史
   * @param {String} role - 消息角色 'user' | 'assistant'
   * @param {String} content - 消息内容
   * @param {String} sessionId - 会话 ID（可选）
   */
  addMessage(role, content, sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const storageKey = STORAGE_PREFIX + sessionKey
    
    try {
      // 获取现有历史
      const history = this.getContext(sessionKey)
      
      // 添加新消息
      const newMessage = {
        role: role,
        content: content,
        time: new Date().toISOString()
      }
      
      history.push(newMessage)
      
      // 限制历史消息数量
      if (history.length > MAX_HISTORY_SIZE) {
        history.splice(0, history.length - MAX_HISTORY_SIZE)
      }
      
      // 保存到本地存储
      uni.setStorageSync(storageKey, JSON.stringify(history))
      
      console.log(`[AI Context] 消息已添加，当前历史数: ${history.length}`)
      return history
    } catch (error) {
      console.error('[AI Context] 添加消息失败:', error)
      return this.getContext(sessionKey)
    }
  }

  /**
   * 清空对话历史
   * @param {String} sessionId - 会话 ID（可选）
   */
  clearContext(sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const storageKey = STORAGE_PREFIX + sessionKey
    
    try {
      uni.removeStorageSync(storageKey)
      console.log(`[AI Context] 会话 "${sessionKey}" 已清空`)
    } catch (error) {
      console.error('[AI Context] 清空上下文失败:', error)
    }
  }

  /**
   * 获取用于 API 调用的消息数组（转换格式）
   * @param {String} sessionId - 会话 ID（可选）
   * @returns {Array} API 格式的消息数组 [{role, content}]
   */
  getAPIMessages(sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const history = this.getContext(sessionKey)
    
    // 转换为 API 需要的格式（只保留 role 和 content）
    return history.map(msg => ({
      role: msg.role,
      content: msg.content
    }))
  }

  /**
   * 设置当前会话 ID
   * @param {String} sessionId - 会话 ID
   */
  setSession(sessionId) {
    this.currentSession = sessionId
    console.log(`[AI Context] 切换到会话: ${sessionId}`)
  }

  /**
   * 获取所有会话 ID
   * @returns {Array} 会话 ID 列表
   */
  getAllSessions() {
    try {
      const allKeys = uni.getStorageInfoSync().keys
      const sessionKeys = allKeys
        .filter(key => key.startsWith(STORAGE_PREFIX))
        .map(key => key.replace(STORAGE_PREFIX, ''))
      
      return sessionKeys
    } catch (error) {
      console.error('[AI Context] 获取所有会话失败:', error)
      return []
    }
  }

  /**
   * 批量添加消息（用于初始化）
   * @param {Array} messages - 消息数组 [{role, content}]
   * @param {String} sessionId - 会话 ID（可选）
   */
  batchAddMessages(messages, sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const storageKey = STORAGE_PREFIX + sessionKey
    
    try {
      let history = this.getContext(sessionKey)
      
      messages.forEach(msg => {
        history.push({
          role: msg.role,
          content: msg.content,
          time: new Date().toISOString()
        })
      })
      
      // 限制历史消息数量
      if (history.length > MAX_HISTORY_SIZE) {
        history.splice(0, history.length - MAX_HISTORY_SIZE)
      }
      
      uni.setStorageSync(storageKey, JSON.stringify(history))
      console.log(`[AI Context] 批量添加 ${messages.length} 条消息`)
      return history
    } catch (error) {
      console.error('[AI Context] 批量添加消息失败:', error)
      return this.getContext(sessionKey)
    }
  }

  /**
   * 导出对话历史（用于备份或分享）
   * @param {String} sessionId - 会话 ID（可选）
   * @returns {String} JSON 格式的对话历史
   */
  exportContext(sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const history = this.getContext(sessionKey)
    
    try {
      return JSON.stringify(history, null, 2)
    } catch (error) {
      console.error('[AI Context] 导出上下文失败:', error)
      return ''
    }
  }

  /**
   * 统计对话信息
   * @param {String} sessionId - 会话 ID（可选）
   * @returns {Object} 统计信息
   */
  getContextStats(sessionId = null) {
    const sessionKey = sessionId || this.currentSession
    const history = this.getContext(sessionKey)
    
    const userMessages = history.filter(msg => msg.role === 'user').length
    const assistantMessages = history.filter(msg => msg.role === 'assistant').length
    const totalChars = history.reduce((sum, msg) => sum + msg.content.length, 0)
    
    return {
      totalMessages: history.length,
      userMessages: userMessages,
      assistantMessages: assistantMessages,
      totalCharacters: totalChars,
      sessionId: sessionKey
    }
  }
}

// 创建全局实例
const aiContextManager = new AIContextManager()

/**
 * 便捷方法：添加用户消息
 */
export function addUserMessage(content, sessionId = null) {
  return aiContextManager.addMessage('user', content, sessionId)
}

/**
 * 便捷方法：添加助手消息
 */
export function addAssistantMessage(content, sessionId = null) {
  return aiContextManager.addMessage('assistant', content, sessionId)
}

/**
 * 便捷方法：获取 API 消息
 */
export function getAPIMessages(sessionId = null) {
  return aiContextManager.getAPIMessages(sessionId)
}

/**
 * 便捷方法：清空上下文
 */
export function clearAIContext(sessionId = null) {
  return aiContextManager.clearContext(sessionId)
}

/**
 * 便捷方法：设置会话
 */
export function setAISession(sessionId) {
  return aiContextManager.setSession(sessionId)
}

/**
 * 便捷方法：获取上下文统计
 */
export function getContextStats(sessionId = null) {
  return aiContextManager.getContextStats(sessionId)
}

// 导出管理器实例
export { aiContextManager as AIContextManager }
export default aiContextManager
