# Mantoi
# 本项目只是学习用的DEMO.
一个基于SpringBoot3+SpringSecurity6+SpringData JPA+Redis+ElasticSearch的仿牛客网求职就业论坛


本项目已完成

### 已完成的功能

* 使用Redis的set实现点赞，取消点赞✅
* 使用SpringSecurity自定义Security认证流程✅
* 引入JWT Token替代传统Session，采用Redis存储“黑名单”Token✅
* 使用WebSocket实现私聊功能✅
* ElasticSearch实现搜索✅
* 敏感词过滤系统✅
* 自定义帖子热度标准，用Redis进行缓存，并定期刷新✅
* 使用分布式Redis缓存和Caffeine实现多级缓存，避免缓存三兄弟✅
