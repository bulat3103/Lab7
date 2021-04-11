package serverModule.commands;

import common.data.SpaceMarine;
import common.exceptions.*;
import common.utility.SpaceMarineLite;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Command 'remove_greater'. Removes elements greater than user entered.
 */
public class RemoveGreaterCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
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
            if (user == null) throw new NonAuthorizedUserException();
            if (!argument.isEmpty() || objectArgument == null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            SpaceMarineLite marineLite = (SpaceMarineLite) objectArgument;
            SpaceMarine marineToCompare = new SpaceMarine(
                    collectionManager.generateId(),
                    marineLite.getName(),
                    marineLite.getCoordinates(),
                    LocalDateTime.now(),
                    marineLite.getHealth(),
                    marineLite.getHeartCount(),
                    marineLite.getAchievements(),
                    marineLite.getWeaponType(),
                    marineLite.getChapter(),
                    user
            );
            List<SpaceMarine> marines = collectionManager.getGreater(marineToCompare);
            for (SpaceMarine marine : marines) {
                if (!marine.getOwner().equals(user)) continue;
                if (!databaseCollectionManager.checkSpaceMarineByIdAndUserId(marine.getId(), user)) throw new IllegalDatabaseEditException();
                databaseCollectionManager.deleteSpaceMarineById(marine.getId());
                collectionManager.removeByValue(marine);
            }
            ResponseOutputer.append("Солдаты успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException exception) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
