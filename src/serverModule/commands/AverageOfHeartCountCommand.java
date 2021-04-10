package serverModule.commands;

import common.exceptions.EmptyCollectionException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'average_of_heart_count'. Outputs the average value of the heartCount field for all items in the collection.
 */
public class AverageOfHeartCountCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public AverageOfHeartCountCommand(CollectionManager collectionManager) {
        super("average_of_heart_count", "вывести среднее значение поля heartCount для всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            double average_of_heart_count = collectionManager.averageOfHeartCount();
            if (average_of_heart_count == 0) throw new EmptyCollectionException();
            ResponseOutputer.append("Среднее значение поля heartCount всех космических десантов: " + average_of_heart_count + "\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        }
        return false;
    }
}
