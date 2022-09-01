package com.banking.account.query.domain.dao;

import com.banking.account.query.domain.entities.BankAccount;
import com.banking.cqrs.core.domain.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountDao extends CrudRepository<BankAccount, String> {
    BankAccount findByAccountHolder(String accountHolder);
    List<BaseEntity> findByBalanceGreaterThan(double balance);
    List<BaseEntity> findByBalanceLessThan(double balance);
}
