package repository;

import domain.Account;
import domain.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepository {
    private final Map<String, Customer> customerById = new HashMap<>();

    public List<Customer> findAll() {
        return  new ArrayList<>(customerById.values());
    }
}
