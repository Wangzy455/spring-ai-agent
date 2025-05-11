document.addEventListener('DOMContentLoaded', function() {
  // 全局变量，存储当前选择的知识库
  let selectedKnowledgeBase = null;

  // DOM元素
  const chatMessages = document.getElementById('chatMessages');
  const messageInput = document.getElementById('messageInput');
  const sendButton = document.getElementById('sendButton');
  const uploadKnowledgeBtn = document.getElementById('uploadKnowledgeBtn');
  const selectKnowledgeBtn = document.getElementById('selectKnowledgeBtn');
  const uploadModal = document.getElementById('uploadModal');
  const closeUploadModal = document.getElementById('closeUploadModal');
  const knowledgeTitle = document.getElementById('knowledgeTitle');
  const knowledgeContent = document.getElementById('knowledgeContent');
  const uploadSubmit = document.getElementById('uploadSubmit');
  const selectModal = document.getElementById('selectModal');
  const closeSelectModal = document.getElementById('closeSelectModal');
  const knowledgeTableBody = document.getElementById('knowledgeTableBody');
  const refreshKnowledgeBtn = document.getElementById('refreshKnowledgeBtn');
  const selectedKnowledgeLabel = document.getElementById('selectedKnowledgeLabel');

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

  // 上传知识库按钮
  uploadKnowledgeBtn.addEventListener('click', function() {
    // 清空表单
    knowledgeTitle.value = '';
    knowledgeContent.value = '';

    // 显示模态框
    uploadModal.classList.add('active');
  });

  // 关闭上传模态框
  closeUploadModal.addEventListener('click', function() {
    uploadModal.classList.remove('active');
  });

  // 上传知识库提交
  uploadSubmit.addEventListener('click', function() {
    const title = knowledgeTitle.value.trim();
    const content = knowledgeContent.value.trim();

    if (!title) {
      showAlert('请输入知识库标题', 'error');
      return;
    }

    if (!content) {
      showAlert('请输入知识库内容', 'error');
      return;
    }

    // 禁用按钮，防止重复提交
    uploadSubmit.disabled = true;
    uploadSubmit.innerHTML = '<div class="loading-spinner"></div> 上传中...';

    // 调用API上传知识库
    uploadKnowledgeBase(title, content);
  });

  // 选择知识库按钮
  selectKnowledgeBtn.addEventListener('click', function() {
    // 显示模态框
    selectModal.classList.add('active');

    // 加载知识库列表
    loadKnowledgeBases();
  });

  // 关闭选择模态框
  closeSelectModal.addEventListener('click', function() {
    selectModal.classList.remove('active');
  });

  // 刷新知识库列表
  refreshKnowledgeBtn.addEventListener('click', loadKnowledgeBases);

  // 发送消息函数
  function sendMessage() {
    const message = messageInput.value.trim();
    if (!message) return;

    // 检查是否已选择知识库
    if (!selectedKnowledgeBase) {
      showAlert('请先选择一个知识库', 'info');
      return;
    }

    // 添加用户消息
    addMessage(message, true);

    // 清空输入框
    messageInput.value = '';
    messageInput.style.height = 'auto';

    // 禁用发送按钮
    sendButton.disabled = true;

    // 添加AI消息（带有打字指示器）
    const aiMessageElement = addTypingIndicator();

    // 调用API获取流式响应
    fetchStreamResponse(selectedKnowledgeBase, message, aiMessageElement);
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

  // 上传知识库
  function uploadKnowledgeBase(title, content) {
    const url = `http://localhost:8080/api/v1/rag/upload?ragTag=${encodeURIComponent(title)}&file=${encodeURIComponent(content)}`;

    fetch(url, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
      }})
    .then(response => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error('上传失败');
      }
    })
    .then(data => {
      if (data.code === "200") {
        showAlert('上传知识库成功', 'success');
        uploadModal.classList.remove('active');

        // 自动选择新上传的知识库
        selectedKnowledgeBase = title;
        updateSelectedKnowledgeUI();

        // 添加系统消息
        addMessage(`已成功上传并选择知识库"${title}"，现在可以开始对话了。`);
      } else {
        throw new Error(data.info || '上传失败');
      }
    })
    .catch(error => {
      console.error('Error uploading knowledge base:', error);
      showAlert(error.message || '上传知识库失败，请重试', 'error');
    })
    .finally(() => {
      uploadSubmit.disabled = false;
      uploadSubmit.innerHTML = '上传知识库';
    });
  }

  // 加载知识库列表
  function loadKnowledgeBases() {
    // 显示加载中
    knowledgeTableBody.innerHTML = `
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <div class="loading-spinner" style="margin: 20px auto;"></div>
                            <p>正在加载知识库...</p>
                        </td>
                    </tr>
                `;

    fetch('http://localhost:8080/api/v1/rag/show', {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
      }
    })
    .then(response => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error('获取知识库列表失败');
      }
    })
    .then(data => {
      if (data.code === "200" && data.data && data.data.length > 0) {
        displayKnowledgeBases(data.data);
      } else {
        knowledgeTableBody.innerHTML = `
                            <tr>
                                <td colspan="2" style="text-align: center;">
                                    <p>暂无知识库，请先上传</p>
                                </td>
                            </tr>
                        `;
      }
    })
    .catch(error => {
      console.error('Error fetching knowledge bases:', error);
      knowledgeTableBody.innerHTML = `
                        <tr>
                            <td colspan="2" style="text-align: center;">
                                <p>获取知识库列表失败，请重试</p>
                            </td>
                        </tr>
                    `;
    });
  }

  // 显示知识库列表
  function displayKnowledgeBases(knowledgeBases) {
    knowledgeTableBody.innerHTML = '';

    knowledgeBases.forEach(kb => {
      const row = document.createElement('tr');

      row.innerHTML = `
                        <td>${kb}</td>
                        <td class="action-cell">
                            <button class="table-btn select-btn" data-kb="${kb}" ${kb === selectedKnowledgeBase ? 'disabled' : ''}>
                                ${kb === selectedKnowledgeBase ? '已选择' : '选择'}
                            </button>
                            <button class="table-btn delete-btn" data-kb="${kb}">
                                删除
                            </button>
                        </td>
                    `;

      knowledgeTableBody.appendChild(row);
    });

    // 添加选择按钮事件
    document.querySelectorAll('.select-btn').forEach(btn => {
      btn.addEventListener('click', function() {
        const kb = this.getAttribute('data-kb');
        selectKnowledgeBase(kb);
      });
    });

    // 添加删除按钮事件
    document.querySelectorAll('.delete-btn').forEach(btn => {
      btn.addEventListener('click', function() {
        const kb = this.getAttribute('data-kb');
        deleteKnowledgeBase(kb);
      });
    });
  }

  // 选择知识库
  function selectKnowledgeBase(kb) {
    selectedKnowledgeBase = kb;

    // 更新UI
    updateSelectedKnowledgeUI();

    // 关闭模态框
    selectModal.classList.remove('active');

    // 显示提示
    showAlert(`已选择知识库"${kb}"`, 'success');

    // 添加系统消息
    addMessage(`已选择知识库"${kb}"，现在可以开始对话了。`);
  }

  // 更新选择的知识库UI
  function updateSelectedKnowledgeUI() {
    // 更新发送按钮状态
    sendButton.disabled = !selectedKnowledgeBase;

    // 更新知识库标签
    if (selectedKnowledgeBase) {
      selectedKnowledgeLabel.textContent = `当前: ${selectedKnowledgeBase}`;
      selectedKnowledgeLabel.style.display = 'inline';
    } else {
      selectedKnowledgeLabel.style.display = 'none';
    }

    // 更新选择按钮状态
    document.querySelectorAll('.select-btn').forEach(btn => {
      const kb = btn.getAttribute('data-kb');
      if (kb === selectedKnowledgeBase) {
        btn.disabled = true;
        btn.textContent = '已选择';
      } else {
        btn.disabled = false;
        btn.textContent = '选择';
      }
    });
  }

  // 删除知识库
  function deleteKnowledgeBase(kb) {
    if (!confirm(`确定要删除知识库"${kb}"吗？此操作不可恢复。`)) {
      return;
    }

    fetch(`http://localhost:8080/api/v1/rag/delete?ragTag=${encodeURIComponent(kb)}`, {
      method: 'GET',
      headers: {
        'Accept': '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
      }
    })
    .then(response => {
      if (response.status === 200) {
        showAlert(`已删除知识库"${kb}"`, 'success');

        // 如果删除的是当前选择的知识库，清除选择
        if (kb === selectedKnowledgeBase) {
          selectedKnowledgeBase = null;
          updateSelectedKnowledgeUI();
          addMessage(`当前选择的知识库"${kb}"已被删除，请重新选择知识库。`);
        }

        // 重新加载知识库列表
        loadKnowledgeBases();
      } else {
        throw new Error('删除失败');
      }
    })
    .catch(error => {
      console.error('Error deleting knowledge base:', error);
      showAlert('删除知识库失败，请重试', 'error');
    });
  }

  // 获取流式响应
  function fetchStreamResponse(kb, message, aiMessageElement) {
    const url = `http://localhost:8080/api/v1/rag/chat?ragTag=${encodeURIComponent(kb)}&message=${encodeURIComponent(message)}`;

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
        sendButton.disabled = !selectedKnowledgeBase;
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