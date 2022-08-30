package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.exceptions.ConcurrencyException;
import com.banking.cqrs.core.infrastructure.EventStore;
import com.banking.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> baseEvents, int expectedVersion) {
        var eventString = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        // Comprobamos si la versión que espero es válida
        if(expectedVersion != -1 && eventString.get(eventString.size() -1).getVersion() != expectedVersion){
            throw new ConcurrencyException();
        }

        var version = expectedVersion;
        for(var event: baseEvents){
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Timestamp(System.currentTimeMillis()))
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .baseEvent(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if(!persistedEvent.getId().isEmpty()){
                // Producir un evento para Kafka
                eventProducer.produce(event.getClass().getSimpleName(),event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvent(String aggregateId) {
        var eventString = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(eventString == null || eventString.isEmpty()){
            throw new AggregateNotFoundException(" -- ERROR la cuenta dle banco es incorrecta");
        }
        return eventString.stream().map(EventModel::getBaseEvent).collect(Collectors.toList());
    }
}
