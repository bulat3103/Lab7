package clientModule;

import clientModule.utility.AuthManager;
import clientModule.utility.Console;
import common.exceptions.NotDeclaredValueException;
import common.exceptions.WrongAmountOfParametersException;

import java.util.Scanner;

public class App {
    private static String host;
    private static int port;

    public static void main(String[] args) {
        //if (!checkArgs(args)) return;
        Scanner scanner = new Scanner(System.in);
        AuthManager authManager = new AuthManager(scanner);
        Console console = new Console(scanner, authManager);
        Client client = new Client("localhost", 20002, console, authManager);
        client.run();
        scanner.close();
    }

    private static boolean checkArgs(String[] args) {
        try {
            if (args.length != 2) throw new WrongAmountOfParametersException();
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new NotDeclaredValueException();
            return true;
        } catch (WrongAmountOfParametersException e) {
            System.out.println("Нужно передавать 2 параметра <String host, int port>!");
        } catch (NotDeclaredValueException e) {
            System.out.println("Порт не может быть отрицательным числом!");
        } catch (NumberFormatException e) {
            System.out.println("Порт должен быть представлен числом!");
        }
        return false;
    }
}

