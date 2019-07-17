### spring-with-nacos 
使用embed-tomcat启动，nacos配置中心，
自定义多环境打包，不同环境读取不同nacos配置，通过nacos namespace 隔离，logback多环境日志 
默认读取resource/application.xml
 - dev 开发环境
 读取 application-dev.xml 配置文件 其中定义了nacos 的 namespace 为 dev
 - test 测试环境
 读取 application-test.xml 配置文件 其中定义了nacos 的 namespace 为 test
 - prod 正式环境
 读取 application-prod.xml 配置文件 其中定义了nacos 的 namespace 为 prod
 
### 多环境打包


















