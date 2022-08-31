package com.banking.account.query.infrastructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.DepositedEvent;
import com.banking.account.common.events.RemovedEvent;

public interface EventHandler {
    void on(AccountOpenedEvent accountOpenedEvent);
    void on(AccountClosedEvent accountClosedEvent);
    void on(RemovedEvent removedEvent);
    void on(DepositedEvent depositedEvent);
}
