FROM php:7.3-fpm-alpine
RUN echo http://dl-cdn.alpinelinux.org/alpine/v3.6/main >> /etc/apk/repositories &&\
    apk --no-cache --update add \
        git \
        gcc \
        g++ \
        make \
        zip \
        autoconf \
        rabbitmq-c-dev \
        mariadb-client &&\
    pecl install amqp &&\
    docker-php-ext-enable amqp &&\
    docker-php-ext-install pdo_mysql

VOLUME /var/www/app
WORKDIR /var/www/app
