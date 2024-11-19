package org.example.task14.utils;

import java.util.List;
import org.example.task14.interfaces.consumer.QueueConsumer;
import org.example.task14.interfaces.producer.QueueProducer;
import org.example.task14.queueconfiguraion.QueueConfiguration;
import org.example.task14.rabbit.RabbitConsumer;
import org.example.task14.rabbit.RabbitProducer;
import org.example.task14.rabbit.config.RabbitConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class ConfigurationsCreator {
    private final String messageToSend = "test";
    private ConfigurableApplicationContext context;

    public QueueConfiguration createSimpleRabbitConfiguration() {
        context = new AnnotationConfigApplicationContext(RabbitConfiguration.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        List<QueueProducer> producers = List.of(new RabbitProducer(rabbitTemplate));
        List<QueueConsumer> consumers = List.of(new RabbitConsumer(rabbitTemplate));

        return new QueueConfiguration(producers, consumers, messageToSend);
    }

    public void clear() {
        if (context != null) {
            context.close();
            context = null;
        }
    }
}