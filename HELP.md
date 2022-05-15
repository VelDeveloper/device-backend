# Getting Started

## Prometheus Configuration
------------------------
* Step 1: `docker pull prom/prometheus`
* Step 2: `docker run -d --name=prometheus -p 9090:9090 -v /Users/vadivelmurugan/spring-projects/device-backend/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml`
* Step 3: `docker container ls`


Prometheus supports 4-core types of metrics:
1) counters => its a cumulative metrics that keeps on increasing ex) no-of visits in website
2) Gauges => What is the current value of something rightnow ex) Temperature
3) Histogram => how big some thing is or how long it takes ex) request duration
4) Summary => Latest percentile of number of requests

## GRAFANA
--------
* Step 1: `docker run -d --name=grafana -p 3000:3000 grafana/grafana`
* Step 2: `docker container ls`

## Credentials
-----------
* username: admin
* password: admin

Get IP address of mac => `ipconfig getifaddr en0`

Go to `Grafana-> import -> 12900 and select data-source prometheus`
use Grafana ID:12900 for spring-boot. you can find several Grafana dashboard in Grafana dashboard section

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.7/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.7/maven-plugin/reference/html/#build-image)
* [Testcontainers MongoDB Module Reference Guide](https://www.testcontainers.org/modules/databases/mongodb/)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-spring-hateoas)
* [Testcontainers](https://www.testcontainers.org/)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-mongodb)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#production-ready)
* [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#using-boot-devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

