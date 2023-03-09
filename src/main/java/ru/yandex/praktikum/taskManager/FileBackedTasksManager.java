package ru.yandex.praktikum.taskManager;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.models.Tasks;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static boolean isStartHistory = false;
    private static String pathFileManager;
    private static FileReader fileReader;

    public FileBackedTasksManager(String path) {
        pathFileManager = path;
    }

    @Override
    public SubTask createSubTask(SubTask subtask) throws NotFoundExeption {
        subtask = super.createSubTask(subtask);
        save(subTaskToString(subtask));
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic = super.createEpic(epic);
        save(epicToString(epic));
        return epic;
    }

    @Override
    public Task createTask(Task task) throws NotFoundExeption {
        task = super.createTask(task);
        save(taskToString(task));
        return task;
    }

    protected void save(String task) {
        String[] fileName = pathFileManager.split("/");
        try (FileWriter fileWriter = new FileWriter(fileName[fileName.length - 1], true)) {//("file.csv", true)) {
            fileWriter.write(task);
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи в файл");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String subTaskToString(SubTask subTask) {
        String tasksToString = "";
        return tasksToString = tasksToString + subTask.getId() + "," + Tasks.SUBTASK + "," + subTask.getName()
                + "," + subTask.getStatus()
                + "," + subTask.getDiscription()
                + "," + subTask.getEpicId()
                + "," + subTask.getStartTime()
                + "," + subTask.getDuration()
                + "\n";
    }

    public String taskToString(Task task) {
        String tasksToString = "";
        return tasksToString = tasksToString + task.getId() + "," + Tasks.TASK + "," + task.getName()
                + "," + task.getStatus()
                + "," + task.getDiscription()
                + "," + task.getStartTime()
                + "," + task.getDuration()
                + "\n";
    }

    public String epicToString(Epic epic) {
        String tasksToString = "";
        return tasksToString = tasksToString + epic.getId() + "," + Tasks.EPIC + "," + epic.getName()
                + "," + epic.getStatus()
                + "," + epic.getDiscription()
                + "," + epic.getStartTime()
                + "," + epic.getDuration()
                + "\n";
    }

    @Override
    public Epic getEpicById(int taskId) throws NotFoundExeption {
        return super.getEpicById(taskId);
    }

    //записываем историю в файл
    public void historyToString(HistoryManager manager) throws IOException {
        String[] fileName = pathFileManager.split("/");
        try (FileWriter fileWriter = new FileWriter(fileName[fileName.length - 1], true)) {      //("file.csv", true)) {
            fileWriter.write("\n");
            manager.getHistory().stream().forEach(x -> {
                try {
                    fileWriter.write(Integer.toString(x.getId()));
                    fileWriter.write(",");
                } catch (ManagerSaveException e) {
                    System.out.println("Ошибка записи в файл");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    //создаем менеджера из файла (читаем файл)
    public static void fromString(TaskManager taskManager) throws IOException, NotFoundExeption {
        fileReader = new FileReader(pathFileManager);
        HashMap<Integer, Epic> epicHashMap = taskManager.setEpicHashMap();
        HashMap<Integer, SubTask> subTaskHashMap = taskManager.setSubTaskHashMap();
        HashMap<Integer, Task> taskHashMap = taskManager.setTaskHashMap();

        try (BufferedReader br = new BufferedReader(fileReader)) {
            while (br.ready()) {
                String line = br.readLine();

                if (line.contains(",") && !isStartHistory) {
                    String[] lineSplit = line.split(",");
                    if (Tasks.getTask(lineSplit[0]) != null) {
                        switch (Tasks.valueOf(lineSplit[0])) {
                            case EPIC:
                                Epic epic = new Epic(lineSplit[1], lineSplit[4],
                                        Integer.valueOf(lineSplit[2]), null, Status.valueOf(lineSplit[3]),
                                        LocalDateTime.parse(lineSplit[5]), Duration.parse(lineSplit[6]));
                                epicHashMap.put(Integer.valueOf(lineSplit[2]), epic);
                                break;
                            case SUBTASK:
                                SubTask subTask = new SubTask(lineSplit[1], lineSplit[4],
                                        Integer.valueOf(lineSplit[2]), Status.valueOf(lineSplit[3]), Integer.valueOf(lineSplit[5]),
                                        LocalDateTime.parse(lineSplit[6]), Duration.parse(lineSplit[7]));
                                subTaskHashMap.put(Integer.valueOf(lineSplit[2]), subTask);
                                prioritizedTasks(subTask);
                                break;
                            case TASK:
                                Task task = new Task(lineSplit[1], lineSplit[4],
                                        Integer.valueOf(lineSplit[2]), Status.valueOf(lineSplit[3]),
                                        LocalDateTime.parse(lineSplit[5]), Duration.parse(lineSplit[6]));
                                taskHashMap.put(Integer.valueOf(lineSplit[2]), task);
                                prioritizedTasks(task);
                                break;
                        }
                    }
                } else if (!line.contains(",")) {
                    break;
                }
            }

        }
        taskManager.getSubTaskHashMapList().stream().forEach(p -> {
            int epic_id = p.getEpicId();
            Epic epic = null;
            try {
                epic = taskManager.getEpicById(epic_id);
            } catch (NotFoundExeption e) {
                e.printStackTrace();
            }
            epic.getSubTaskList().add(p);
            try {
                taskManager.getEpicById(epic.getId()).calculationStartEndDurationEpic();
            } catch (NotFoundExeption e) {
                e.printStackTrace();
            }

        });

        historyFromString(taskManager);
    }

    //сведение эпиков и сабтасков из файла
    public static void addSubTaskInEpicFile(TaskManager taskManager) throws NotFoundExeption {
        taskManager.getSubTaskHashMapList().stream().forEach(p -> {
            try {
                taskManager.getEpicById(p.getEpicId()).getSubTaskList().add(p);
                taskManager.getEpicById(p.getEpicId()).calculationStartEndDurationEpic(); //пересчитываем время окончания эпика
            } catch (NotFoundExeption e) {
                e.printStackTrace();
            }
        });
    }

    //Восстановление истории из файла
    public static List<Task> historyFromString(TaskManager taskManager) throws IOException {
        boolean isStartHistory = false;

        try (BufferedReader br = new BufferedReader(new FileReader("file.csv"))) {
            while (br.ready()) {
                String line = br.readLine();

                if (!line.contains(",") && !isStartHistory) {
                    isStartHistory = true;

                } else if (isStartHistory) {

                    String[] lineSplit = line.split(",");

                    for (String lineTemp : lineSplit) {
                        if (taskManager.getEpicById(Integer.valueOf(lineTemp)) != null) {
                            Task task = taskManager.getEpicById(Integer.valueOf(lineTemp));
                            taskManager.getInMemoryHistoryManagerDefault().add(task);
                        } else if (taskManager.getTaskById(Integer.valueOf(lineTemp)) != null) {
                            Task task = taskManager.getTaskById(Integer.valueOf(lineTemp));
                            taskManager.getInMemoryHistoryManagerDefault().add(task);
                        } else if (taskManager.getSubTaskById(Integer.valueOf(lineTemp)) != null) {
                            Task task = taskManager.getSubTaskById(Integer.valueOf(lineTemp));
                            taskManager.getInMemoryHistoryManagerDefault().add(task);
                        }
                    }
                    isStartHistory = false;
                }
            }
        } catch (NotFoundExeption notFoundExeption) {
            notFoundExeption.printStackTrace();
        }
        return taskManager.getInMemoryHistoryManager();
    }

    public static void loadFromFile(TaskManager fileBackedTasksManager) throws IOException, NotFoundExeption {
        fromString(fileBackedTasksManager);

    }
}