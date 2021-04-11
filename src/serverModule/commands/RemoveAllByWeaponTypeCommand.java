package serverModule.commands;

import common.data.SpaceMarine;
import common.data.Weapon;
import common.exceptions.*;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

import java.util.List;

/**
 * Command 'remove_all_by_weapon_type'. Removes from the collection all items whose value of the weaponType field is equivalent to the specified one.
 */
public class RemoveAllByWeaponTypeCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveAllByWeaponTypeCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_all_by_weapon_type <weaponType>", "удалить из коллекции все элементы, значение поля weaponType которого эквивалентно заданному");
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
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            Weapon weapon = Weapon.valueOf(argument.toUpperCase());
            List<SpaceMarine> marines = collectionManager.getAllByWeaponType(weapon);
            for (SpaceMarine marine : marines) {
                if (!marine.getOwner().equals(user)) continue;
                if (!databaseCollectionManager.checkSpaceMarineByIdAndUserId(marine.getId(), user)) throw new IllegalDatabaseEditException();
                databaseCollectionManager.deleteSpaceMarineById(marine.getId());
                collectionManager.removeByValue(marine);
            }
            ResponseOutputer.append("Все солдаты с типом оружия " + weapon.toString() + " успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
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
