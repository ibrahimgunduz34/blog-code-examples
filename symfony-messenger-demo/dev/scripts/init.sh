#!/usr/bin/env sh

# Create demo database to save the failed messages
mysql -h mariadb.local \
  --user=root \
  --password=root \
  -e "CREATE DATABASE messenger_demo COLLATE utf8_general_ci; GRANT ALL PRIVILEGES ON messenger_demo.* TO 'root'@'%'; "
