/**
 * 错误处理工具
 */
export const errorHandler = {
  /**
   * 处理攻略详情加载错误
   * @param {Error} error 错误对象
   * @param {number} id 攻略ID
   * @returns {Object} 错误信息对象 {message, detail}
   */
  handleGuideDetailError(error, id) {
    console.error('攻略详情加载失败:', error);
    
    let errorMessage = '加载失败，请重试';
    let errorDetail = '';
    
    // 分析错误类型
    if (error.message) {
      if (error.message.includes('攻略ID无效')) {
        errorMessage = '攻略ID无效';
        errorDetail = '请检查攻略ID是否正确';
      } else if (error.message.includes('攻略不存在')) {
        errorMessage = '攻略不存在';
        errorDetail = '该攻略可能已被删除';
      } else if (error.message.includes('已下架或未审核')) {
        errorMessage = '攻略已下架或未审核';
        errorDetail = '该攻略暂不可用';
      } else if (error.message.includes('未获取到攻略数据')) {
        errorMessage = '攻略数据加载失败';
        errorDetail = '请稍后重试';
      } else if (error.message.includes('网络') || error.message.includes('timeout')) {
        errorMessage = '网络连接异常';
        errorDetail = '请检查网络连接后重试';
      }
    }
    
    // 记录详细错误信息到日志
    const errorLog = {
      timestamp: new Date().toISOString(),
      error: errorMessage,
      errorDetail: errorDetail,
      guideId: id,
      originalError: error.message,
      stack: error.stack,
      env: 'Windows',
      mpVersion: '1.06.2504060',
      libVersion: '3.13.0'
    };
    
    console.log('错误日志:', JSON.stringify(errorLog));
    
    return { message: errorMessage, detail: errorDetail };
  },

  /**
   * 验证攻略ID
   * @param {*} id 攻略ID
   * @returns {boolean} 是否有效
   */
  validateGuideId(id) {
    if (!id || typeof id !== 'string' && typeof id !== 'number') {
      return false;
    }
    
    const idNum = parseInt(id, 10);
    return !isNaN(idNum) && idNum > 0;
  },

  /**
   * 显示错误提示
   * @param {string} message 错误信息
   * @param {string} detail 错误详情（可选）
   */
  showErrorToast(message, detail) {
    uni.showToast({
      title: message,
      icon: 'none',
      duration: 3000,
      complete: () => {
        if (detail) {
          uni.showModal({
            title: '错误详情',
            content: detail,
            showCancel: false,
            confirmText: '确定'
          });
        }
      }
    });
  },

  /**
   * 记录错误日志
   * @param {Object} errorInfo 错误信息
   */
  logError(errorInfo) {
    // 这里可以添加日志上报逻辑
    console.log('错误日志:', JSON.stringify(errorInfo));
  }
};