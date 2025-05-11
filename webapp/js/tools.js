document.addEventListener('DOMContentLoaded', function() {
  // 全局变量，存储当前选择的工具
  let currentTool = 'csdn';

  // DOM元素
  const chatMessages = document.getElementById('chatMessages');
  const messageInput = document.getElementById('messageInput');
  const sendButton = document.getElementById('sendButton');
  const currentToolElement = document.getElementById('currentTool');
  const sidebarItems = document.querySelectorAll('.sidebar-item');

  // 自动调整输入框高度
  messageInput.addEventListener('input', function() {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
  });

  // 按Enter发送消息（Shift+Enter换行）
  messageInput.addEventListener('keydown', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  });

  // 点击发送按钮
  sendButton.addEventListener('click', sendMessage);

  // 侧边栏工具选择
  sidebarItems.forEach(item => {
    item.addEventListener('click', function() {
      // 移除所有激活状态
      sidebarItems.forEach(i => i.classList.remove('active'));
      // 添加当前激活状态
      this.classList.add('active');

      // 更新当前工具
      currentTool = this.getAttribute('data-tool');

      // 更新标题
      if (currentTool === 'csdn') {
        currentToolElement.textContent = 'CSDN自动发帖';
      } else if (currentTool === 'search') {
        currentToolElement.textContent = '自然语言检索';
      }

      // 添加系统消息
      const toolName = currentTool === 'csdn' ? 'CSDN自动发帖' : '自然语言检索';
      addMessage(`已切换到${toolName}工具，请输入您的请求。`);
    });
  });

  // 发送消息函数
  function sendMessage() {
    const message = messageInput.value.trim();
    if (!message) return;

    // 添加用户消息
    addMessage(message, true);

    // 清空输入框
    messageInput.value = '';
    messageInput.style.height = 'auto';

    // 禁用发送按钮
    sendButton.disabled = true;

    // 添加AI消息（带有打字指示器）
    const aiMessageElement = addTypingIndicator();

    // 根据当前工具调用不同的API
    if (currentTool === 'csdn') {
      fetchCSDNResponse(message, aiMessageElement);
    } else if (currentTool === 'search') {
      fetchSearchResponse(message, aiMessageElement);
    }
  }

  // 添加消息到聊天区域
  function addMessage(text, isUser = false) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${isUser ? 'user' : ''}`;

    const currentTime = new Date();
    const timeString = `${currentTime.getHours()}:${String(currentTime.getMinutes()).padStart(2, '0')}`;

    messageDiv.innerHTML = `
                    <div class="message-avatar">
                        <i class="fas ${isUser ? 'fa-user' : 'fa-robot'}"></i>
                    </div>
                    <div class="message-content">
                        <div class="message-text">${text}</div>
                        <div class="message-time">今天 ${timeString}</div>
                    </div>
                `;

    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;

    return messageDiv;
  }

  // 添加打字指示器
  function addTypingIndicator() {
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message';

    const currentTime = new Date();
    const timeString = `${currentTime.getHours()}:${String(currentTime.getMinutes()).padStart(2, '0')}`;

    messageDiv.innerHTML = `
                    <div class="message-avatar">
                        <i class="fas fa-robot"></i>
                    </div>
                    <div class="message-content">
                        <div class="message-text">
                            <div class="typing-indicator">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                        </div>
                        <div class="message-time">今天 ${timeString}</div>
                    </div>
                `;

    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;

    return messageDiv;
  }

  // 获取CSDN自动发帖响应
  function fetchCSDNResponse(message, aiMessageElement) {
    const url = `http://localhost:8080/api/v1/func/csdn?message=${encodeURIComponent(message)}`;

    fetch(url, {
      method: 'GET',
      headers: {
        'Accept': '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
      }
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('网络响应不正常');
      }
      return response.json();
    })
    .then(data => {
      // 处理流式响应
      handleStreamResponse(data, aiMessageElement);
    })
    .catch(error => {
      console.error('Error:', error);
      // 移除打字指示器，显示错误消息
      const messageContent = aiMessageElement.querySelector('.message-content');
      const messageText = messageContent.querySelector('.message-text');
      messageText.textContent = '抱歉，发生了错误，请稍后再试。';

      // 启用发送按钮
      sendButton.disabled = false;

      // 显示错误提示
      showAlert('请求失败，请稍后再试', 'error');
    });
  }

  // 获取自然语言检索响应
  function fetchSearchResponse(message, aiMessageElement) {
    const url = `http://localhost:8080/api/v1/search/query?message=${encodeURIComponent(message)}`;

    fetch(url, {
      method: 'GET',
      headers: {
        'Accept': '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
      }
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('网络响应不正常');
      }
      return response.json();
    })
    .then(data => {
      // 处理JSON响应
      if (data.code === "200") {
        // 移除打字指示器，显示响应消息
        const messageContent = aiMessageElement.querySelector('.message-content');
        const messageText = messageContent.querySelector('.message-text');
        messageText.textContent = data.data;

        // 启用发送按钮
        sendButton.disabled = false;
      } else {
        throw new Error(data.info || '请求失败');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      // 移除打字指示器，显示错误消息
      const messageContent = aiMessageElement.querySelector('.message-content');
      const messageText = messageContent.querySelector('.message-text');
      messageText.textContent = '抱歉，发生了错误，请稍后再试。';

      // 启用发送按钮
      sendButton.disabled = false;

      // 显示错误提示
      showAlert('请求失败，请稍后再试', 'error');
    });
  }

  // 处理流式响应
  function handleStreamResponse(data, aiMessageElement) {
    // 移除打字指示器
    const messageContent = aiMessageElement.querySelector('.message-content');
    const messageText = messageContent.querySelector('.message-text');
    messageText.textContent = '';

    let fullText = '';
    let currentIndex = 0;

    // 模拟打字效果
    function typeNextChunk() {
      if (currentIndex < data.length) {
        const chunk = data[currentIndex];
        if (chunk.result && chunk.result.output && chunk.result.output.text) {
          const textChunk = chunk.result.output.text;
          fullText += textChunk;
          messageText.textContent = fullText;
          chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        currentIndex++;
        setTimeout(typeNextChunk, 50); // 调整打字速度
      } else {
        // 打字完成，启用发送按钮
        sendButton.disabled = false;
      }
    }

    // 开始打字效果
    typeNextChunk();
  }

  // 显示提示框
  function showAlert(message, type = 'info') {
    const alert = document.getElementById('alert');
    const alertMessage = document.getElementById('alertMessage');

    alertMessage.textContent = message;
    alert.className = 'alert';
    alert.classList.add(`alert-${type}`);

    const icon = alert.querySelector('i');
    if (type === 'success') {
      icon.className = 'fas fa-check-circle';
    } else if (type === 'error') {
      icon.className = 'fas fa-exclamation-circle';
    } else {
      icon.className = 'fas fa-info-circle';
    }

    alert.classList.add('show');

    setTimeout(() => {
      alert.classList.remove('show');
    }, 3000);
  }
});