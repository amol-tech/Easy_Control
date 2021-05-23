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

    public List<Account> selectAllAccounts()
    {
        return entityManager.createQuery("Select a from Account a").getResultList();
    }

}
