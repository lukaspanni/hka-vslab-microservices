# hka-vslab-microservices

## Creating database with Docker
```shell
docker run -d --name <testdb> -p 3306:3306 -e MYSQL_ROOT_PASSWORD=<> mysql
docker exec -it <testdb> mysql -h 127.0.0.1 -P 3306 -p
```
