package org.example.task14.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task14.interfaces.producer.QueueProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitProducer implements QueueProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String queueName = "queue";

    @Override
    public void produceMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(this.queueName, message);
    }

    @Override
    public void stopProducer() {
        rabbitTemplate.getConnectionFactory().resetConnection();
        rabbitTemplate.destroy();
        log.info("Producer stopped");
    }
}
