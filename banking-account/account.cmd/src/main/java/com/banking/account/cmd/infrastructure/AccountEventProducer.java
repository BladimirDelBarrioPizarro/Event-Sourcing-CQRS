package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class AccountEventProducer implements EventProducer {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void produce(String topic, BaseEvent event) {
        this.kafkaTemplate.send(topic,event);
    }
}