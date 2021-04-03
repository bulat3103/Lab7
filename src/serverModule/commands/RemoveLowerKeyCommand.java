package serverModule.commands;

import common.exceptions.EmptyCollectionException;
import common.exceptions.WrongAmountOfParametersException;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

import java.util.List;

/**
 * Command 'remove_lower_key'. Removes all items from the collection whose key is less than the specified one.
 */
public class RemoveLowerKeyCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public RemoveLowerKeyCommand(CollectionManager collectionManager) {
        super("remove_lower_key null", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
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
            int key = Integer.parseInt(argument);
            collectionManager.removeLowerKey(key);
            ResponseOutputer.append("Солдаты успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        }
        return false;
    }
}
