package com.banking.account.query.infrastructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.DepositedEvent;
import com.banking.account.common.events.RemovedEvent;
import com.banking.account.query.domain.dao.AccountDao;
import com.banking.account.query.domain.entities.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{
    @Autowired
    private AccountDao accountDao;

    @Override
    public void on(AccountOpenedEvent accountOpenedEvent) {
      var bankAccount = BankAccount.builder()
              .id(accountOpenedEvent.getId())
              .accountHolder(accountOpenedEvent.getAccountHolder())
              .accountType(accountOpenedEvent.getAccountType())
              .balance(accountOpenedEvent.getOpenBalance())
              .creationDate(accountOpenedEvent.getCreatedDate())
              .build();
        accountDao.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent accountClosedEvent) {
        accountDao.deleteById(accountClosedEvent.getId());
    }

    @Override
    public void on(RemovedEvent removedEvent) {
        var bankAccount = accountDao.findById(removedEvent.getId());
        if(bankAccount.isEmpty()){
            return;
        }
        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance - removedEvent.getAmount();
        bankAccount.get().setBalance(latestBalance);
        accountDao.save(bankAccount.get());
    }

    @Override
    public void on(DepositedEvent depositedEvent) {
        var bankAccount = accountDao.findById(depositedEvent.getId());
        if(bankAccount.isEmpty()){
            return;
        }
        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance + depositedEvent.getAmount();
        bankAccount.get().setBalance(latestBalance);
        accountDao.save(bankAccount.get());
    }
}
