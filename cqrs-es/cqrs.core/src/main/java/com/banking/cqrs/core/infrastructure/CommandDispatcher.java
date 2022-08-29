package com.banking.cqrs.core.infrastructure;

import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;

// Interfaz Mediator
// Mantiene el log de todos los métodos CommandHandlerMethod que han sido registrados y
// será responsable de enviar o hacer el dispatch de un objeto command
public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> commandHandlerMethod);
   void send(BaseCommand baseCommand);
}
