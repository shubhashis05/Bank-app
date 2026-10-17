package repository;

import domain.Transaction;

import java.util.*;

public class TransactionRepository {
private final Map<String , List<Transaction>> txByAccount = new HashMap<>();

    public void add(Transaction transaction) {
        String AccountNumber = transaction.getAccountNumber();
        if(!txByAccount.containsKey(AccountNumber)) {
            ArrayList<Transaction> listOfTransaction  = new ArrayList<>();
            listOfTransaction.add(transaction);
            txByAccount.put(AccountNumber, listOfTransaction);
        }
        else {
            txByAccount.get(AccountNumber).add(transaction);
        }
    }

    public List<Transaction> findByAccount(String accountNumber) {
        if(!txByAccount.containsKey(accountNumber))
            return new ArrayList<Transaction>();
        List<Transaction> list = txByAccount.get(accountNumber);
        return list;
    }
}
