# Mantoi
一个基于SpringBoot3+SpringSecurity6+SpringData JPA+Redis+ElasticSearch的仿牛客网求职就业论坛，正在开发中...

正在设想的新功能：
1. 支持OAuth2登录，并可以通过QQ、微博来登录
2. 支持头像的显示
3. 优化倒排索引的位图性能和内存空间优化
...

### 已完成的功能

* 使用Redis的set实现点赞，取消点赞✅
* 使用SpringSecurity自定义Security认证流程✅
* 引入JWT Token替代传统Session，采用Redis存储“黑名单”Token✅
* ElasticSearch实现搜索✅
* 自定义实现倒排索引，不用ES也可以搜索✅
* 敏感词过滤系统✅
* 自定义帖子热度标准，用Redis进行缓存，并定期刷新✅
* 使用分布式Redis缓存和Caffeine实现多级缓存，避免缓存三兄弟✅
