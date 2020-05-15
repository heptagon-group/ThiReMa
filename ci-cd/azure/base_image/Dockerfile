FROM ubuntu:latest

WORKDIR /home

# install jre and jdk for java 11
RUN apt-get update
RUN apt install -y default-jre
RUN apt install -y default-jdk
RUN apt install -y maven
RUN apt install -y git
RUN apt install -y wget
RUN apt install -y software-properties-common
RUN add-apt-repository -y ppa:deadsnakes/ppa
RUN apt install -y python3.7
RUN apt install -y curl
RUN curl -sL https://deb.nodesource.com/setup_13.x | bash -
RUN apt install -y nodejs
RUN apt install -y lsof
