package serverModule.utility;

import common.utility.User;
import serverModule.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Operates the commands.
 */
public class CommandManager {
    private final int COMMAND_HISTORY_MAX_VALUE = 14;

    private String[] commandHistory = new String[COMMAND_HISTORY_MAX_VALUE];
    private List<AbstractCommand> commands = new ArrayList<>();
    private AbstractCommand helpCommand;
    private AbstractCommand infoCommand;
    private AbstractCommand showCommand;
    private AbstractCommand insertCommand;
    private AbstractCommand updateCommand;
    private AbstractCommand removeKeyCommand;
    private AbstractCommand clearCommand;
    private AbstractCommand executeScriptCommand;
    private AbstractCommand exitCommand;
    private AbstractCommand removeGreaterCommand;
    private AbstractCommand historyCommand;
    private AbstractCommand removeLowerKeyCommand;
    private AbstractCommand removeAllByWeaponTypeCommand;
    private AbstractCommand sumOfHealthCommand;
    private AbstractCommand averageOfHeartCountCommand;

    private ReentrantLock locker = new ReentrantLock();

    public CommandManager(AbstractCommand helpCommand, AbstractCommand infoCommand, AbstractCommand showCommand, AbstractCommand insertCommand, AbstractCommand updateCommand, AbstractCommand removeKeyCommand, AbstractCommand clearCommand, AbstractCommand executeScriptCommand, AbstractCommand exitCommand, AbstractCommand removeGreaterCommand, AbstractCommand historyCommand, AbstractCommand removeLowerKeyCommand, AbstractCommand removeAllByWeaponTypeCommand, AbstractCommand sumOfHealthCommand, AbstractCommand averageOfHeartCountCommand) {
        this.helpCommand = helpCommand;
        commands.add(helpCommand);
        this.infoCommand = infoCommand;
        commands.add(infoCommand);
        this.showCommand = showCommand;
        commands.add(showCommand);
        this.insertCommand = insertCommand;
        commands.add(insertCommand);
        this.updateCommand = updateCommand;
        commands.add(updateCommand);
        this.removeKeyCommand = removeKeyCommand;
        commands.add(removeKeyCommand);
        this.clearCommand = clearCommand;
        commands.add(clearCommand);
        this.executeScriptCommand = executeScriptCommand;
        commands.add(executeScriptCommand);
        this.exitCommand = exitCommand;
        commands.add(exitCommand);
        this.removeGreaterCommand = removeGreaterCommand;
        commands.add(removeGreaterCommand);
        this.historyCommand = historyCommand;
        commands.add(historyCommand);
        this.removeLowerKeyCommand = removeLowerKeyCommand;
        commands.add(removeLowerKeyCommand);
        this.removeAllByWeaponTypeCommand = removeAllByWeaponTypeCommand;
        commands.add(removeAllByWeaponTypeCommand);
        this.sumOfHealthCommand = sumOfHealthCommand;
        commands.add(sumOfHealthCommand);
        this.averageOfHeartCountCommand = averageOfHeartCountCommand;
        commands.add(averageOfHeartCountCommand);
    }

    /**
     * Adds command to command history.
     * @param commandToAdd Command to add.
     */
    public void addToHistory(String commandToAdd, User user) {
        locker.lock();
        try {
            for (AbstractCommand command : commands) {
                if (command.getName().split(" ")[0].equals(commandToAdd)) {
                    for (int i = COMMAND_HISTORY_MAX_VALUE - 1; i > 0; i--) {
                        commandHistory[i] = commandHistory[i - 1];
                    }
                    commandHistory[0] = commandToAdd;
                }
            }
        } finally {
            locker.unlock();
        }
    }

    /**
     * Prints info about the all commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean help(String argument, Object objectArgument, User user) {
        if (helpCommand.execute(argument, objectArgument, user)) {
            for (AbstractCommand command : commands) {
                ResponseOutputer.appendTable(command.getName(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean info(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return infoCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean show(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return showCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean insert(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return insertCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean update(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return updateCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeKey(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return removeKeyCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean clear(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return clearCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean executeScript(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return executeScriptCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeGreater(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return removeGreaterCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean history(String argument, Object objectArgument, User user) {
        if (historyCommand.execute(argument, objectArgument, user)) {
            locker.lock();
            try {
                if (commandHistory.length == 0) {
                    ResponseOutputer.append("Ни одной команды еще не было использовано!\n");
                    return false;
                }
                ResponseOutputer.append("Последние использованные команды:\n");
                for (int i = 0; i < commandHistory.length; i++) {
                    if (commandHistory[i] != null) ResponseOutputer.append(" " + commandHistory[i] + "\n");
                }
                return true;
            } finally {
                locker.unlock();
            }
        }
        return false;
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeLowerKey(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return removeLowerKeyCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeAllByWeaponType(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return removeAllByWeaponTypeCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean averageOfHeartCount(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return averageOfHeartCountCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean sumOfHealth(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return sumOfHealthCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean exit(String argument, Object objectArgument, User user) {
        locker.lock();
        try {
            return exitCommand.execute(argument, objectArgument, user);
        } finally {
            locker.unlock();
        }
    }
}
