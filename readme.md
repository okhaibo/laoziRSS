 <p align="center">
   <a href="https://github.com/TyCoding/boot-chat/" target="_blank">
    <img src="https://img.shields.io/badge/telegrambots-4.9-lightred.svg" alt="Build Status">
   </a>
   <img src="https://img.shields.io/badge/Mybatis%20Plus-3.3.2-orange.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Rometools-1.8.0-yellow.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/MySQL-8.0.19-green.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Spring%20Boot-2.3.1.RELEASE-yellowgreen.svg" alt="Downloads">
   <img src="https://visitor-badge.glitch.me/badge?page_id=okhaibo.laoziRSS" alt="Coverage Status">

 ## 使用
 1. 下载 [release](https://github.com/okhaibo/laoziRSS/releases) 版本的jar包
 2. 导入rssbot.sql到MySQL数据库中
 3. 下载 `src/main/resources/application-online.yml` ,并根据实际环境填写里面的内容
 4. 将application-online.yml和jar放在同一文件夹下，运行jar包.

  > win10 双击可直接运行；其余系统可通过`java -jar rssgo-0.0.1-SNAPSHOT.jar` 运行。

  运行成功将生成logs文件夹，可以通过log文件查看服务的运行状况

  支持命令
  * `/sub url` 或直接发送url进行订阅
  * `/unsub url` 退订指定url
  * `/list` 获取当前用户的所有订阅
  * `/timer 数字(1-59)` 设置更新频率，如 /timer 5 设置每隔5分钟抓取一次订阅源的更新(默认5分钟) 
  
  * 频道订阅 `/sub @channelId url`
  * 频道退订 `/unsub @channelId url`
  * 频道列表 `/list @channelId`
  > 必须将bot加入到频道内，并设置为频道的管理员



 ## Demo

**注** ：由于目前测试Bot搭建在私人电脑上，电脑偶尔关机或proxy不可用时，可能无响应

频道 Demo：https://t.me/hiboc

测试 Bot：https://t.me/laoziRSSbot



 ![](https://github.com/okhaibo/laoziRSS/raw/master/demo1.png)

 ![](https://github.com/okhaibo/laoziRSS/raw/master/demo2.png)  

 ![](https://github.com/okhaibo/laoziRSS/raw/master/demo3.png) 

 ![](https://github.com/okhaibo/laoziRSS/raw/master/demo4.png)  

 ![](https://github.com/okhaibo/laoziRSS/raw/master/demo5.png)  


**TODO**
- [x] /sub

- [x] /unsub

- [x] /list

- [x] /timer

- [x] 多用户

- [x] 多订阅源

- [x] 支持多种模式消息(纯文字、单图、多图)

- [x] 微博emoji转换

- [x] 文字过多的文章截取一部分内容推送

- [x] 支持频道订阅

- [x] 整理proxy设置

- [x] 管理员权限

- [x] 图片发送失败处理

- [ ] 后台网页对订阅源更丰富的配置

- [ ] 记录并重发由于握手失败导致发送失败的消息

- [ ] 添加docker部署

- [ ] 自定义消息模板

- [ ] 数据库可选，目前默认MySQL

- [ ] 支持telegraph, instant view

- [ ] 批量导入/出订阅源

- [ ] 后台数据可视化(也许吧)

  
