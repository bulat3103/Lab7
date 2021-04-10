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
        //if (!initialize(args)) return;
        DatabaseManager databaseManager = new DatabaseManager(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseManager);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new InsertCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveKeyCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new RemoveGreaterCommand(collectionManager, databaseCollectionManager),
                new HistoryCommand(),
                new RemoveLowerKeyCommand(collectionManager, databaseCollectionManager),
                new RemoveAllByWeaponTypeCommand(collectionManager, databaseCollectionManager),
                new SumOfHealthCommand(collectionManager),
                new AverageOfHeartCountCommand(collectionManager));
        RequestManager requestManager = new RequestManager(commandManager);
        Server server = new Server(PORT, requestManager);
        server.run();
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
