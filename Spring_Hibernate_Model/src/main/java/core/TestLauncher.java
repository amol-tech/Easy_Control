package core;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import core.model.Account;
import core.service.AccountService;
import core.service.impl.AccountServiceImpl;

public class TestLauncher
{

    public static void main(String[] args)
    {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("config.xml");
        Launcher launcher = (Launcher) appContext.getBean("launcher");
        System.out.println(launcher.getMessage());
        
        AccountService accountService = (AccountService) appContext.getBean(AccountServiceImpl.class);
        List<Account> allAccounts = accountService.getAllAccounts();
        for (Account account : allAccounts)
        {
            System.out.println(account.getName());
        }
    }
}
