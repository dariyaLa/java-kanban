import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.taskManager.FileBackedTasksManager;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание");
        Epic epic2 = new Epic("Эпик 1", "Эпик 1 Описание");
        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1);
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1);
        SubTask subTaskThreeEpicOne = new SubTask("Подзадача 3", "Подзадача 3 Описание", 1);
        SubTask subTaskTwoUpdate = new SubTask();
        Task task = new Task("Задача 1", "Описание задачи 1");
        SubTask taskUpdate = new SubTask("Задача 1", "Описание задачи 1 обновленное", 3, Status.NEW, 1);
        TaskManager taskManager = Managers.getDefault();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        FileBackedTasksManager fileBackedTasksManagerFromFile = new FileBackedTasksManager();
        //FileReader fileReader = new FileReader("file.csv");
        //Writer fileWriter = new FileWriter("file.csv", true);

        while (true) {
            // обаботка разных случаев
            printMenu(); // печатаем меню ещё раз перед завершением предыдущего действия
            int userInput = scanner.nextInt(); // повторное считывание данных от пользователя

            if (userInput == 1) {
                if (taskManager.getEpicHashMap().isEmpty() && taskManager.getSubTaskHashMap().isEmpty() && taskManager.getTaskHashMap().isEmpty()) {
                    System.out.println("Эпики и задачи не созданы или были удалены. Необходимо создать");
                } else {
                    System.out.println("Получение списка эпиков: " + taskManager.getEpicHashMap());
                    System.out.println("Получение списка подзадач: " + taskManager.getSubTaskHashMap());
                    System.out.println("Получение списка задач: " + taskManager.getTaskHashMap());
                }
            } else if (userInput == 2) {
                taskManager.removeTasks();
                taskManager.removeAlLSubTasks();
                taskManager.removeAllEpics();
            } else if (userInput == 3) {
                System.out.println("Введите идентификатор задачи, которую хотите получить");
                userInput = scanner.nextInt();
                System.out.println(taskManager.getEpicById(taskManager.getEpicHashMap(), userInput));
            } else if (userInput == 4) {
                taskManager.createEpic(epic);
                System.out.println("Эпик создан");
                taskManager.createEpic(epic2);
                taskManager.createSubTask(subTaskOneEpicOne);
                taskManager.createSubTask(subTaskTwoEpicOne);
                taskManager.createSubTask(subTaskThreeEpicOne);
                System.out.println("Подзадачи созданы");
                taskManager.createTask(task);
                System.out.println("Зачада создана");
            } else if (userInput == 5) {
                System.out.println("Обновление подзадачи");
                subTaskTwoUpdate.setName(subTaskTwoEpicOne.getName());
                subTaskTwoUpdate.setDiscription(subTaskTwoEpicOne.getDiscription());
                subTaskTwoUpdate.setEpicId(subTaskTwoEpicOne.getEpicId());
                subTaskTwoUpdate.setStatus(Status.IN_PROGRESS);
                subTaskTwoUpdate.setId(3);
                System.out.println(subTaskTwoUpdate);
                taskManager.updateSubTask(subTaskTwoUpdate);
                System.out.println(taskManager.getSubTaskHashMap().get(3));
                taskManager.updateTask(taskUpdate);
                System.out.println(taskManager.getTaskHashMap());
            } else if (userInput == 6) {
                System.out.println("Введите идентификатор эпика, который хотите удалить");
                userInput = scanner.nextInt();
                taskManager.removeEpicId(userInput);
            } else if (userInput == 7) {
                System.out.println("Введите идентификатор задачи, которую хотите удалить");
                userInput = scanner.nextInt();
                taskManager.removeSubTaskId(userInput);
            } else if (userInput == 8) {
                System.out.println("Введите идентификатор эпика, для получения списка его подзадач");
                userInput = scanner.nextInt();
                System.out.println(taskManager.getSubTaskEpic(taskManager.getEpicHashMap(), userInput));
            } else if (userInput == 9) {
                System.out.println("Тестирование истории");
                System.out.println(taskManager.getInMemoryHistoryManager());
            } else if (userInput == 10) {
                System.out.println("Введите идентификатор подзадачи, которую хотите получить");
                userInput = scanner.nextInt();
                System.out.println(taskManager.getSubTaskById(userInput));
            } else if (userInput == 11) {
                fileBackedTasksManager.createEpic(epic);
                fileBackedTasksManager.createTask(task);
                fileBackedTasksManager.createSubTask(subTaskOneEpicOne);
                System.out.println("Созданы эпик, подзадача, задача");
                System.out.println("Все задачи записаны в файл");
                System.out.println(fileBackedTasksManager.getEpicById(fileBackedTasksManager.getEpicHashMap(), 1));
                System.out.println("Для истории получен эпик по идентификатору");
                FileBackedTasksManager.historyToString(fileBackedTasksManager.getInMemoryHistoryManagerDefault());
                System.out.println("История записана в файл");
            } else if (userInput == 12) {
                FileBackedTasksManager.loadFromFile(new FileReader("file.csv"), fileBackedTasksManagerFromFile);
                System.out.println("Задачи считаны из файла");
            } else if (userInput == 13) {
                System.out.println("История считана из файла");
                System.out.println(FileBackedTasksManager.historyFromString(fileBackedTasksManagerFromFile));
            } else if (userInput == 0) {
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
                + "3 - Получение эпика по идентификатору;" + "\n" //сделано
                + "4 - Создание эпиков и подзадач (random data);" + "\n" //сделано
                + "5 - Обновление;" + "\n" // сделано
                + "6 - Удаление эпика по идентификатору;" + "\n" //сделано
                + "7 - Удаление подзадачи по идентификатору;" + "\n" //сделано
                + "8 - Получение списка подзадач эпика;" + "\n" //сделано
                + "9 - Тестирование истории;" + "\n" //сделано
                + "10 - Получение подзадачи по идентификатору;" + "\n" //сделано
                + "11 - Запись в файл;" + "\n" //сделано
                + "12 - Чтение из файла;" + "\n" //сделано
                + "13 - Считать историю из файла;" + "\n" //сделано
                + "0 - Выйти из приложения.");
    }

}
