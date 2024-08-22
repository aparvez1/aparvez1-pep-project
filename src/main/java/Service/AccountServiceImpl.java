package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountServiceImpl implements AccountService {

    @Override
    public Account createAccount(Account account) {
        String sql = "INSERT INTO account (username, password) VALUES(?,?)";
        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            int rowsEffected = ps.executeUpdate();

            if (rowsEffected == 1) {
                return findAccountByUsername(account.getUsername());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account getAccountById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE account_id=?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Account foundAccount = new Account();
                        foundAccount.setAccount_id(rs.getInt("account_id"));
                        foundAccount.setUsername(rs.getString("username"));
                        foundAccount.setPassword(rs.getString("password"));
                        return foundAccount;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAllAcccounts() {
        try (Connection conn = ConnectionUtil.getConnection()) {

            List<Account> accounts = new LinkedList<>();
            String sql = "SELECT * FROM account";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }
            while (rs.next()) {
                Account account = new Account();
                account.setAccount_id(rs.getInt(1));
                account.setUsername(rs.getString(2));
                account.setPassword(rs.getString(3));

                accounts.add(account);
            }

            return accounts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account updateAccount(Account account) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "UPDATE account SET username=?, password=? where account_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account.getAccount_id());
            ps.setString(2, account.getUsername());
            ps.setString(3, account.getPassword());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Account updatedAccount = new Account();
            updatedAccount.setAccount_id(rs.getInt(1));
            updatedAccount.setUsername(rs.getString(2));
            updatedAccount.setPassword(rs.getString(3));

            return updatedAccount;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteAccount(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM account WHERE account_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int check = ps.executeUpdate();

            if (check == 0) {
                return false;
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Account findAccountByUsername(String username) {

        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username=?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Account foundAccount = new Account();
                        foundAccount.setAccount_id(rs.getInt("account_id"));
                        foundAccount.setUsername(rs.getString("username"));
                        foundAccount.setPassword(rs.getString("password"));
                        return foundAccount;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
