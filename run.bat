docker-compose down
docker-compose up --detach
timeout /t 10 /nobreak
cd api
./mvnw compile quarkus:dev
pause