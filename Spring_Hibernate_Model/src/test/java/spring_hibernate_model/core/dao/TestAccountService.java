package spring_hibernate_model.core.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import core.model.Account;
import core.service.AccountService;
import core.service.impl.AccountServiceImpl;
import junit.framework.TestCase;

public class TestAccountService extends TestCase
{
    private ApplicationContext context;
    private AccountService accountService;

    @Override
    protected void setUp() throws Exception
    {
        context = new ClassPathXmlApplicationContext("config.xml");
        accountService = (AccountService) context.getBean(AccountServiceImpl.class);
    }

    public void testGetAllAccounts()
    {
        List<Account> allAccounts = accountService.getAllAccounts();
        for (Account account : allAccounts)
        {
            System.out.println(account.getName());
        }
    }

    public void testAdd()
    {
        Account account = new Account("4", "Test Account", "CUSTOMER", 20, "MUmbai");
        accountService.add(account);
        accountService.add(account);
    }

    public void testRemove()
    {
        Account account = new Account("4", "Test Account", "CUSTOMER", 20, "MUmbai");
        accountService.remove(account);
    }
}
