package serverModule.commands;

import common.data.SpaceMarine;
import common.exceptions.EmptyCollectionException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.SpaceMarineLite;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Command 'remove_greater'. Removes elements greater than user entered.
 */
public class RemoveGreaterCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
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
                    marineLite.getChapter()
            );
            collectionManager.removeGreater(marineToCompare);
            ResponseOutputer.append("Солдаты успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        }
        return false;
    }
}
