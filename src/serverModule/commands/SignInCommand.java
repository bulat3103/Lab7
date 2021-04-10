package serverModule.commands;

import common.exceptions.DatabaseManagerException;
import common.exceptions.UserNotFoundException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.User;
import serverModule.utility.DatabaseUserManager;
import serverModule.utility.ResponseOutputer;

public class SignInCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public SignInCommand(String name, String description, DatabaseUserManager databaseUserManager) {
        super("sign_in", "войти в аккаунт");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user))
                ResponseOutputer.append("Авторизация прошла успешно!\n");
            else throw new UserNotFoundException();
            return true;
        } catch (WrongAmountOfParametersException e) {
            ResponseOutputer.append("У этой команды должен быть только один параметр: 'user'\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (UserNotFoundException e) {
            ResponseOutputer.append("Неправильные имя пользователя или пароль!\n");
        } catch (ClassCastException e) {
            ResponseOutputer.append("Переданный клиентом объект неверен!\n");
        }
        return false;
    }
}
