package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/*
Clase para mantener la lista de eventos
* */

@Slf4j
@Getter
@Setter
public abstract class AggregateRoot {
    protected String id;
    private int version = -1;
    // Lista que representa el conjunto de cambios (eventos).
    private final List<BaseEvent> changes = new ArrayList<>();

    // Los cambios ya han sido confirmados así que limpiamos la lista.
    public void markChangesAsCommitted(){
        this.changes.clear();
    }

    // Método para aplicar el evento a la lista de cambios (changes).
    protected void applyChange(BaseEvent baseEvent, boolean isNewEvent){
        try{
            // El método getDeclaredMethod() de la clase Java Class devuelve un objeto de método que representa el método especificado declarado dentro de la clase o la interfaz de esta clase.
            var method = getClass().getDeclaredMethod("apply",baseEvent.getClass());
            // Lo hacemos accesible
            method.setAccessible(true);
            // Lo ejecutamos
            method.invoke(this,baseEvent);
        }catch (NoSuchMethodException ex){
          log.error(" -- Error el método apply no fué encontrado para {}",baseEvent.getClass().getName());
        }catch (Exception ex){
           log.error(" -- Error aplicando el evento al aggregate {0}", ex);
        }finally {
            // Agregamos el evento al final
            if(isNewEvent){
                changes.add(baseEvent);
            }
        }
    }

    // Método para agregar un evento al aggregate
    public void raiseEvent(BaseEvent baseEvent){
        applyChange(baseEvent,true);
    }

    // Método para reprocesar todos los eventos.
    public void replayEvents(Iterable<BaseEvent> events){
        events.forEach(event -> applyChange(event,false));
    }
}
