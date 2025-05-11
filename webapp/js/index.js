document.addEventListener('DOMContentLoaded', function() {
  // 获取饼图数据
  fetch('http://localhost:8080/api/v1/dashboard/piedata', {
    method: 'POST',
    headers: {
      'Accept': '*/*',
      'Accept-Encoding': 'gzip, deflate, br',
      'Connection': 'keep-alive',
      'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
    }
  })
  .then(response => response.json())
  .then(data => {
    if (data.code === "200") {
      renderPieChart(data.data);
    } else {
      console.error('获取饼图数据失败:', data.info);
    }
  })
  .catch(error => {
    console.error('获取饼图数据出错:', error);
  });

  // 获取柱状图数据
  fetch('http://localhost:8080/api/v1/dashboard/zdata', {
    method: 'POST',
    headers: {
      'Accept': '*/*',
      'Accept-Encoding': 'gzip, deflate, br',
      'Connection': 'keep-alive',
      'User-Agent': 'PostmanRuntime-ApipostRuntime/1.1.0'
    }
  })
  .then(response => response.json())
  .then(data => {
    if (data.code === "200") {
      renderBarChart(data.data);
    } else {
      console.error('获取柱状图数据失败:', data.info);
    }
  })
  .catch(error => {
    console.error('获取柱状图数据出错:', error);
  });

  // 渲染饼图
  function renderPieChart(data) {
    const ctx = document.getElementById('pieChart').getContext('2d');

    // 提取数据
    const labels = Object.keys(data);
    const values = Object.values(data);

    // 生成颜色
    const backgroundColors = [
      '#4dabf7', // 蓝色
      '#f03e3e', // 红色
      '#40c057', // 绿色
      '#fab005', // 黄色
      '#7950f2', // 紫色
      '#fd7e14', // 橙色
      '#868e96'  // 灰色
    ];

    new Chart(ctx, {
      type: 'pie',
      data: {
        labels: labels,
        datasets: [{
          data: values,
          backgroundColor: backgroundColors,
          borderColor: '#1e1e1e',
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
            labels: {
              color: '#ffffff',
              font: {
                size: 14
              }
            }
          },
          tooltip: {
            callbacks: {
              label: function(context) {
                const label = context.label || '';
                const value = context.raw;
                const percentage = (value * 100).toFixed(1) + '%';
                return `${label}: ${percentage}`;
              }
            }
          }
        }
      }
    });
  }

  // 渲染柱状图
  function renderBarChart(data) {
    const ctx = document.getElementById('barChart').getContext('2d');

    // 提取数据
    const labels = Object.keys(data);
    const values = Object.values(data);

    // 生成颜色
    const backgroundColors = [
      'rgba(77, 171, 247, 0.7)', // 蓝色
      'rgba(240, 62, 62, 0.7)',  // 红色
      'rgba(64, 192, 87, 0.7)',  // 绿色
      'rgba(250, 176, 5, 0.7)',  // 黄色
      'rgba(121, 80, 242, 0.7)', // 紫色
      'rgba(253, 126, 20, 0.7)', // 橙色
      'rgba(134, 142, 150, 0.7)' // 灰色
    ];

    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: '文件数量',
          data: values,
          backgroundColor: backgroundColors,
          borderColor: backgroundColors.map(color => color.replace('0.7', '1')),
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              color: '#ffffff',
              precision: 0
            },
            grid: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          },
          x: {
            ticks: {
              color: '#ffffff'
            },
            grid: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        plugins: {
          legend: {
            display: false
          }
        }
      }
    });
  }
});