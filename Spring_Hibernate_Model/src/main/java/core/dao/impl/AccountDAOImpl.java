package core.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import core.dao.AccountDAO;
import core.model.Account;

@Component
public class AccountDAOImpl implements AccountDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    public List<Account> getAllAccounts()
    {
        return entityManager.createQuery("Select a from Account a").getResultList();
    }

    public void add(Account account)
    {
        entityManager.persist(account);
    }

    public void remove(Account account)
    {
        Account acc = entityManager.find(Account.class, account.getId());
        entityManager.remove(acc);
    }

    public Account getAccountByIdentifer(String id)
    {
        return entityManager.find(Account.class, id);
    }
}
