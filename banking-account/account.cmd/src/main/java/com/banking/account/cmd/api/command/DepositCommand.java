package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class DepositCommand extends BaseCommand {
    private double amount;
}
