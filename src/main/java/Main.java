import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Manage manage = new Manage();
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание");
        Epic epic2 = new Epic("Эпик 1", "Эпик 1 Описание");
        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1);
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1);
        SubTask subTaskThreeEpicOne = new SubTask("Подзадача 3", "Подзадача 3 Описание", 1);
        SubTask subTaskTwoUpdate = new SubTask();
        Task task = new Task("Задача 1", "Описание задачи 1");
        SubTask taskUpdate = new SubTask("Задача 1", "Описание задачи 1 обновленное", 3 , Status.NEW, 1);

        while (true) {
            // обаботка разных случаев
            printMenu(); // печатаем меню ещё раз перед завершением предыдущего действия
            int userInput = scanner.nextInt(); // повторное считывание данных от пользователя

            if (userInput == 1) {
                if (manage.getEpicHashMap().isEmpty() && manage.getSubTaskHashMap().isEmpty() && manage.getTaskHashMap().isEmpty()) {
                    System.out.println("Эпики и задачи не созданы или были удалены. Необходимо создать");
                } else {
                    System.out.println("Получение списка эпиков: " + manage.getEpicHashMap());
                    System.out.println("Получение списка подзадач: " + manage.getSubTaskHashMap());
                    System.out.println("Получение списка задач: " + manage.getTaskHashMap());
                }
            } else if (userInput == 2) {
                manage.removeTasks();
                manage.removeAlLSubTasks();
                manage.removeAllEpics();
            } else if (userInput == 3) {
                System.out.println("Введите идентификатор задачи, которую хотите получить");
                userInput = scanner.nextInt();
                System.out.println(manage.getEpicById(manage.getEpicHashMap(),userInput));
            } else if (userInput == 4) {
                manage.createEpic(epic);
                System.out.println("Эпик создан");
                manage.createEpic(epic2);
                manage.createSubTask(subTaskOneEpicOne);
                manage.createSubTask(subTaskTwoEpicOne);
                manage.createSubTask(subTaskThreeEpicOne);
                System.out.println("Подзадачи созданы");
                manage.createTask(task);
                System.out.println("Зачада создана");
            } else if (userInput == 5) {
                System.out.println("Обновление подзадачи");
                subTaskTwoUpdate.setName(subTaskTwoEpicOne.getName());
                subTaskTwoUpdate.setDiscription(subTaskTwoEpicOne.getDiscription());
                subTaskTwoUpdate.setEpicId(subTaskTwoEpicOne.getEpicId());
                subTaskTwoUpdate.setStatus(Status.IN_PROGRESS);
                subTaskTwoUpdate.setId(3);
                System.out.println(subTaskTwoUpdate);
                manage.updateSubTask(subTaskTwoUpdate);
                System.out.println(manage.getSubTaskHashMap().get(3));
                manage.updateTask(taskUpdate);
                System.out.println(manage.getTaskHashMap());
            } else if (userInput == 6) {
                System.out.println("Введите идентификатор эпика, который хотите удалить");
                userInput = scanner.nextInt();
                manage.removeEpicId(userInput);
            } else if (userInput == 7) {
                System.out.println("Введите идентификатор эпика, для получения списка его подзадач");
                userInput = scanner.nextInt();
                System.out.println(manage.getSubTaskEpic(manage.getEpicHashMap(),userInput));
            } else if (userInput == 8) {
                System.out.println("Выход из приложения");
                return;
            } else if (userInput == 9) {
                System.out.println("Для тестирования");
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
                + "5 - Обновление;" + "\n" // сделано
                + "6 - Удаление по идентификатору;" + "\n" //сделано
                + "7 - Получение списка подзадач эпика;" + "\n" //сделано
                + "9 - Тестирование;" + "\n" //сделано
                + "8 - Выйти из приложения.");
    }

}
