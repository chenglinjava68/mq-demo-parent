# mq-demo-parent

## 常用Message Queue对比

**RabbitMQ**

RabbitMQ是使用Erlang编写的一个开源的消息队列，本身支持很多的协议：AMQP，XMPP, SMTP, STOMP，也正因如此，它非常重量级，更适合于企业级的开发。
同时实现了Broker构架，这意味着消息在发送给客户端时先在中心队列排队。
对路由，负载均衡或者数据持久化都有很好的支持。

**Redis**
Redis是一个基于Key-Value对的NoSQL数据库，开发维护很活跃。
虽然它是一个Key-Value数据库存储系统，但它本身支持MQ功能，所以完全可以当做一个轻量级的队列服务来使用。
对于RabbitMQ和Redis的入队和出队操作，各执行100万次，每10万次记录一次执行时间。

入队时，当数据比较小时Redis的性能要高于RabbitMQ，而如果数据大小超过了10K，Redis则慢的无法忍受；
出队时，无论数据大小，Redis都表现出非常好的性能，而RabbitMQ的出队性能则远低于Redis。

**ZeroMQ**

ZeroMQ号称最快的消息队列系统，尤其针对大吞吐量的需求场景。
ZMQ能够实现RabbitMQ不擅长的高级/复杂的队列，但是开发人员需要自己组合多种技术框架，技术上的复杂度是对这MQ能够应用成功的挑战。
ZeroMQ具有一个独特的非中间件的模式，你不需要安装和运行一个消息服务器或中间件，因为你的应用程序将扮演了这个服务角色。
你只需要简单的引用ZeroMQ程序库，可以使用NuGet安装，然后你就可以愉快的在应用程序之间发送消息了。
但是ZeroMQ仅提供非持久性的队列，也就是说如果down机，数据将会丢失。其中，Twitter的Storm中默认使用ZeroMQ作为数据流的传输。

**ActiveMQ**

ActiveMQ是Apache下的一个子项目。 
类似于ZeroMQ，它能够以代理人和点对点的技术实现队列。
同时类似于RabbitMQ，它少量代码就可以高效地实现高级应用场景。

**Kafka/Jafka**

Kafka是Apache下的一个子项目，是一个高性能跨语言分布式Publish/Subscribe消息队列系统，而Jafka是在Kafka之上孵化而来的，即Kafka的一个升级版。
具有以下特性：

_快速持久化_，可以在O(1)的系统开销下进行消息持久化；
_高吞吐_，在一台普通的服务器上既可以达到10W/s的吞吐速率；
_完全的分布式系统_，Broker、Producer、Consumer都原生自动支持分布式，自动实现复杂均衡；
_支持Hadoop数据并行加载_，对于像Hadoop的一样的日志数据和离线分析系统，但又要求实时处理的限制，这是一个可行的解决方案。

Apache Kafka相对于ActiveMQ是一个非常轻量级的消息系统，除了性能非常好之外，还是一个工作良好的分布式系统。


**效率对比：**
以下是以10万条数据

消息队列            数据总大小               总消耗时间                   每100条消耗时间 
kafka               106byte                      3s                             3ms
RabbitMQ            106byte                      19s                            19ms
redis               106byte                      250s                           3ms
 

消息队列            数据总大小               总消耗时间                    每100条消耗时间          
kafka              3103byte（0.3k）               14s                             14ms
RabbitMQ           3103byte（0.3k）               93s                             93ms
redis              3103byte（0.3k）               297s                            297ms


消息队列            数据总大小               总消耗时间                    每100条消耗时间          
kafka               3000103byte（0.3M）         12000s（200min）（3h）            12000ms（12s）
RabbitMQ            3000103byte（0.3M）         11000s(183min)(3h)                11000ms(11s)   
redis               3000103byte（0.3M）         13000s（216.7min）（3.6h）        13000ms（13s）


消息队列            数据总大小               总消耗时间                    每100条消耗时间                 
kafka               27000103byte（3M）          160000s（2666.7min）（44h）       160000ms（160s）（2.7min）
RabbitMQ            27000103byte（3M）          140000s(2333.3min)(38.9h)         140000ms(140s)(2.3min)
redis               27000103byte（3M）          900000s（15000min）（250h）       900000ms（900s）（15min）