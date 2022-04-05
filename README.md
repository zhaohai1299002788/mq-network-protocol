# Mq Network Protocol


**[Mq Network Protocol](https://github.com/zhaohai1299002788/mq-network-protocol) 提供支持轻量级的http协议消息队列MQ。目标就是更轻量级，让web页面或者app应用可以直接使用http协议，无需通过服务端进行支持，更轻量级的解耦合。发送和消费本质还是基于TCP协议，设计理念是二次消费。现阶段支持MySQL和HBase存储二次消费消息，后续支持Redis。**

----------
* 可以通过LUR脚本 + http协议直接进行秒杀请求，这样不需要通过业务系统就可以实现流量削峰。更好的与业务解耦，防止因为秒杀流量过大导致业务系统崩溃
* 可以让页面的埋点数据和埋点日志直接通过http请求发送到MQ中，再通过大数据系统消费MQ数据进行用户画像和用户行为分析

----------

* 目前支持RocketMQ的消息队列
* 后续提供KafKa的消息队列支持