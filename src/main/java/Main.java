import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.httpService.KVServer;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.taskManager.FileBackedTasksManager;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, NotFoundExeption, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание",
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));

        Epic epic2 = new Epic("Эпик 2", "Эпик 2 Описание",
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));

        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(11, 0)), Duration.ofMinutes(60));
        SubTask subTaskThreeEpicOne = new SubTask("Подзадача 3", "Подзадача 3 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        SubTask subTaskForEpicOne = new SubTask("Подзадача 4", "Подзадача 4 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 20)), Duration.ofMinutes(20));
        SubTask subTaskNotStartTimeEpicOne = new SubTask("Подзадача 5", "Подзадача 5 Описание", 1,
                null, Duration.ofMinutes(60));

        SubTask subTaskTwoUpdate = new SubTask();

        Task task = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.of(LocalDate.of(2022, 2, 20), LocalTime.of(12, 0)), Duration.ofMinutes(60));
        Task taskTwo = new Task("Задача 2", "Описание задачи 2",
                LocalDateTime.of(LocalDate.of(2022, 2, 20), LocalTime.of(12, 0)), Duration.ofMinutes(60));

        SubTask taskUpdate = new SubTask("Задача 1", "Описание задачи 1 обновленное", 3, Status.NEW, 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        TaskManager taskManager = Managers.getDefault();
        String fileNameManager = "file.csv";
        TaskManager fileBackedTasksManager = Managers.getDefaultFile(fileNameManager);

        while (true) {
            // обаботка разных случаев
            printMenu(); // печатаем меню ещё раз перед завершением предыдущего действия
            int userInput = scanner.nextInt(); // повторное считывание данных от пользователя

            if (userInput == 1) {
                if (taskManager.getEpicHashMapList().isEmpty() && taskManager.getSubTaskHashMapList().isEmpty() && taskManager.getTaskHashMapList().isEmpty()) {
                    System.out.println("Эпики и задачи не созданы или были удалены. Необходимо создать");
                } else {
                    System.out.println("Получение списка эпиков: " + taskManager.getEpicHashMapList());
                    System.out.println("Получение списка подзадач: " + taskManager.getSubTaskHashMapList());
                    System.out.println("Получение списка задач: " + taskManager.getTaskHashMapList());
                }
            } else if (userInput == 2) {
                taskManager.removeTasks();
                taskManager.removeAlLSubTasks();
                taskManager.removeAllEpics();
            } else if (userInput == 3) {
                System.out.println("Введите идентификатор задачи, которую хотите получить");
                userInput = scanner.nextInt();
                System.out.println(taskManager.getEpicByIdClient(userInput));
            } else if (userInput == 4) {
                taskManager.createEpic(epic);
                System.out.println("Эпик создан");
                //taskManager.createEpic(epic2);
                taskManager.createSubTask(subTaskOneEpicOne);
                taskManager.createSubTask(subTaskTwoEpicOne);
                taskManager.createSubTask(subTaskThreeEpicOne);
                taskManager.createSubTask(subTaskNotStartTimeEpicOne);
                taskManager.createSubTask(subTaskForEpicOne);

                System.out.println("Подзадачи созданы");
                taskManager.createTask(task);
                taskManager.createTask(taskTwo);
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
                System.out.println(taskManager.getSubTaskHashMapList().get(3));
                taskManager.updateTask(taskUpdate);
                System.out.println(taskManager.getTaskHashMapList());
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
                System.out.println(taskManager.getSubTaskEpic(userInput));
            } else if (userInput == 9) {
                System.out.println("Тестирование истории");
                System.out.println(taskManager.getInMemoryHistoryManager());
            } else if (userInput == 10) {
                System.out.println("Введите идентификатор подзадачи, которую хотите получить");
                userInput = scanner.nextInt();
                System.out.println(taskManager.getSubTaskById(userInput));
            } else if (userInput == 11) {
                fileBackedTasksManager.createEpic(epic);
                fileBackedTasksManager.createSubTask(subTaskOneEpicOne);
                fileBackedTasksManager.createSubTask(subTaskOneEpicOne);
                fileBackedTasksManager.createTask(task);
                System.out.println("Созданы эпик, подзадача, задача");
                System.out.println("Все задачи записаны в файл");
                fileBackedTasksManager.getEpicByIdClient(1);
                //System.out.println(fileBackedTasksManager.getEpicById(1));
                System.out.println("Для истории получен эпик по идентификатору");
                //fileBackedTasksManager.historyToString(fileBackedTasksManager.getInMemoryHistoryManagerDefault());
                System.out.println("История записана в файл");
            } else if (userInput == 12) {
                FileBackedTasksManager.loadFromFile(fileBackedTasksManager);
                System.out.println("Задачи считаны из файла");
                System.out.println(fileBackedTasksManager.getTaskHashMapList());
                System.out.println(fileBackedTasksManager.getTaskHashMapList());
                System.out.println(fileBackedTasksManager.getTaskHashMapList());

            } else if (userInput == 13) {
                System.out.println("История считана из файла");
               // System.out.println(FileBackedTasksManager.historyFromString(fileBackedTasksManagerFromFile));
            } else if (userInput == 14) {
                System.out.println("Время завершения задачи:" + taskManager.getEndTime(taskManager.foundTask(4)));
            } else if (userInput == 15) {
                System.out.println("Приоритезированный список:" + taskManager.getTaskTreeSetPrioritized());
            } else if (userInput == 16) {
                //запускаем сервер
                KVServer kvServer = new KVServer();
                kvServer.start();
                //кладем данные менеджера на сервер
                TaskManager httpTaskManager = Managers.getDefaultServer("http://localhost:8078");
                httpTaskManager.createEpic(epic);
                httpTaskManager.createSubTask(subTaskForEpicOne);
                httpTaskManager.createTask(task);
                //дергаем эпик,чтобы он попал в историю
                httpTaskManager.getEpicByIdClient(1);
                System.out.println("Данные на сервере");
                //создаем нового менеджера и дублируем в него состояние первого из файла
                TaskManager httpTaskManagerload = Managers.getDefaultServer("http://localhost:8078");
                System.out.println("Данные c сервера выгружены");
                System.out.println(httpTaskManagerload.getEpicHashMapList());
                System.out.println(httpTaskManagerload.getInMemoryHistoryManager());

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
                + "14 - Посчитать продолжительность эпика;" + "\n" //сделано
                + "15 - Вывести приоритезированный список задач (по startTime);" + "\n" //сделано
                + "16 - Выгрузить менеджера с сервера" + "\n" //сделано
                + "0 - Выйти из приложения.");
    }

}