package serverModule.commands;

import common.data.Weapon;
import common.exceptions.EmptyCollectionException;
import common.exceptions.WrongAmountOfParametersException;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

import java.util.List;

/**
 * Command 'remove_all_by_weapon_type'. Removes from the collection all items whose value of the weaponType field is equivalent to the specified one.
 */
public class RemoveAllByWeaponTypeCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public RemoveAllByWeaponTypeCommand(CollectionManager collectionManager) {
        super("remove_all_by_weapon_type <weaponType>", "удалить из коллекции все элементы, значение поля weaponType которого эквивалентно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            Weapon weapon = Weapon.valueOf(argument.toUpperCase());
            collectionManager.removeAllByWeaponType(weapon);
            ResponseOutputer.append("Все солдаты с типом оружия " + weapon.toString() + " успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        }
        return false;
    }
}
