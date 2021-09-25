# Email Sender

## Prerequisites for Coding or running in Intellij

- Java 11
- Maven 3.6.0 (or above)
- IntelliJ IDEA with Lombok IDEA Plugin
- Docker installed locally

## Running in Docker containers
You will need 
- Java 11
- Maven 3.6.0 (or above)
- Docker installed locally

### 1. Clone repo.

### 2. Do a maven build
Run the following
```bash
mvn clean verify
```

### 3. Docker build the Emailer
The following will create an image called tim/emailer with Tag "latest".

```bash
make app-build
```

### 4. Run the Dockerised Emailer
```bash
make app-run
```

Now verify that it has started:
```bash
docker logs emailer
```

You will see something like this:
```bash
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.5)

2021-09-25 09:48:18.467  INFO 1 --- [           main] com.example.emailer.EmailerApplication   : Starting EmailerApplication v0.0.1-SNAPSHOT using Java 11.0.12 on f4c249a9a3b0 with PID 1 (/app/app.jar started by root in /app)
2021-09-25 09:48:18.473  INFO 1 --- [           main] com.example.emailer.EmailerApplication   : No active profile set, falling back to default profiles: default
2021-09-25 09:48:21.220  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-09-25 09:48:21.246  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-09-25 09:48:21.247  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.53]
2021-09-25 09:48:21.403  INFO 1 --- [           main] o.a.c.c.C.[.[localhost].[/emailer]       : Initializing Spring embedded WebApplicationContext
2021-09-25 09:48:21.404  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2797 ms
2021-09-25 09:48:22.642  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath base path '/actuator'
2021-09-25 09:48:22.719  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/emailer'
2021-09-25 09:48:22.743  INFO 1 --- [           main] com.example.emailer.EmailerApplication   : Started EmailerApplication in 5.62 seconds (JVM running for 6.709)

```



### 6. Run Tests in src/test/http/rest-http.http
You can either run these in Intellij by hitting the play button, or copy into Postman.


### 7. Cleanup

Destroy the emailer container
```bash
make app-cleanup
```

## Running in Intellij
Import into Intellij

Run EmailerApplication with a Spring profile of dev.

Run the tests in : src/test/http/rest-http.http
As per instructions above.



## TODOs

### This is not deployed
Sorry I did not have time to deploy them somewhere (my free AWS account expired a while ago).

### Add 2nd Email Client
SendMail rejected my application for an email account saying it was suspicious. I tried others but one could send but nothing arrived, 
another only seems to do validations (whatever that is exactly).

So I just added a placeholder for the 2nd email client and included it in _RetryableEmailWithFallbackSender_.
As long as we code to conform with the interface it will work.

### Complete Error handling
I didn't completely test the error scenarios. I guess I need to check the 

### More Automated tests
Add Failsafe plugin to Maven, and separate Integration from Unit tests.

Add Integration tests covering Controllers all the way to Wiremocks.

### Consider Circuit Breaker
I tried to do it but it was taking too long. Specifically in _RetryableEmailWithFallbackSender_ we have a Retry with a fallback of another Retry, 
which falls back onto a standard exception. 

The challenge is putting a circuit breaker across the whole lot. A decorator class around _RetryableEmailWithFallbackSender_ should work. But it needs to 
communicate the failure upwards by throwing a specific exception.

### Retry : specify exception
We want to retry on certain exceptions, or rather ignore certain exceptions.
For example we want to ignore all 4XX errors from the emailClients because we are not going to recover from that.


### Externalize Timeouts Etc
Need to go through application and move Timeouts etc into application.yml For example: WebConfig

Use Configuration Properties and get Spring to map values into a class.

## Further Design changes
This is a synchronous service at the moment. It might be a better option to 
put the requests into a queue, and return a notification to the user. Some other process
can then pull the messages from the queue and do the sending. 

This kind of design would speed up the response to the user and allow each instance to deal with a higher 
number of requests.