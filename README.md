# rabbitMQ SpringBoot
Java Messaging Service (JMS) with Spring Framework. Asynchronous Messaging, Queue, Exchange &amp; Routing Of Messages

part2 include app.proprties with stucture of cloud and local hostsconfiguration 

*implementation 'org.springframework.boot:spring-boot-starter-amqp'

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
