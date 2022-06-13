FROM openjdk:11-jre-slim-sid

WORKDIR /usr/src/app

ENV SPRING_PROFILES_ACTIVE "dev"

EXPOSE 8080

COPY ./entrypoint.sh .
RUN chmod 755 ./entrypoint.sh
RUN mkdir -p resources
COPY api/src/main/resources/ resources/
COPY build/api.jar the-crm-service.jar

ENTRYPOINT ["./entrypoint.sh"]
