package serverModule;

import common.exceptions.WrongAmountOfParametersException;
import serverModule.commands.*;
import serverModule.utility.*;

public class App {
    public static final int PORT = 20002;
    private static String databaseUsername = "s311726";
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;

    public static void main(String[] args) {
        if (!initialize(args)) return;
        FileManager fileManager = new FileManager();
        DatabaseManager databaseManager = new DatabaseManager(databaseAddress, databaseUsername, databasePassword);
        CollectionManager collectionManager = new CollectionManager(fileManager);
        CommandManager commandManager = new CommandManager(new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new InsertCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new RemoveKeyCommand(collectionManager),
                new ClearCommand(collectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new RemoveGreaterCommand(collectionManager),
                new HistoryCommand(),
                new RemoveLowerKeyCommand(collectionManager),
                new RemoveAllByWeaponTypeCommand(collectionManager),
                new SaveCommand(collectionManager),
                new SumOfHealthCommand(collectionManager),
                new AverageOfHeartCountCommand(collectionManager),
                new LoadCollectionCommand(collectionManager));
        RequestManager requestManager = new RequestManager(commandManager);
        Server server = new Server(PORT, requestManager);
        server.run();
        collectionManager.saveCollection();
        databaseManager.closeConnection();
    }

    private static boolean initialize(String[] args) {
        try {
            if (args.length != 2) throw new WrongAmountOfParametersException();
            databaseHost = args[0];
            databasePassword = args[1];
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs";
            return true;
        } catch (WrongAmountOfParametersException e) {
            System.out.println("Должно передаваться 2 параметра: 'host', 'password'!");
        }
        return false;
    }
}
