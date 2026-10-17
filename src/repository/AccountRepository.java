package repository;

import domain.Account;
import service.BankService;

import java.util.*;

public class AccountRepository {
    private final Map<String, Account> accountsBynumber = new HashMap<>();
    public void save(Account account){
        accountsBynumber.put(account.getAccountNumber(), account);
    }

    public List<Account> findAll(){
        return new ArrayList<>(accountsBynumber.values());
    }

    public Optional<Account> findByNumber(String accountNumber) {
        return Optional.ofNullable(accountsBynumber.get(accountNumber));
    }

    public List<Account> findByCustomerId() {
        List<Account> list = new ArrayList<>(accountsBynumber.values());
        return list;
    }
}
