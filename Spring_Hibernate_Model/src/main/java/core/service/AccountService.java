package core.service;

import java.util.List;

import core.model.Account;

public interface AccountService
{
    public List<Account> getAllAccounts();
    
    public void add(Account account);
    
    public void remove(Account account);
    
    public Account getAccountByIdentifer(String id);
}
