package serverModule.commands;

import common.data.SpaceMarine;
import common.exceptions.DatabaseManagerException;
import common.exceptions.IllegalDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'clear'. Clears the collection.
 */
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if(!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            for (SpaceMarine marine : collectionManager.getCollection().values()) {
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkSpaceMarineByIdAndUserId(marine.getId(), user)) throw new IllegalDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.append("Коллекция успешно очищена!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.append("Принадлежащие другим пользователям объекты доступны только для чтения!\n");
        } catch (DatabaseManagerException exception) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException exception) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        }
        return false;
    }
}
