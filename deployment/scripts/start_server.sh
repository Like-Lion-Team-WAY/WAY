#!/bin/bash
# Navigate to the directory where the JAR file is located
cd /home/ec2-user/app
echo "init" > start_server_log.txt
# Start the new JAR file
if [[ "$HOSTNAME" == "ip-172-31-1-210.ap-northeast-2.compute.internal" ]]; then
serverName=myServerName kafkaGroupId=group1 nohup java -jar application.jar > applog.txt 2> errorlog.txt &
sudo echo "Application started." >> start_server_log.txt
else
serverName=myServerName2 kafkaGroupId=group2 nohup java -jar application.jar > applog.txt 2> errorlog.txt &
sudo echo "Application started." >> start_server_log.txt
fi
