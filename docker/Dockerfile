FROM maven:3.6.3-openjdk-15 AS build
COPY pom.xml /pom.xml
COPY src /src/
RUN mvn clean package -f /pom.xml

FROM openjdk:15
ENV BOT_TOKEN=UNSET
RUN mkdir /config/
COPY --from=build /target/LogBot-jar-with-dependencies.jar /LogBot.jar
CMD /usr/bin/java -jar /LogBot.jar $BOT_TOKEN