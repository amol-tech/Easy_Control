package core.dao;

import java.util.List;

import core.model.Account;

public interface AccountDAO
{
    public List<Account> selectAllAccounts();
}
