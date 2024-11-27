FROM ghcr.io/graalvm/native-image-community:22-muslib as build

RUN microdnf install wget 

RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.6.1.tar.gz \
    && rm dockerize-linux-amd64-v0.6.1.tar.gz
RUN tar xvf apache-maven-3.9.9-bin.tar.gz

ENV M2_HOME=/app/apache-maven-3.9.9
ENV M2=$M2_HOME/bin
ENV PATH=$M2:$PATH

RUN mkdir -p /app/calc
COPY . /app/calc

RUN cd /app/calc && mvn clean package 

WORKDIR /app

FROM eclipse-temurin:23.0.1_11-jre-noble

WORKDIR /app

COPY --from=build /usr/local/bin /usr/local/bin
COPY --from=build /app/calc/target/calculator-1.0.0.jar .

ENV DB_HOST=db:3306
ENV DB_USERNAME=root
ENV DB_PASSWORD=admin
ENV ORIGIN1=fe:3000
ENV ORIGIN2=app:8080

EXPOSE 8080

CMD ["sh", "-c", "dockerize -wait tcp://$DB_HOST -timeout 60s && sleep 5 && java -Dallowed_origin1=$ORIGIN1 -Dallowed_origin2=$ORIGIN2 -Ddb_url=jdbc:mysql://$DB_HOST/ntd -Ddb_username=$DB_USERNAME -Ddb_password=$DB_PASSWORD -Dspring.profiles.active=prod -Xmx256m -jar calculator-1.0.0.jar" ]

