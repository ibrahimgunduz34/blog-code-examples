# Spring Boot: A Practical Guide to Building Review Environments for Feature Branches

Blog post: 
[https://dev.to/ibrahimgunduz34/spring-boot-a-practical-guide-to-building-review-environments-for-feature-branches-10fd](https://dev.to/ibrahimgunduz34/spring-boot-a-practical-guide-to-building-review-environments-for-feature-branches-10fd)

Once you initialized Docker Swarm, you can create an example deployment by running `./run-example-deployment.sh`.

Next, call the test endpoint from the deployed instance a few seconds later after the deployment

```shell
$ curl -H "Host: MF-443.example.com" http://127.0.0.1

Current version is: MF-443
```