#!/bin/bash

export HOST_IP=`/sbin/ifconfig -a | grep inet | grep -v 127.0.0.1 | grep -v 172.1 | grep -v 172.20 | grep -v 10.0 | grep -v inet6 | awk '{print $2}' | tr -d "addr:"`

echo "HOST_IP: ${HOST_IP}"

touch ./docker/env/docker-compose.yaml
#touch ../data/logstash/logstash.conf

envsubst < ./docker/env/docker-compose-template.yaml > ./docker/env/docker-compose.yaml
#envsubst < ../data/logstash/logstash-template.conf > ../data/logstash/logstash.conf

cd ./docker/env/

docker-compose up -d mysql

echo "Mysql start successfully!"