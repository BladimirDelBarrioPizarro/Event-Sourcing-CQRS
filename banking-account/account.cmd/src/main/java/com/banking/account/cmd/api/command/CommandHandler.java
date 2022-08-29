package com.banking.account.cmd.api.command;

public interface CommandHandler {
    void handle(OpenAccountCommand openAccountCommand);
    void handle(DepositCommand depositCommand);
    void handle(RemoveCommand removeCommand);
    void handle(CloseAccountCommand closeAccountCommand);
}
