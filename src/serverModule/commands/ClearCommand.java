package serverModule.commands;

import common.exceptions.WrongAmountOfParametersException;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'clear'. Clears the collection.
 */
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if(!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            collectionManager.clearCollection();
            ResponseOutputer.append("Коллекция успешно очищена!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        }
        return false;
    }
}
