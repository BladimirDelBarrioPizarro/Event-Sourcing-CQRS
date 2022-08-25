package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    // Colección de métodos que están siendo registrados.
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> commandHandlerMethod) {
        // Agregamos un nuevo valor y lo asocia con una key que no está asignada a ningún value en el Hasmap aún.
        // Contiene la lista de comandos que contiene un método.
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(commandHandlerMethod);
    }

    @Override
    public void send(BaseCommand baseCommand) {
        // Validamos si el handler tiene commands
       var handlers = routes.get(baseCommand.getClass());
       if(handlers == null || handlers.size() == 0){
           throw new RuntimeException("El command handler no fué registrado");
       }
       if(handlers.size() > 1){
           throw new RuntimeException("No se puede enviar un command que tiene más de un handler");
       }
       handlers.get(0).handle(baseCommand);
    }
}
