./generate_jwtKeys.sh
cd product-service
mvn clean compile package
cd ../auth-service
mvn clean compile package
cd ../
docker compose up