import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        Manage manage = new Manage();
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание");
        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1);
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1);
        SubTask subTaskTwoUpdate = new SubTask();


        while (true) {
            // обаботка разных случаев
            printMenu(); // печатаем меню ещё раз перед завершением предыдущего действия
            int userInput = scanner.nextInt(); // повторное считывание данных от пользователя

            if (userInput == 1) {
                if (manage.getEpicHashMap().isEmpty() && manage.getSubTaskHashMap().isEmpty()) {
                    System.out.println("Эпики и подзадачи не созданы или удалены. Необходимо создать");
                } else {
                    System.out.println("Получение списка эпиков: " + manage.getEpicHashMap());
                    System.out.println("Получение списка подзадач: " + manage.getSubTaskHashMap());
                }
            } else if (userInput == 2) {
                manage.removeAllTask();
            } else if (userInput == 3) {
                System.out.println("Введите идентификатор задачи, которую хотите получить");
                userInput = scanner.nextInt();
                System.out.println(manage.getTaskWithId(userInput));
            } else if (userInput == 4) {
                manage.createEpic(epic);
                System.out.println("Эпик создан");
                manage.createSubTask(subTaskOneEpicOne);
                manage.createSubTask(subTaskTwoEpicOne);
                System.out.println("Подзадачи созданы");
            } else if (userInput == 5) {
                System.out.println("Обновление подзадачи");
                subTaskTwoUpdate.setName(subTaskTwoEpicOne.getName());
                subTaskTwoUpdate.setDiscription(subTaskTwoEpicOne.getDiscription());
                subTaskTwoUpdate.setEpicId(subTaskTwoEpicOne.getEpicId());
                subTaskTwoUpdate.setStatus(Status.IN_PROGRESS);
                subTaskTwoUpdate.setId(3);
                System.out.println(subTaskTwoUpdate);

                manage.updateTask(subTaskTwoUpdate);
                System.out.println(manage.getSubTaskHashMap().get(3));
            } else if (userInput == 6) {
                System.out.println("Введите идентификатор задачи, которую хотите удалить");
                userInput = scanner.nextInt();
                manage.removeTaskId(userInput);
            } else if (userInput == 7) {
                System.out.println("Введите идентификатор эпика, для получения списка его подзадач");
                userInput = scanner.nextInt();
                System.out.println(manage.getSubTaskEpic(userInput));
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
