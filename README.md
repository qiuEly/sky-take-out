# 食用指南
## 因为个人账户不能支持微信小程序支付，本仓库已经省略微信支付功能，前端小程序项目已经进行修改来跳过支付功能
- 后端服务器部署
  1. git pull 本仓库之后修改自己的application配置即可，具体的application-dev.yml文件如下
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
之后使用maven进行打包，打包过程中出现的问题要学会自己google一下
2. 准备服务器并开启自己想要的端口
3. 配置java环境 jdk17 也可以使用docker，具体自己解决
4. java -jar xxx.jar &

（1）执行java -jar xxx.jar后

（2）ctrl+z 退出到控制台,执行 bg

（3）exit
  即可进行后台运行后端程序
- 前端管理页面部署
  本地部署下载本地部署的nginx包，将其放在没有中文的包下，点击nginx.exe即可
  服务器部署，根据本仓库的nginx.conf文件进行配置即可，不会配置文件的--自己去学--
  修改html文件中所有的socketUrl 为你的服务器ip即可，端口在nginx.conf中配置 （webstorm 全局修改使用 ctrl + shift + r )
  copy html 文件夹中的内容到 /usr/share/nginx/html中即可

