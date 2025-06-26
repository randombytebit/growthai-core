package sd.growthai_core.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic aiResponseTopic(){
        return TopicBuilder.name("ai-response-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userMessageTopic(){
        return TopicBuilder.name("user-message-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
