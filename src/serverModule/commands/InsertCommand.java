package serverModule.commands;

import common.data.SpaceMarine;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.SpaceMarineLite;
import serverModule.utility.CollectionManager;
import clientModule.utility.SpaceMarineBuilder;
import serverModule.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'insert'. Adds a new element to collection.
 */
public class InsertCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        super("insert null {element}", "добавить новый элемент с заданным ключом");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfParametersException();
            int key = Integer.parseInt(argument);
            SpaceMarineLite marineLite = (SpaceMarineLite) objectArgument;
            collectionManager.addToCollection(key,new SpaceMarine(
                    collectionManager.generateId(),
                    marineLite.getName(),
                    marineLite.getCoordinates(),
                    LocalDateTime.now(),
                    marineLite.getHealth(),
                    marineLite.getHeartCount(),
                    marineLite.getAchievements(),
                    marineLite.getWeaponType(),
                    marineLite.getChapter()

            ));
            ResponseOutputer.append("Успешно добавлено в коллекцию!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
        }
        return false;
    }
}
