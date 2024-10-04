package accounts;

import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

public class AccountService {
    private DBService dbService;

    public AccountService(DBService dbService) {
        this.dbService = dbService;
    }

    public void signUp(String login, String password) throws DBException {
        dbService.addUser(login, password);
    }

    public boolean signIn(String login, String password) throws DBException {
        UsersDataSet user = dbService.getUserByLogin(login);
        return user != null && user.getPassword().equals(password);
    }
}
