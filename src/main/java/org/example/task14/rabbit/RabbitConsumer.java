package org.example.task14.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task14.interfaces.consumer.QueueConsumer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitConsumer implements QueueConsumer {
    private final RabbitTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = "queue")
    public void consumeMessage() {
        Message message = rabbitTemplate.receive("queue");
        if (message != null) {
            message.getBody();
        }
    }

    @Override
    public void stopConsumer() {
        log.info("Stop consumer");
        rabbitTemplate.getConnectionFactory().resetConnection();
        rabbitTemplate.destroy();
    }
}
