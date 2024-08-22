package Service;

import java.util.List;

import Model.Account;

public interface AccountService {

    Account createAccount(Account account);
    Account getAccountById(int id);
    Account findAccountByUsername(String username);
    List<Account> getAllAcccounts();
    Account updateAccount(Account account);
    boolean deleteAccount(int id);

}
