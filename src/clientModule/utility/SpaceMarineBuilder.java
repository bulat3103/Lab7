package clientModule.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.Weapon;
import common.exceptions.NotDeclaredValueException;

import java.util.Scanner;

/**
 * Asks a user a marine's value.
 */
public class SpaceMarineBuilder {
    private Scanner scanner;
    private boolean fileMode;

    public SpaceMarineBuilder(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Sets a scanner to scan user input.
     * @param scanner Scanner to set.
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getScanner() {
        return scanner;
    }

    public void setFileMode() {
        this.fileMode = true;
    }

    public void setUserMode() {
        this.fileMode = false;
    }

    /**
     * Asks a user the marine's name.
     * @return Marine's name.
     */
    public String askName() {
        String name;
        while (true) {
            try {
                System.out.println("Введите имя:");
                System.out.print(">");
                name = scanner.nextLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение поля 'name' не может быть пустым!");
            }
        }
        return name;
    }

    /**
     * Asks a user the marine's X coordinate.
     * @return Marine's X coordinate.
     */
    public int askX() {
        String strX;
        int x;
        while (true) {
            try {
                System.out.println("Введите координату X > -666:");
                System.out.print(">");
                strX = scanner.nextLine().trim();
                x = Integer.parseInt(strX);
                if (fileMode) System.out.println(strX);
                if (x <= -666) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение должно быть больше -666!");
            } catch (NumberFormatException exception) {
                System.out.println("Значение 'X' должно быть числом!");
            }
        }
        return x;
    }

    /**
     * Asks a user the marine's Y coordinate.
     * @return Marine's Y coordinate.
     */
    public Float askY() {
        String strY;
        float y;
        while (true) {
            try {
                System.out.println("Введите координату Y > -603:");
                System.out.print(">");
                strY = scanner.nextLine().trim();
                y = Float.parseFloat(strY);
                if (fileMode) System.out.println(strY);
                if (y <= -603) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение должно быть больше -666!");
            } catch (NumberFormatException exception) {
                System.out.println("Значение 'X' должно быть числом!");
            }
        }
        return y;
    }

    /**
     * Asks a user the marine's coordinates.
     * @return Marine's coordinates.
     */
    public Coordinates askCoordinates() {
        int x;
        float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the marine's health.
     * @return Marine's health.
     */
    public int askHealth() {
        String strHealth;
        int health;
        while (true) {
            try {
                System.out.println("Введите здоровье:");
                System.out.print(">");
                strHealth = scanner.nextLine().trim();
                health = Integer.parseInt(strHealth);
                if (fileMode) System.out.println(strHealth);
                if (health <= 0) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Здоровье должно быть больше нуля!");
            } catch (NumberFormatException exception) {
                System.out.println("Значение 'health' должно быть целым числом!");
            }
        }
        return health;
    }

    /**
     * Asks a user the marine's heartCount.
     * @return Marine's heartCount.
     */
    public Integer askHeartCount() {
        String strHeartCount;
        int heartCount;
        while (true) {
            try {
                System.out.println("Введите кол-во сердец:");
                System.out.print(">");
                strHeartCount = scanner.nextLine().trim();
                heartCount = Integer.parseInt(strHeartCount);
                if (fileMode) System.out.println(strHeartCount);
                if (heartCount < 0 || heartCount > 3) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Количество сердец может быть от 1 до 3");
            } catch (NumberFormatException exception) {
                System.out.println("Значение 'heartCount' должно быть целым числом!");
            }
        }
        return heartCount;
    }

    /**
     * Asks a user the marine's achievements.
     * @return Marine's achievements.
     */
    public String askAchievements() {
        String achieve;
        while (true) {
            try {
                System.out.println("Введите достижение:");
                System.out.print(">");
                achieve = scanner.nextLine().trim();
                if (fileMode) System.out.println(achieve);
                if (achieve.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение поля 'achievements' не может быть пустым!");
            }
        }
        return achieve;
    }

    /**
     * Asks a user the marine's weapon type.
     * @return Marine's weapon type.
     */
    public Weapon askWeapon() {
        String strWeapon;
        Weapon weapon;
        while (true) {
            try {
                System.out.println("Список доступного оружия - " + Weapon.list());
                System.out.println("Введите оружие");
                System.out.print(">");
                strWeapon = scanner.nextLine().trim();
                if (fileMode) System.out.println(strWeapon);
                weapon = Weapon.valueOf(strWeapon.toUpperCase());
                break;
            } catch (IllegalArgumentException exception) {
                System.out.println("Такого оружия нет!");
            }
        }
        return weapon;
    }

    /**
     * Asks a user the marine's chapter.
     * @return Marine's chapter.
     */
    public Chapter askChapter() {
        return new Chapter(askChapterName(), askChapterParentLegion());
    }

    /**
     * Asks a user the marine chapter's name.
     * @return Chapter's name.
     */
    public String askChapterName() {
        String name;
        while (true) {
            try {
                System.out.println("Введите имя ордена:");
                System.out.print(">");
                name = scanner.nextLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение поля 'name' не может быть пустым!");
            }
        }
        return name;
    }

    /**
     * Asks a user the marine chapter's parentLegion.
     * @return Chapter's parentLegion.
     */
    public String askChapterParentLegion() {
        String parentLegion;
        while (true) {
            System.out.println("Введите имя родительского легиона:");
            System.out.print(">");
            parentLegion = scanner.nextLine().trim();
            if (fileMode) System.out.println(parentLegion);
            if (parentLegion.equals("")) parentLegion = null;
            break;
        }
        return parentLegion;
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param ask A question.
     */
    public boolean askAboutChangingField(String ask) {
        String res = ask + " (+/-):";
        String answer;
        while (true) {
            try {
                System.out.println(res);
                System.out.print(">");
                answer = scanner.nextLine().trim();
                if (fileMode) System.out.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Ответ должен быть представлен знаками '+' или '-'!");
            }
        }
        return answer.equals("+");
    }
}
