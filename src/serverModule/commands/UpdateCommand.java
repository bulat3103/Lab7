package serverModule.commands;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.SpaceMarine;
import common.data.Weapon;
import common.exceptions.EmptyCollectionException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.SpaceMarineLite;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции по id");
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
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            int id = Integer.parseInt(argument);
            int key = collectionManager.getKeyById(id);
            SpaceMarine oldMarine = collectionManager.getFromCollection(key);
            SpaceMarineLite marineLite = (SpaceMarineLite) objectArgument;
            String name = marineLite.getName() == null ? oldMarine.getName() : marineLite.getName();
            Coordinates coordinates = marineLite.getCoordinates() == null ? oldMarine.getCoordinates() : marineLite.getCoordinates();
            LocalDateTime creationDate = oldMarine.getCreationDate();
            int health = marineLite.getHealth() == -1 ? oldMarine.getHealth() : marineLite.getHealth();
            Integer heartCount = marineLite.getHeartCount() == -1 ? oldMarine.getHeartCount() : marineLite.getHeartCount();
            String achievements = marineLite.getAchievements() == null ? oldMarine.getAchievements() : marineLite.getAchievements();
            Weapon weapon = marineLite.getWeaponType() == null ? oldMarine.getWeaponType() : marineLite.getWeaponType();
            Chapter chapter = marineLite.getChapter() == null ? oldMarine.getChapter() : marineLite.getChapter();
            collectionManager.removeFromCollection(key);
            collectionManager.addToCollection(key, new SpaceMarine(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    health,
                    heartCount,
                    achievements,
                    weapon,
                    chapter
            ));
            ResponseOutputer.append("Успешно изменено!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            System.out.println("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки");
        } catch (EmptyCollectionException exception) {
            System.out.println("Коллекция пуста!");
        }
        return false;
    }
}
