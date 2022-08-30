package com.banking.account.cmd.api.command;

public interface CommandHandler {
    void handle(OpenAccountCommand openAccountCommand);
    void handle(DepositAccountCommand depositCommand);
    void handle(RemoveAccountCommand removeCommand);
    void handle(CloseAccountCommand closeAccountCommand);
}
