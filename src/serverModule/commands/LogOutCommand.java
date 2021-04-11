package serverModule.commands;

import common.exceptions.DatabaseManagerException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.User;
import serverModule.utility.DatabaseUserManager;
import serverModule.utility.ResponseOutputer;

import java.sql.SQLException;

public class LogOutCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public LogOutCommand(DatabaseUserManager databaseUserManager) {
        super("log_out", "выйти из аккаунта");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            databaseUserManager.updateOnline(user, false);
            ResponseOutputer.append("Выход из аккаунта произведен!\n");
            return true;
        } catch (WrongAmountOfParametersException e) {
            ResponseOutputer.append("У этой команды должен быть только один параметр: 'user'\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        }
        return false;
    }
}
