package org.example.task14.interfaces.producer;

public interface QueueProducer {

    void produceMessage(String topic, String message);

    void stopProducer();
}
