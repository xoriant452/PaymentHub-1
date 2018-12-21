#!/bin/bash
nohup java -jar -Dserver.port=8882 -Dspring.config.location=/disk2/softwares/kafka-cluster-sub-ssl/application.properties -jar /disk2/softwares/kafka-cluster-sub-ssl/PaymentHub2.0-0.0.1-SNAPSHOT.jar  > /tmp/log.txt 2>&1 &
echo $! > /disk2/softwares/kafka-cluster-sub-ssl/pid.file
