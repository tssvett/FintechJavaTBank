package org.example.task14.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task14.interfaces.producer.QueueProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer implements QueueProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void produceMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void stopProducer() {
        kafkaTemplate.getProducerFactory().getListeners().forEach(kafkaTemplate.getProducerFactory()::removeListener);
        kafkaTemplate.destroy();
        log.info("Stop producer");
    }
}
