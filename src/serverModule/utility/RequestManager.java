package serverModule.utility;

import common.utility.Request;
import common.utility.Response;
import common.utility.ResponseCode;
import common.utility.User;

public class RequestManager {
    private CommandManager commandManager;

    public RequestManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response manage(Request request) {
        commandManager.addToHistory(request.getCommandName(), request.getUser());
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getArgument(), request.getObjectArgument(), request.getUser());
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    private synchronized ResponseCode executeCommand(String command, String argument, Object objectArgument, User user) {
        switch (command) {
            case "":
                break;
            case "help":
                if (!commandManager.help(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "insert":
                if (!commandManager.insert(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "remove_key":
                if (!commandManager.removeKey(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "history":
                if (!commandManager.history(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "remove_lower_key":
                if (!commandManager.removeLowerKey(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "remove_all_by_weapon_type":
                if (!commandManager.removeAllByWeaponType(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "sum_of_health":
                if (!commandManager.sumOfHealth(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            case "average_of_heart_count":
                if (!commandManager.averageOfHeartCount(argument, objectArgument, user)) return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.append("Команда '" + command + "' не найдена. Наберите 'help' для справки.\n");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
