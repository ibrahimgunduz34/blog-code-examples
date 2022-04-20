# Symfony Messenger Demo Application

It's created for the blog post [https://medium.com/weekly-webtips/using-rabbitmq-in-a-symfony-application-through-messenger-component-e61498b668b](https://medium.com/weekly-webtips/using-rabbitmq-in-a-symfony-application-through-messenger-component-e61498b668b)

## Prerequisites
* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/)

## Starting The Application
Run the following command to start the application containers
```shell 
$ cd dev
$ docker-compose up
```

## Accessing The RabbitMQ Server

The RabbitMQ instance will be started as a container by the docker-compose. 
You can access the instance easily by typing the `http://localhost:15672`on your browser.

## Monitoring The Consumer
You can run the command below to see the consumer output whenever it received a new message.

```shell 
$ cd dev
$ docker-compose logs -f consumer.local
```
 
## Producing A Sample Message

We put the code that produce the message in a controller called `src/Controller/DefaultController.php`
You can produce a sample message by sending a GET request to `/registration/complete` endpoint.

```shell
$ curl http://localhost:8080/registration/complete
```




