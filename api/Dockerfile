FROM node:10

RUN sleep 3

WORKDIR /api

COPY ./api ./

CMD ["apt", "install"]
CMD ["apt", "update"]
CMD ["apt", "install", "maven"]

RUN ./mvnw compile quarkus:dev

EXPOSE 8080