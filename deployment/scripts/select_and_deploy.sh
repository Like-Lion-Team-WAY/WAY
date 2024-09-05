#!/bin/bash

# 서버의 호스트 이름 확인
HOSTNAME=$(hostname)

if [[ "$HOSTNAME" == "ip-172-31-1-210.ap-northeast-2.compute.internal" ]]; then
  # 서버1에 해당하는 JAR 파일을 배포
  echo "Deploying for Server 1"
  sudo cp /home/ec2-user/app/SERVER1-WAY-0.0.1-SNAPSHOT.jar /home/ec2-user/app/application.jar
elif [[ "$HOSTNAME" == "ip-172-31-3-37.ap-northeast-2.compute.internal" ]]; then
  # 서버2에 해당하는 JAR 파일을 배포
  echo "Deploying for Server 2"
  sudo cp /home/ec2-user/app/SERVER2-WAY-0.0.1-SNAPSHOT.jar /home/ec2-user/app/application.jar
else
  echo "Unknown server. Exiting deployment."
  exit 1
fi
