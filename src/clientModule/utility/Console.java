package clientModule.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.Weapon;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.Request;
import common.utility.ResponseCode;
import common.utility.SpaceMarineLite;
import common.utility.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Operates command input.
 */
public class Console {
    private Scanner scanner;
    private Stack<File> scriptFileNames = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();
    private AuthManager authManager;

    public Console(Scanner scanner, AuthManager authManager) {
        this.scanner = scanner;
        this.authManager = authManager;
    }

    /**
     * Mode for catching commands from user input.
     */
    public Request interactiveMode(ResponseCode serverResponseCode, User user) {
        String userInput = "";
        String[] userCommand = {"", ""};
        ProcessCode processCode = null;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.SERVER_EXIT || serverResponseCode == ResponseCode.ERROR)) {
                        throw new IncorrectInputInScriptException();
                    }
                    while (fileMode() && !scanner.hasNextLine()) {
                        scanner.close();
                        scanner = scannerStack.pop();
                        System.out.println("Возвращаюсь из скрипта '" + scriptFileNames.pop().getName() + "'!");
                    }
                    if (fileMode()) {
                        userInput = scanner.nextLine();
                        if (!userInput.isEmpty()) {
                            System.out.print("$ ");
                            System.out.println(userInput);
                        }
                    } else {
                        System.out.print("$ ");
                        if (scanner.hasNext()) {
                            userInput = scanner.nextLine();
                        } else {
                            System.out.println("Клиент завершен!");
                            System.exit(0);
                        }
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    System.out.println("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};
                }
                processCode = checkCommand(userCommand[0], userCommand[1]);
            } while (processCode == ProcessCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                switch (processCode) {
                    case OBJECT:
                        SpaceMarineLite marineToInsert = generateMarineToInsert();
                        return new Request(userCommand[0], userCommand[1], marineToInsert, user);
                    case UPDATE_OBJECT:
                        SpaceMarineLite marineToUpdate = generateMarineToUpdate();
                        return new Request(userCommand[0], userCommand[1], marineToUpdate, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptFileNames.isEmpty() && scriptFileNames.search(scriptFile) != -1) {
                            throw new ScriptRecursionException();
                        }
                        scannerStack.push(scanner);
                        scriptFileNames.push(scriptFile);
                        scanner = new Scanner(scriptFile);
                        System.out.println("Выполняю скрипт '" + scriptFile.getName() + "'!");
                        break;
                    case LOG_IN:
                        return authManager.handle();
                }
            } catch (FileNotFoundException exception) {
                System.out.println("Файл со скриптом не найден!");
            } catch (ScriptRecursionException exception) {
                System.out.println("Скрипты не могут вызываться рекурсивно!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            System.out.println("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                scanner.close();
                scanner = scannerStack.pop();
            }
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }



    /**
     * Launches the command.
     * @param command Command to launch.
     * @return Exit code.
     */
    private ProcessCode checkCommand(String command, String argument) {
        try {
            switch (command) {
                case "":
                    return ProcessCode.ERROR;
                case "help":
                case "show":
                case "info":
                case "clear":
                case "history":
                case "sum_of_health":
                case "average_of_heart_count":
                case "exit":
                case "log_out":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "insert":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                case "update":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.UPDATE_OBJECT;
                case "log_in":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.LOG_IN;
                case "remove_key":
                case "remove_lower_key":
                case "remove_all_by_weapon_type":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "execute_script":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.SCRIPT;
                case "remove_greater":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                default:
                    System.out.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessCode.ERROR;
            }
        } catch (WrongAmountOfParametersException e) {
            System.out.println("Проверьте правильность ввода аргументов!");
        }
        return ProcessCode.OK;
    }

    private SpaceMarineLite generateMarineToInsert() {
        SpaceMarineBuilder builder = new SpaceMarineBuilder(scanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        return new SpaceMarineLite(
                builder.askName(),
                builder.askCoordinates(),
                builder.askHealth(),
                builder.askHeartCount(),
                builder.askAchievements(),
                builder.askWeapon(),
                builder.askChapter()
        );
    }

    private SpaceMarineLite generateMarineToUpdate() {
        SpaceMarineBuilder builder = new SpaceMarineBuilder(scanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        String name = builder.askAboutChangingField("Хотите изменить имя космического солдата?") ?
                builder.askName() : null;
        Coordinates coordinates = builder.askAboutChangingField("Хотите изменить координаты космического солдата?") ?
                builder.askCoordinates() : null;
        int health = builder.askAboutChangingField("Хотите изменить здоровье космического солдата?") ?
                builder.askHealth() : -1;
        Integer heartCount = builder.askAboutChangingField("Хотите изменить количество сердец космического солдата?") ?
                builder.askHeartCount() : -1;
        String achievements = builder.askAboutChangingField("Хотите изменить достижения космического солдата?") ?
                builder.askAchievements() : null;
        Weapon weapon = builder.askAboutChangingField("Хотите изменить оружие космического солдата?") ?
                builder.askWeapon() : null;
        Chapter chapter = builder.askAboutChangingField("Хотите изменить часть, к которой принадлежит космический солдат?") ?
                builder.askChapter() : null;
        return new SpaceMarineLite(
                name,
                coordinates,
                health,
                heartCount,
                achievements,
                weapon,
                chapter
        );
    }

    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}
