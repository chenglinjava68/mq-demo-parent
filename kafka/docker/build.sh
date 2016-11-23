docker pull wurstmeister/zookeeper
docker pull wurstmeister/kafka
docker run --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper
docker run --name kafka -e HOST_IP=localhost -e KAFKA_ADVERTISED_PORT=9092 -e KAFKA_BROKER_ID=1 -e ZK=zk -p 9092:9092 --link zookeeper:zk -t wurstmeister/kafka