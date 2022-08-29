package com.banking.account.cmd.api.command.impl;

import com.banking.account.cmd.api.command.*;
import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler {
    @Autowired
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand openAccountCommand) {
       var aggregate = new AccountAggregate(openAccountCommand);
       eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositCommand depositCommand) {
       var aggregate = eventSourcingHandler.getById(depositCommand.getId());
       aggregate.depositAmount(depositCommand.getAmount());
       eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(RemoveCommand removeCommand) {
        var aggregate = eventSourcingHandler.getById(removeCommand.getId());
        if(removeCommand.getAmount() > aggregate.getBalance()){
            throw new IllegalStateException(" -- ERROR insuficientes fondos, no se puede retirar dinero");
        }
        aggregate.removedAmount(removeCommand.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand closeAccountCommand) {
        var aggregate = eventSourcingHandler.getById(closeAccountCommand.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);
    }
}
