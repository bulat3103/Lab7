package serverModule.utility;

import common.utility.Request;
import common.utility.Response;
import common.utility.ResponseCode;

public class RequestManager {
    private CommandManager commandManager;

    public RequestManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response manage(Request request) {
        commandManager.addToHistory(request.getCommandName());
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getArgument(), request.getObjectArgument());
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    private ResponseCode executeCommand(String command, String argument, Object objectArgument) {
        switch (command) {
            case "":
                break;
            case "loadCollection":
                if (!commandManager.loadCollection(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "help":
                if (!commandManager.help(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "insert":
                if (!commandManager.insert(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "remove_key":
                if (!commandManager.removeKey(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "history":
                if (!commandManager.history(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "remove_lower_key":
                if (!commandManager.removeLowerKey(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "remove_all_by_weapon_type":
                if (!commandManager.removeAllByWeaponType(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "sum_of_health":
                if (!commandManager.sumOfHealth(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "average_of_heart_count":
                if (!commandManager.averageOfHeartCount(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            case "save":
                if (!commandManager.save(argument, objectArgument)) return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.append("Команда '" + command + "' не найдена. Наберите 'help' для справки.\n");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
