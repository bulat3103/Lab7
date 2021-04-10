package serverModule.commands;

import common.exceptions.WrongAmountOfParametersException;
import common.utility.User;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'history'. Checks for wrong arguments then do nothing.
 */
public class HistoryCommand extends AbstractCommand{

    public HistoryCommand() {
        super("history", "вывести историю использованных команд");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        }
        return false;
    }
}
