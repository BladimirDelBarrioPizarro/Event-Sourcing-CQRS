package com.banking.account.query.infrastructure.consumers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.DepositedEvent;
import com.banking.account.common.events.RemovedEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    /*
    * Acknowledgment -> Para indicar que el mensaje ya ha sido procesado
    * */
    void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);
    void consume(@Payload AccountClosedEvent event, Acknowledgment ack);
    void consume(@Payload DepositedEvent event, Acknowledgment ack);
    void consume(@Payload RemovedEvent event, Acknowledgment ack);
}
