package serverModule.commands;

import common.exceptions.WrongAmountOfParametersException;
import serverModule.utility.ResponseOutputer;

/**
 * Command 'execute_script'. Executes scripts from a file. Actually only checks argument and prints messages.
 */
public class ExecuteScriptCommand extends AbstractCommand{

    public ExecuteScriptCommand() {
        super("execute_script <file_name>", "исполнить скрипт из указанного файла");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
        }
        return false;
    }
}
