package core.dao;

import java.util.List;

import core.model.Account;

public interface AccountDAO 
{
    public List<Account> getAllAccounts();
    
    public void add(Account account);
    
    public void remove(Account account);
    
    public Account getAccountByIdentifer(String id);
}
