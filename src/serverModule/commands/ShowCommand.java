package serverModule.commands;

import common.exceptions.WrongAmountOfParametersException;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'show'. Shows information about all elements of the collection.
 */
public class ShowCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            ResponseOutputer.append(collectionManager.showCollection() + "\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        }
        return false;
    }
}
