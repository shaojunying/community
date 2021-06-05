# the first stage of our build will use a maven 3.6.1 parent image
FROM maven:3.6.1-jdk-8-alpine
# copy the source tree and the pom.xml to our new container
COPY ./ ./

# package our application code
RUN mvn clean package -Dmaven.test.skip=true

# set the startup command to execute the jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]