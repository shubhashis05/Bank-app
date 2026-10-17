package app;

import domain.Account;
import domain.Transaction;
import service.BankService;
import service.impl.BankServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        System.out.println("-------- Welcome to console bank ----------");
        boolean running = true;
        while (running) {
            System.out.println("""
                            1) Open Account
                            2) Deposit
                            3) Withdraw
                            4) Transfer
                            5) Account Statement
                            6) List Accounts
                            7) Search Accounts by Customer Name
                            0) Exit
                         """);
            System.out.println("Please enter your choice: ");
            String choice =  sc.nextLine().trim();
            System.out.println("your choise is : " + choice);
            switch (choice) {
                case "0" -> running = false;
                case "1" -> openAccount(sc,bankService);
                case "2" -> deposit(sc,bankService);
                case "3" -> withdraw(sc,bankService);
                case "4" -> transfer(sc,bankService);
                case "5" -> statement(sc,bankService);
                case "6" -> listAccount(sc,bankService);
                case "7" -> searchAccount(sc,bankService);
            }
        }

    }

    private static void openAccount(Scanner sc, BankService bankService) {
        System.out.println("Please enter your name: ");
        String name = sc.nextLine().trim();
        System.out.println("Please enter your email: ");
        String email = sc.nextLine().trim();
        System.out.println("Please enter your account type (savings/current) : ");
        String type = sc.nextLine().trim();
        System.out.println("Please enter your initial amount : ");
        String initialAmount = sc.nextLine().trim();
        Double initialBalance = Double.valueOf(initialAmount);
        // opning bank Account
        String accountNumber = bankService.openAccount(name,email,type);
        if(initialBalance > 0)
            bankService.deposit(accountNumber,initialBalance,"First deposit");
        System.out.println("Your account new is opened and Account number is : " + accountNumber);
    }

    private static void deposit(Scanner sc, BankService bankService) {
        System.out.println("Please enter your account number  : ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("please enter amount to be deposited : ");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.deposit(accountNumber,amount,"Deposit");
        System.out.println("Succesfully deposited to " + accountNumber + " amount -> "+ amount);
    }

    private static void withdraw(Scanner sc,BankService bankService) {
        System.out.println("Please enter your account number  : ");
        String accountNumber = sc.nextLine().trim();
        System.out.println("please enter amount to be withdrawn : ");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.withdraw(accountNumber,amount,"withdraw");
        System.out.println("Succesfully withdraw to " + accountNumber + " amount -> "+ amount);
    }

    private static void transfer(Scanner sc,BankService bankService) {
        System.out.println("Please enter account number form you want to transfer : ");
        String from = sc.nextLine().trim();
        System.out.println("Please enter the account number you want to transfer : ");
        String to = sc.nextLine().trim();
        System.out.println("Please enter the amount you want to transfer : ");
        Double amount = Double.valueOf(sc.nextLine().trim());
        bankService.transfer(from,to,amount,"Transfer");
        System.out.println("Succesfully transfer to " + from + " to " + to + " -> "+ amount);
    }

    private static void statement(Scanner sc,BankService bankService) {
        System.out.println("Please enter the account number : ");
        String accountNumber = sc.nextLine().trim();
        List<Transaction> listOfTransaction = bankService.getStatement(accountNumber);
        System.out.println("you all transactions : ");
        for(Transaction transaction : listOfTransaction){
            System.out.println(transaction.getAccountNumber() +" | "+transaction.getAmount()+" | "
                    +transaction.getId()+" | "+transaction.getNote()+" | "+transaction.getTimeStamp());
        }
    }

    private static void listAccount(Scanner sc, BankService bankService) {
        bankService.listAccounts().stream().forEach(a->{
            System.out.println(a.getAccountNumber() + " | "+ a.getAccountType() + " | "+ a.getBalance() +" | "+ a.getAccountType() );
        });
    }

    private static void searchAccount(Scanner sc,BankService bankService) {
        System.out.println("Please enter the account holder name you want to search : ");
        String name = sc.nextLine().trim();
        List<Account> listOfAccounts = bankService.searchAccountsByCustomerName(name);
    }
}
