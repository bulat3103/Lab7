package serverModule.utility;

import common.exceptions.DatabaseManagerException;
import common.utility.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserManager {
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseManager.USER_TABLE + " (" +
            DatabaseManager.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + ") VALUES (?, ?)";

    private DatabaseManager databaseManager;

    public DatabaseUserManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public User getUserById(long userID) throws SQLException {
        User user;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_USER_BY_ID, false);
            preparedStatement.setLong(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Выполнен запрос SELECT_USER_BY_ID!");
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseManager.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseManager.USER_TABLE_PASSWORD_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
            throw new SQLException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return user;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public int getUserIdByUsername(User user) throws DatabaseManagerException {
        int userID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedStatement.setString(1, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Выполнен запрос SELECT_USER_BY_USERNAME!");
            if (resultSet.next()) {
                userID = resultSet.getInt(DatabaseManager.USER_TABLE_ID_COLUMN);
            } else userID = -1;
            return userID;
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public boolean insertUser(User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedStatement = databaseManager.doPreparedStatement(INSERT_USER, false);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            if (preparedStatement.executeUpdate() == 0) throw new SQLException();
            System.out.println("Выполнен запрос INSERT_USER!");
            return true;
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса INSERT_USER!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }
}
