package wolper.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;


@Configuration
@ComponentScan("wolper.controller")
@EnableWebSocketMessageBroker


public class StompCOnfiguration extends AbstractWebSocketMessageBrokerConfigurer {
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/data").setAllowedOrigins("*").withSockJS()
                .setDisconnectDelay(60000*3);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
         SimpleBrokerRegistration simpleBrokerRegistration = registry.enableSimpleBroker("/queue", "/topic");
         registry.setApplicationDestinationPrefixes("/app");
         simpleBrokerRegistration.setTaskScheduler(messageBrokerSockJsTaskScheduler());
         long [] hbeat = {40000,40000};
         simpleBrokerRegistration.setHeartbeatValue(hbeat);

        //Если заяц
        //StompBrokerRelayRegistration stompBrokerRelayRegistration = registry.enableStompBrokerRelay("/queue", "/topic");
        //registry.setApplicationDestinationPrefixes("/app");
        //stompBrokerRelayRegistration.setRelayHost("spotted-monkey.rmq.cloudamqp.com");
        //stompBrokerRelayRegistration.setSystemLogin("uhyhnnlw");
        //stompBrokerRelayRegistration.setVirtualHost("uhyhnnlw");
        //stompBrokerRelayRegistration.setClientLogin("uhyhnnlw");
        //stompBrokerRelayRegistration.setClientPasscode("zU9EajruKv2YGyKpIgdKmx9g9q-vZjDi");
        //stompBrokerRelayRegistration.setSystemPasscode("zU9EajruKv2YGyKpIgdKmx9g9q-vZjDi");

    }

    @Bean(name = "taskScheduler")
    public ThreadPoolTaskScheduler messageBrokerSockJsTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("SockJS-");
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        return scheduler;
    }
}


