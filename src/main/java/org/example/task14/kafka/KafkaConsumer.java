package org.example.task14.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.task14.interfaces.consumer.QueueConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
public class KafkaConsumer implements QueueConsumer {

    @Override
    @KafkaListener(topics = "topic", groupId = "group_id")
    public void consumeMessage() {
        log.info("Message received");
    }

    @Override
    public void stopConsumer() {
        log.info("Stop consumer");
    }
}
