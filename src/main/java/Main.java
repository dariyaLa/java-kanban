import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        Manage manage = new Manage();

        while (true) {
            // обаботка разных случаев
            printMenu(); // печатаем меню ещё раз перед завершением предыдущего действия
            int userInput = scanner.nextInt(); // повторное считывание данных от пользователя

            if (userInput == 1) {
                if (manage.epicHashMap.isEmpty() && manage.subTaskHashMap.isEmpty()) {
                    System.out.println("Эпики и подзадачи не созданы или удалены. Необходимо создать");
                } else {
                    System.out.println("Получение списка эпиков: " + manage.epicHashMap);
                    System.out.println("Получение списка подзадач: " + manage.subTaskHashMap);
                }
            } else if (userInput == 2) {
                manage.removeAllTask();
            } else if (userInput == 3) {
                manage.getTaskWithId();
            } else if (userInput == 4) {
                manage.createEpicRandom();
                System.out.println("Эпики созданы");
                manage.createSubTaskRandom();
                System.out.println("Подзадачи созданы");
            } else if (userInput == 5) {
                manage.updateEpic();
            } else if (userInput == 6) {
                manage.removeTaskId();
            } else if (userInput == 7) {
                manage.getSubTaskEpic();
            } else if (userInput == 8) {
                System.out.println("Выход из приложения");
                return;
            } else {
                System.out.println("Введена несуществующая команда. Повторите попытку");
            }
        }

    }

    public static void printMenu() {
        System.out.println("\n"
                + "Выберите один из пунктов меню:" + "\n"
                + "1 - Получение списка всех задач;" + "\n" //сделано
                + "2 - Удаление всех задач;" + "\n" //сделано
                + "3 - Получение задачи по идентификатору;" + "\n" //сделано
                + "4 - Создание эпиков и подзадач (random data);" + "\n" //сделано
                + "5 - Обновление (random data);" + "\n" // сделано
                + "6 - Удаление по идентификатору;" + "\n" //сделано
                + "7 - Получение списка подзадач эпика;" + "\n" //сделано
                + "8 - Выйти из приложения.");
    }

}
