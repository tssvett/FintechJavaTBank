package org.example.task14.queueconfiguraion;

import java.util.List;
import org.example.task14.interfaces.consumer.QueueConsumer;
import org.example.task14.interfaces.producer.QueueProducer;

public record QueueConfiguration(
        List<QueueProducer> queueProducerList,
        List<QueueConsumer> queueConsumerList,
        String message
) {

    public void run() {
        queueProducerList.forEach(queueProducer -> queueProducer.produceMessage("topic", message));
        queueConsumerList.forEach(QueueConsumer::consumeMessage);
    }

    public void stop() {
        queueProducerList.forEach(QueueProducer::stopProducer);
        queueConsumerList.forEach(QueueConsumer::stopConsumer);
    }
}
