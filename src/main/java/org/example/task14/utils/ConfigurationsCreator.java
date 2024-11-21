package org.example.task14.utils;

import java.util.ArrayList;
import java.util.List;
import org.example.task14.config.QueueConfig;
import org.example.task14.enums.QueueType;
import org.example.task14.interfaces.consumer.QueueConsumer;
import org.example.task14.interfaces.producer.QueueProducer;
import org.example.task14.kafka.KafkaConsumer;
import org.example.task14.kafka.KafkaProducer;
import org.example.task14.queueconfiguraion.QueueConfiguration;
import org.example.task14.rabbit.RabbitConsumer;
import org.example.task14.rabbit.RabbitProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

public class ConfigurationsCreator {
    private final String messageToSend = "test";
    private ConfigurableApplicationContext context;

    public QueueConfiguration createConfiguration(int producersCount, int consumersCount, QueueType queueType) {
        context = new AnnotationConfigApplicationContext(QueueConfig.class);
        List<QueueProducer> producers = new ArrayList<>();
        List<QueueConsumer> consumers = new ArrayList<>();

        return switch (queueType) {
            case RABBITMQ -> createRabbitConfiguration(producersCount, consumersCount, consumers, producers);
            case KAFKA -> createKafkaConfiguration(producers, consumers);
        };
    }

    private QueueConfiguration createKafkaConfiguration(List<QueueProducer> producers, List<QueueConsumer> consumers) {
        KafkaTemplate<String, String> kafkaTemplate = context.getBean(KafkaTemplate.class);

        for (int i = 0; i < consumers.size(); i++) {
            consumers.add(new KafkaConsumer());
        }

        for (int i = 0; i < producers.size(); i++) {
            producers.add(new KafkaProducer(kafkaTemplate));
        }
        return new QueueConfiguration(producers, consumers, messageToSend);
    }

    private QueueConfiguration createRabbitConfiguration(int producersCount, int consumersCount, List<QueueConsumer> consumers, List<QueueProducer> producers) {
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        for (int i = 0; i < consumersCount; i++) {
            consumers.add(new RabbitConsumer(rabbitTemplate));
        }

        for (int i = 0; i < producersCount; i++) {
            producers.add(new RabbitProducer(rabbitTemplate));
        }
        return new QueueConfiguration(producers, consumers, messageToSend);
    }

    public void clear() {
        if (context != null) {
            context.close();
            context = null;
        }
    }
}