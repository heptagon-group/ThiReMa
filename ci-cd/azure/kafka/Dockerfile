FROM ThiReMaRegistry2.azurecr.io/ubuntu-env

WORKDIR /home/confluent

COPY ./confluent .
COPY ./init.sh .

# zookeeper
EXPOSE 2181 
# kafka server
EXPOSE 9092
# avro registry
EXPOSE 8081
# control center
EXPOSE 9021

RUN chmod -R 777 .
ENV CONFLUENT_HOME=/home/confluent
ENV PATH=$PATH:$CONFLUENT_HOME/bin

# download and put into ./confluent
RUN ./install_confluent_CLI.sh 
