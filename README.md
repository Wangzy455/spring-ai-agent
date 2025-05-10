### 基于Spring ai alibaba 的智能网盘demo

#### 快速开始
需要使用JDK17或以上的版本  
依赖除了docker文件中提供的minio,还需要mysql和redis,建表语句在docs中

运行 
```
git clone git@github.com:Wangzy455/spring-ai-agent.git
```
修改配置文件以及CSDN controller  
配置文件中数据库配置更换成自己的,api key可以去阿里百炼申请,在代码中的CSDNcontroller中的cookie变量填写自己的cookie

已实现功能

基于Minio存储文件（增删改查），实现文件下载  
支持拖拽文件上传  
文件元数据存储和使用  
简单的智能检索  
回收站文件的定时删除  


TODO:
- [ ] 编写适配系统的mcp服务
- [ ] 把主服务改造成mcp client
- [ ] 在mcp client里面引入mcp服务
- [ ] 首页进行数据展示
- [ ] 联网数据查询
- [ ] 文件分享

