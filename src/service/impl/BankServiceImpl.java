package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFound;
import exceptions.InsufficentBalance;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;

import java.lang.reflect.AccessFlag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BankServiceImpl implements BankService {
    private final AccountRepository accountRepository =  new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
//    validation name email accountType usign functuional Interface
//    name
    private final Validation<String> validateName = name->{
        if(name == null || name.isEmpty() || name.isBlank())
            throw new ValidationException("Name is null or empty");
    };
//    email
    private final Validation<String> validateEmail = email->{
        if(email == null || email.isEmpty() || email.isBlank() || (!email.contains("@") && !email.contains("gmail.com")))
            throw new ValidationException("Email is null or empty");
    };
//    account type
    private final Validation<String> validateAccountType = type->{
        if(type == null || type.isEmpty() || type.isBlank())
            throw new ValidationException("Email is null or empty");
        if(!type.toLowerCase().equals("savings") && !type.toLowerCase().equals("current"))
            throw new ValidationException("Type is not savings or current");
    };
//    amount
    private final Validation<Double> validateAmount = number->{
        if(number == null || number.isNaN() || number.isInfinite() || number < 0)
            throw new ValidationException("please enter a valid number");
    };
    @Override
    public String openAccount(String name, String email, String accountType) {
//        validation checking
        validateName.validate(name);
        validateEmail.validate(email);
        validateAccountType.validate(accountType);
        String customerId = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Account account = new Account(accountNumber,customerId,0.0,accountType);
//        save
        accountRepository.save(account);
        return accountNumber ;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream().toList();
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        validateAmount.validate(amount);
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFound("Account not found : " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.DEPOSIT,
                account.getAccountNumber().toString(),amount,
                LocalDateTime.now(),note);
        transactionRepository.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String withdraw) {
        validateAmount.validate(amount);
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFound("Account not found : " + accountNumber));
//        checking that enoungh balance is present or not??
        if(account.getBalance() < amount){
            throw  new InsufficentBalance("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.WITHDRAW,
                account.getAccountNumber().toString(),amount,
                LocalDateTime.now(),withdraw);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String from, String to, Double amount, String note) {
        validateAmount.validate(amount);
        if(from.equals(to))
            throw new  RuntimeException("from and to cannot be the same");
        Account fromAccount = accountRepository.findByNumber(from)
                .orElseThrow(()-> new AccountNotFound("Account not found : " + from));
        Account toAccount = accountRepository.findByNumber(to)
                .orElseThrow(()-> new AccountNotFound("Account not found : " + to));
        //        checking that enoungh balance is present or not??
        if(fromAccount.getBalance() < amount){
            throw  new InsufficentBalance("Insufficient funds");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        Transaction fromTransaction = new Transaction(UUID.randomUUID().toString(), Type.TRANSFER_OUT,
                fromAccount.getAccountNumber().toString(),amount,
                LocalDateTime.now(),note);
        Transaction toTransaction = new Transaction(UUID.randomUUID().toString(), Type.TRANSFER_IN,
                toAccount.getAccountNumber().toString(),amount,
                LocalDateTime.now(),note);
        transactionRepository.add(fromTransaction);
        transactionRepository.add(toTransaction);
    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {
        List<Transaction> transactionList = transactionRepository.findByAccount(accountNumber);
        return  transactionList;
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String name) {
        name = name.toLowerCase();
        List<Account> result = new ArrayList<>();
        List<Account> accountsList = accountRepository.findByCustomerId();
        for(Customer c : customerRepository.findAll()){
            if(c.getName().toLowerCase().contains(name)){

            }
        }
        return result;
    }

    private String getAccountNumber() {
        //i will change it later 10 -> 1 = 11 ----> AC11 { now i am changing }
        int size = accountRepository.findAll().size()+1;
        String accountNumber = String.format("AC%06d", size);
        return accountNumber;
    }
}
