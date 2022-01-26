FROM debian:buster-slim

RUN apt update -y
RUN apt install maven -y
RUN apt install openjdk-11-jdk -y
RUN apt-get install make -y
RUN apt-get install psmisc -y

ADD pom.xml /
ADD build_script.sh /
ADD makefile /
ADD libs /libs
ADD robot-worlds-server /robot-worlds-server
ADD robot-worlds-client /robot-worlds-client

EXPOSE 7000
