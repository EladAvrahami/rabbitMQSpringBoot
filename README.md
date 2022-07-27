# rabbitMQ SpringBoot
Java Messaging Service (JMS) with Spring Framework. Asynchronous Messaging, Queue, Exchange &amp; Routing Of Messages

*** part2 include app.proprties with stucture of cloud and local hostsconfiguration ***

implementation 'org.springframework.boot:spring-boot-starter-amqp'

<br/>
reply-code=403, reply-text=ACCESS_REFUSED - access to exchange 'amq.default' in vhost 'RabbitWrapperDevMain' refused for user 'blabla'
<br/>
https://www.rabbitmq.com/access-control.html - search ACCESS_REFUSED here
<br/>
https://stackoverflow.com/questions/54269896/rabbitmq-config-queues-listener-in-yaml-file - ymal config to rabbit
<br/>
https://societe-generale.github.io/rabbitmq-advanced-spring-boot-starter/#rabbitmq-auto-configuration - auto-configuration ymal
<br/>
https://www.baeldung.com/spring-boot-yaml-vs-properties -ymal file structure

operator+spring : https://springone.io/2021/sessions/building-an-operator-in-java
<br/>
https://springframework.guru/spring-boot-messaging-with-rabbitmq/
<br/>
How to set springBoot profiles : <a href="https://dzone.com/articles/spring-boot-profiles-1" target="_blank">Here</a> or 
<a href="https://www.javacodegeeks.com/2019/07/profiles-spring-boot-application.html#respond" target="_blank">Here2</a>

### also good document : https://springframework.guru/spring-boot-messaging-with-rabbitmq/

### Using RabbitMQ Messaging Topology Kubernetes Operator:
https://www.rabbitmq.com/kubernetes/operator/using-topology-operator.html
### Using the RabbitMQ Kubernetes Operators on Openshif
https://www.rabbitmq.com/kubernetes/operator/using-on-openshift.html


## Below is the sample rabbitmq auto configuration.
<pre>
rabbitmq:
  auto-config:
    #enable or disable auto configuration. Default is true
    enabled: true
    
    #Info Headers can be used to add additional information to be added in each message headers
    info-headers:
      source-application: ${spring.application.name}
      
    #Exchange configuration at default level, will be applied to all the missing configuration of each Exchange.  This can be overridden by configuring at each exchange level.  
    default-exchange:
      type: topic
      durable: false
      auto-delete: true
    
    #Queue configuration at default level, will be applied to all the missing configuration of each Queue.  This can be overridden by configuring at each queue level.
    default-queue:
      durable: false
      auto-delete: true
      dead-letter-enabled: true
    
    #Dead Letter Configuration to configure dead letter exchange and queue postfix.
    dead-letter-config:
      dead-letter-exchange:
        name: my-app-dead-letter-exchange-${user.name}
        auto-delete: true
        durable: false
      queue-postfix: .dlq
    
    #Re Queue Configuration to configure the requeue exchange and queue.
    re-queue-config:
      enabled: true
      exchange:
        name: re-queue-exchange-${user.name}
      queue:
        name: re-queue-queue-${user.name}
      routing-key: requeue.key
          
    #You can configure all your exchanges here  
    exchanges:
      exchange-one:
        name: exchange-one-${user.name}
      exchange-two:
        name: exchange-two-${user.name}
      exchange-mock:
        name: exchange-mock-${user.name}
    
    #You can configure all your queue here
    queues:
      queue-one:
        name: queue-one-${user.name}
      queue-two:
        name: queue-two-${user.name}
      queue-mock:
        name: queue-mock-${user.name}
        dead-letter-enabled: false
    
    #You can configure your bindings for the exchanges and queues here    
    bindings:
      binding-one:
        exchange: exchange-one
        queue: queue-one
        routing-key: rkey-one
      binding-two:
        exchange: exchange-two
        queue: queue-two
        routing-key: rkey-two
      binding-mock:
        exchange: exchange-mock
        queue: queue-mock
        routing-key: rkey-mock

</pre>


### person:
<pre>

package com.poalim.rabbitmqintegration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person implements Serializable {
    private String idPerson;
    private String namePerson;


    @Override
    public String toString() {
        return "Person {" +
            "idPerson='" + idPerson + '\'' +
            ", namePerson='" + namePerson + '\'' +
            '}';
    }
}

</pre>


### ProduceController :
<pre>
package com.poalim.rabbitmqintegration.impl;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProduceController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/send-message", method = RequestMethod.POST)
    public String sendMessage(@RequestBody Person person) {
        rabbitTemplate.convertAndSend("tester.exchange", "", person);
        System.out.println(person);
        return "Send message to RabbitMQ successfully completed";
    }

}

</pre>

### consumer
<pre>
package com.poalim.rabbitmqintegration.impl;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@EnableRabbit
public class RabbitMQConsumer {


    @RabbitListener(queues = "test.queue")
    public void getMessage(Person person) {
        System.out.println("RabbitListener-getMessage:" + person);

    }
}
</pre>

### app ymal
<pre>
# Place any custom environment variables relevant for the all environments
#https://www.baeldung.com/spring-boot-yaml-vs-properties -explanation yaml file
#https://github.com/EladAvrahami/rabbitMQSpringBoot/blob/main/README.md - rabbitmq auto configuration

spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: 
    password: 
    virtual-host: Rabbit

</pre>

### curl
<pre>
curl -X POST
http://localhost:8080/api/v1/send-message
-H 'Accept: /'
-H 'Accept-Encoding: gzip, deflate'
-H 'Cache-Control: no-cache'
-H 'Connection: keep-alive'
-H 'Content-Length: 51'
-H 'Content-Type: application/json'
-H 'Host: localhost:8080'
-H 'User-Agent: PostmanRuntime/7.15.2'
-H 'cache-control: no-cache'
-d '{ "idPerson": "ronen", "namePerson": "12345678" }'
</pre>






## program2:
### application.ymal :
<pre>
# a developer message and a stacktrace.
# False by default.
error-handling:
  debug-mode: ${ERROR_HANDLING_DEBUG_MODE:false}
  error-handling.exception-log-level: error
header-forwarding:
  enabled: ${HEADER_FORWARDING_ENABLED:true}
log4j2:
  formatMsgNoLookups: true
logger:
  appender: ${LOGGER_APPENDER:CONSOLE}
  level: ${LOGGER_LEVEL:INFO}
logging:
  config: ${LOGBACK_LOCATION:classpath:logback-spring.xml}
  level:
    org:
      apache:
        http: ${LOGGER_LEVEL:INFO}
      springframework: INFO
management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: health,prometheus
  server:
    port: 8080
server:
  max-http-header-size: ${MAX_SERVER_HEADER_SIZE:32768}
  port: 8080
splunk-sensitive-index-expression: ${SENSITIVE_KEY_WORD_ENV:%SENSITIVE%}
spring:
  application:
    name: rabbitmq-integration
    message: Message has been sent Successfully..
  config:
    import: classpath:impl/application.properties
  sleuth:
    sampler:
      probability: ${SPRING_SLEUTH_SAMPLER_PROBABILITY:1.0}
################################ Pay attention to space (to be included in the spring)
  rabbitmq:
    host: rabbitmq-dev-cluster-ha.rabbitmq-dev.svc.cluster.local
    port: 5672
    username: T #${RABBIT_USERNAME}
    password: T #${RABBIT_PASSWORD}
    virtual-host: RabbitWrapperDevMain
    exchange: tester.exchange
    queue: test.queue
    routingkey: user.routingkey
</pre>

###nodel:
<pre>


@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = User.class)
public class SimpleMessage implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String id;
    private String firstName;
    private String lastName;


    @Override
    public String toString() {
        return "SimpleMessage{" +
            "id='" + id + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}


</pre>

service folder -> ### ConsumeMessageService:
<pre>

@Service
@EnableRabbit
public class ConsumeMessageService {


    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void consumeMessage(SimpleMessage simpleMessage) {
        System.out.println("consumeMessage:" + simpleMessage);

    }
}
</pre>


service folder -> ### ProduceMessageServiceImpl:
<pre>
@Service
@NoArgsConstructor
public class ProduceMessageServiceImpl extends ProduceMessageService {


    private RabbitTemplate rabbitTemplate;

    /**
     * autowires an rabbitTemplate object of the RabbitTemplate class.
     * The RabbitTemplate class allows sending and receiving messages with RabbitMQ.
     * @param rabbitTemplate
     */
    @Autowired
    public ProduceMessageServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    //@Value("${spring.rabbitmq.user.routingkey}")
    //private String routingkey;


    public GenericResponse produceMessage(Person person) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            SimpleMessage simpleMessage = new SimpleMessage();
            simpleMessage.setId(person.getId());
            simpleMessage.setFirstName(person.getFirstName());
            simpleMessage.setLastName(person.getLastName());
            rabbitTemplate.convertAndSend(exchange, "", simpleMessage);


            genericResponse.setMessage("Message was sent to queue");
            genericResponse.setStatusCode("Success");
        } catch (Exception e) {
            System.out.println("Got produceMessage exception:" + e.getMessage());
            genericResponse.setMessage(e.getMessage());
            genericResponse.setStatusCode("Error");
        }

        return genericResponse;
    }
}

</pre>


### controller:
<pre>

@RestController
@RequiredArgsConstructor
public class RabbitmqIntegrationController implements RabbitmqIntegration_v1Api {

    private final ProduceMessageService produceMessageService;

    @Override
    @PostMapping("/send-message")
    public ResponseEntity<ProduceMessageResponse> produceMessage(RequestedLanguage _lang, Person person) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ProduceMessageResponse().data(produceMessageService.produceMessage(person)));
    }
}
</pre>


###Curl
<pre>
curl --location --request POST 'http://localhost:8080/send-message' \
--header 'Content-Type: application/json' \
--data-raw '{ "id": "id1111",
 "firstName": "elad",
 "lastName": "avrahami"
  }'

</pre>







