package clientModule.utility;

import common.exceptions.NotDeclaredValueException;

import java.util.Scanner;

public class AuthAsker {
    private Scanner scanner;

    public AuthAsker(Scanner scanner) {
        this.scanner = scanner;
    }

    public String askLogin() {
        String login;
        while (true) {
            try {
                System.out.println("Введите логин:");
                System.out.print("> ");
                login = scanner.nextLine().trim();
                if (login.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException e) {
                System.out.println("Логин не может быть пустым!");
            }
        }
        return login;
    }

    public String askPassword() {
        String password;
        while (true) {
            try {
                System.out.println("Введите пароль:");
                System.out.print("> ");
                password = scanner.nextLine().trim();
                if (password.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException e) {
                System.out.println("Логин не может быть пустым!");
            }
        }
        return password;
    }

    public boolean askQuestion(String question) {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                System.out.println(finalQuestion);
                System.out.print("> ");
                answer = scanner.nextLine().trim();
                if (!answer.equals("+") && !answer.equals("-")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException e) {
                System.out.println("Ответ должен быть либо '+', либо '-'!");
            }
        }
        return answer.equals("+");
    }
}
