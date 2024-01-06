# Mantoi
# THIS PROJECT IS WORKING IN PROGRESS, THERE ARE STILL MANY PROBLEMS TO SOLVE.
一个基于SpringBoot3+SpringSecurity6+SpringData JPA+Redis+ElasticSearch的仿牛客网求职就业论坛

本项目灵感来源于https://github.com/cosen1024/community.
在上面有所改动和修改。

Working on it.

### 已完成

* 使用Redis的set实现点赞，取消点赞✅
* 使用SpringSecurity自定义Security认证流程✅
* 引入JWT Token替代传统Session，采用Redis存储“黑名单”Token✅
* 使用WebSocket实现私聊功能✅



### TODO

* ElasticSearch实现搜索✅
* 敏感词过滤系统✅
* Nginx实现反向代理
* 搞点分布式
* 使用消息队列对事件进行处理，并进行封装
* 自定义帖子热度标准，用Redis进行缓存，并定期刷新✅
* 使用分布式Redis缓存和Caffeine实现多级缓存，避免缓存三兄弟
* 重构实体类的Repository，放弃使用SpringData JPA
