#!/bin/bash
# this script is used to init the enviroment
# start hook server
python3 ./hookServer.py -p 1234 -s ./refresh.sh &

# start project
git clone --depth=1 --single-branch --branch ra git@github.com:heptagon-group/Heptagon-Code.git
cd /home/Heptagon-Code/backend/
# configure database
sed -i "s/localhost:5432/$DBURL:5432/g" ./frontend-controller/src/main/resources/application.properties
sed -i "s/localhost:5432/$DBURL:5432/g" ./data-processor/src/main/resources/application.properties
sed -i "s/localhost:5432/$DBURL:5432/g" ./telegram-bot/src/main/resources/application.properties
# configure kafka
sed -i "s/localhost:9092/$KAFKA_URL:9092/g" ./frontend-controller/src/main/resources/application.properties
sed -i "s/localhost:9092/$KAFKA_URL:9092/g" ./data-processor/src/main/resources/application.properties
sed -i "s/localhost:9092/$KAFKA_URL:9092/g" ./telegram-bot/src/main/resources/application.properties
sed -i "s/localhost:9092/$KAFKA_URL:9092/g" ./gateway-simulator/src/main/resources/application.properties
# configure schema registry
sed -i "s/schema.registry.url=.*/schema\.registry\.url=http:\/\/$REGISTRYURL:8081/" ./data-processor/src/main/resources/application.properties
sed -i "s/schema.registry.url=.*/schema\.registry\.url=http:\/\/$REGISTRYURL:8081/" ./gateway-simulator/src/main/resources/application.properties
# configure data-processor redirection
sed -i "s/localhost:8090/$DATAPROCESSORURL/g" ./frontend-controller/src/main/resources/application.properties

# build
mvn clean install --file ./commons/pom.xml
mvn clean install --file ./frontend-controller/pom.xml
cd data-processor
mvn schema-registry:register
cd ..
mvn clean install --file ./data-processor/pom.xml
mvn clean install --file ./telegram-bot/pom.xml
mvn clean install --file ./gateway-simulator/pom.xml

cd commons
mvn spring-boot:run &
echo $! > ../pidCM.file # so I can kill it later
cd ../frontend-controller
mvn spring-boot:run &
echo $! > ../pidFC.file # so I can kill it later
cd ../data-processor
mvn spring-boot:run &
echo $! > ../pidDP.file # so I can kill it later
cd ../telegram-bot
mvn spring-boot:run &
echo $! > ../pidTB.file # so I can kill it later
cd ../gateway-simulator
mvn spring-boot:run &
echo $! > ../pidGS.file # so I can kill it later
cd ..
/bin/bash
tail -f /dev/null