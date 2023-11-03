# 食用指南
## 因为个人账户不能支持微信小程序支付，本仓库已经省略微信支付功能
- 本地部署
  git pull 本仓库之后修改自己的application配置即可，具体的application-dev.yml文件如下
  ```
  sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: 
    port: 
    database:
    username: 
    password: 
  alioss: # 阿里云OSS配置，官方文档很清楚
    access-key-secret: 
    access-key-id:
    endpoint: 
    bucket-name: 
 #redis
  redis:
    host: 
    port:
    password:
    database: 
  wechat:
    appid: 
    secret: 
    ```
之后使用maven打包
