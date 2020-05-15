#!/bin/bash
# this script is used to refresh the repository and restart the
# deployment
cd /home/Heptagon-Code/backend
echo "---refreshing repository----"
kill $(cat pidCM.file) # kill last running app
kill $(cat pidFC.file) # kill last running app
kill $(cat pidDP.file) # kill last running app
kill $(cat pidTB.file) # kill last running app
kill $(cat pidGS.file) # kill last running app
kill $(lsof -t -i:8080)

# refresh project
cd /home/Heptagon-Code
git fetch --all
git reset --hard origin/rq
git pull
cd backend/
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
mvn clean install --file ./data-processor/pom.xml
mvn clean install --file ./telegram-bot/pom.xml
mvn clean install --file ./gateway-simulator/pom.xml

#start only frontend-controller
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