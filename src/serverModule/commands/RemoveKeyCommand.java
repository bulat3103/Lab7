package serverModule.commands;

import common.data.SpaceMarine;
import common.exceptions.*;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'remove_key'. Removes an item from the collection by its key.
 */
public class RemoveKeyCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_key", "удалить элемент из коллекции по его ключу");
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
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            int key = Integer.parseInt(argument);
            SpaceMarine o = collectionManager.getFromCollection(key);
            if (o == null) throw new NotFoundMarineException();
            if (!o.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkSpaceMarineByIdAndUserId(o.getId(), user)) throw new IllegalDatabaseEditException();
            databaseCollectionManager.deleteSpaceMarineById(o.getId());
            collectionManager.removeFromCollection(key);
            ResponseOutputer.append("Солдат успешно удален!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (NotFoundMarineException exception) {
            ResponseOutputer.append("Космический десант не найден!\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException exception) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.append("Принадлежащие другим пользователям объекты доступны только для чтения!\n");
        }
        return false;
    }
}
