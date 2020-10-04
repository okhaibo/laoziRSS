 <p align="center">
   <a href="https://github.com/TyCoding/boot-chat/" target="_blank">
    <img src="https://img.shields.io/badge/telegrambots-4.9-lightred.svg" alt="Build Status">
   </a>
   <img src="https://img.shields.io/badge/Mybatis%20Plus-3.3.2-orange.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Rometools-1.8.0-yellow.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/MySQL-8.0.19-green.svg" alt="Build Status">
   
   <img src="https://img.shields.io/badge/Spring%20Boot-2.3.1.RELEASE-yellowgreen.svg" alt="Downloads">
   <img src="https://visitor-badge.glitch.me/badge?page_id=okhaibo.laoziRSS" alt="Coverage Status">
 </p>
 
 ## 使用
 1. 下载 [release](https://github.com/okhaibo/laoziRSS/releases) 版本的jar包
 2. 下载 `src/main/resources/application-online.yml` ,并根据实际环境填写里面的内容
 3. 运行jar包.
 
  > win10 双击可直接运行；其余系统可通过`java -jar rssgo-0.0.1-SNAPSHOT.jar` 运行。
  
  运行成功将生成logs文件夹，可以通过log文件查看服务的运行状况
 
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
- [ ] 数据库可选，目前默认MySQL
- [ ] 支持telegraph, instant view
 
![](https://github.com/okhaibo/laoziRSS/raw/master/demo.png)  
