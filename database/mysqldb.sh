podman run --replace -dt --memory=512m --cpus=0.75 -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_USER=ntd -e MYSQL_DATABASE=ntd --name mysql -p 3306:3306 mysqldbdb
