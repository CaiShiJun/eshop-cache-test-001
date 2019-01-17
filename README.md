1、启动 Redis 集群：

eshop-cache-01：

sh /etc/init.d/redis_7001 start
sh /etc/init.d/redis_7002 start

eshop-cache-02：

sh /etc/init.d/redis_7003 start
sh /etc/init.d/redis_7004 start

eshop-cache-03：

sh /etc/init.d/redis_7005 start
sh /etc/init.d/redis_7006 start
sh /etc/init.d/redis_7007 start
sh /etc/init.d/redis_7008 start



2、检测 Redis 集群启动状态：

eshop-cache-01：

redis-trib.rb check 192.168.74.101:7001



3、启动 Mysql 数据库。

eshop-cache-04：

已设置为开机自动启动：chkconfig mysqld on
启动Mysql数据库（一般不需要自己手动启动）：service mysqld start



4、启动 Zookeeper 集群：

eshop-cache-01、eshop-cache-02、eshop-cache-03：

sh /usr/local/zk/bin/zkServer.sh restart



5、检查 Zookeeper 集群启动状态：

eshop-cache-01、eshop-cache-02、eshop-cache-03：

sh /usr/local/zk/bin/zkServer.sh status



6、启动 Kafka 集群：

shop-cache-01、eshop-cache-02、eshop-cache-03：

nohup /usr/local/kafka/bin/kafka-server-start.sh /usr/local/kafka/config/server.properties &



7、检查 Kafka 集群启动状态：

eshop-cache-02、eshop-cache-03：

/usr/local/kafka/bin/kafka-topics.sh --zookeeper eshop-cache-01:2181,eshop-cache-02:2181,eshop-cache-03:2181 --topic test --replication-factor 1 --partitions 1 --create

/usr/local/kafka/bin/kafka-console-producer.sh --broker-list eshop-cache-01:9092,eshop-cache-02:9092,eshop-cache-03:9092 --topic test

/usr/local/kafka/bin/kafka-console-consumer.sh --zookeeper eshop-cache-01:2181,eshop-cache-02:2181,eshop-cache-03:2181 --topic test --from-beginning



8、启动 nginx : 
/usr/servers/nginx/sbin/nginx



9、检查 nginx 启动状态：
ps -ef | grep nginx

检测 nginx 配置文件是否正确：
/usr/servers/nginx/sbin/nginx -t
重新加载 nginx 配置文件：
/usr/servers/nginx/sbin/nginx -s reload  



10、启动 storm 集群：

eshop-cache-01：

storm nimbus >/dev/null 2>&1 &
storm supervisor >/dev/null 2>&1 &
storm logviewer >/dev/null 2>&1 &
storm ui >/dev/null 2>&1 &

eshop-cache-02：

storm supervisor >/dev/null 2>&1 &
storm logviewer >/dev/null 2>&1 &

eshop-cache-03：

storm supervisor >/dev/null 2>&1 &
storm logviewer >/dev/null 2>&1 &



11、启动 storm 热点商品拓扑任务：

eshop-cache-01：

storm jar /usr/local/eshop-storm-1.0-SNAPSHOT.jar org.github.caishijun.eshop.storm.HotProductTopology HotProductTopology

storm kill HotProductTopology
















