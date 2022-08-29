package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.DepositedEvent;
import com.banking.account.common.events.RemovedEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    // Si la cuenta está activa
    private Boolean active;
    // Dinero de la cuenta
    private double balance;

    // Constructor para crear una cuenta, recibe un command
    // El command es enviado al aggregate
    public AccountAggregate(OpenAccountCommand openAccountCommand){
      // Creamos un nuevo evento para crear la cuenta
      raiseEvent(AccountOpenedEvent.builder()
              .id(openAccountCommand.getId())
              .accountHolder(openAccountCommand.getAccountHolder())
              .accountType(openAccountCommand.getAccountType())
              .openBalance(openAccountCommand.getOpenBalance())
              .createdDate(new Timestamp(System.currentTimeMillis()))
              .build());
    }

    // Método para setear los valores del constructor dentro del AccountAggregate
    public void apply(AccountOpenedEvent accountOpenedEvent){
        this.id = accountOpenedEvent.getId();
        this.active = true;
        this.balance = accountOpenedEvent.getOpenBalance();
    }

    // Método para depositar dinero
    public void depositAmount(double amount){
        if(!this.active){
            throw new IllegalStateException(" -- ERROR: Los fondos no pueden ser depositados en una cuenta no activa");
        }
        if(amount <= 0){
            throw new IllegalStateException(" -- ERROR: No puede ingresar un fondo menor o igual a 0.");
        }

        raiseEvent(DepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(DepositedEvent depositedEvent){
        this.id = depositedEvent.getId();
        this.balance += depositedEvent.getAmount();
    }

    public void removedAmount(double amount){
        if(!this.active){
            throw new IllegalStateException(" -- ERROR la cuenta bancaria está cerrada");
        }
        raiseEvent(RemovedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(RemovedEvent removedEvent){
        this.id = removedEvent.getId();
        this.balance -= removedEvent.getAmount();
    }

    public void closeAccount(){
        if(!active){
            throw new IllegalStateException(" -- ERROR la cuenta esta cerrada");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent accountClosedEvent){
        this.id = accountClosedEvent.getId();
        this.active = false;
    }
}
