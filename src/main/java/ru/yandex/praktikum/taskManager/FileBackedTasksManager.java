package ru.yandex.praktikum.taskManager;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.models.Tasks;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static boolean isStartHistory = false;
    public static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter("file.csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileBackedTasksManager() throws FileNotFoundException {
    }

    @Override
    public SubTask createSubTask(SubTask subtask) throws IOException {
        subtask = super.createSubTask(subtask);
        save(subTaskToString(subtask));
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) throws IOException {
        epic = super.createEpic(epic);
        save(epicToString(epic));
        return epic;
    }

    @Override
    public Task createTask(Task task) throws IOException {
        task = super.createTask(task);
        save(taskToString(task));
        return task;
    }

    private void save(String task) throws IOException {
        try {
            fileWriter.write(task);
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи в файл");
        }

    }

    public String subTaskToString(SubTask subTask) {
        String tasksToString = "";
        return tasksToString = tasksToString + subTask.getId() + "," + Tasks.SUBTASK + "," + subTask.getName()
                + "," + subTask.getStatus()
                + "," + subTask.getDiscription()
                + "," + subTask.getEpicId()
                + "\n";
    }

    public String taskToString(Task task) {
        String tasksToString = "";
        return tasksToString = tasksToString + task.getId() + "," + Tasks.TASK + "," + task.getName()
                + "," + task.getStatus()
                + "," + task.getDiscription()
                + "\n";
    }

    public String epicToString(Epic epic) {
        String tasksToString = "";
        return tasksToString = tasksToString + epic.getId() + "," + Tasks.EPIC + "," + epic.getName()
                + "," + epic.getStatus()
                + "," + epic.getDiscription()
                + "\n";
    }

    @Override
    public Epic getEpicById(List<Epic> epicList, int taskId) {
        return super.getEpicById(epicList, taskId);
    }

    //записываем историю в файл
    public static void historyToString(HistoryManager manager) throws IOException {
        try {
            fileWriter.write("\n");
            manager.getHistory().stream().forEach(x -> {
                try {
                    fileWriter.write(Integer.toString(x.getId()));
                    fileWriter.write(",");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //создаем менеджера из файла (читаем файл)
    public static void fromString(TaskManager taskManager, FileReader fileReader) throws IOException {
        BufferedReader br = new BufferedReader(fileReader);
        HashMap<Integer, Epic> epicHashMap = taskManager.setEpicHashMap();
        HashMap<Integer, SubTask> subTaskHashMap = taskManager.setSubTaskHashMap();
        HashMap<Integer, Task> taskHashMap = taskManager.setTaskHashMap();

        while (br.ready()) {
            String line = br.readLine();

            if (line.contains(",") && !isStartHistory) {
                String[] lineSplit = line.split(",");
                if (Tasks.getTask(lineSplit[1]) != null) {
                    switch (Tasks.valueOf(lineSplit[1])) {
                        case EPIC:
                            epicHashMap.put(Integer.valueOf(lineSplit[0]), new Epic(lineSplit[2], lineSplit[4],
                                    Integer.valueOf(lineSplit[0]), null, Status.valueOf(lineSplit[3])));
                            break;
                        case SUBTASK:
                            subTaskHashMap.put(Integer.valueOf(lineSplit[0]), new SubTask(lineSplit[2], lineSplit[4],
                                    Integer.valueOf(lineSplit[0]), Status.valueOf(lineSplit[3]), Integer.valueOf(lineSplit[5])));
                            break;
                        case TASK:
                            taskHashMap.put(Integer.valueOf(lineSplit[0]), new Task(lineSplit[2], lineSplit[4],
                                    Integer.valueOf(lineSplit[0]), Status.valueOf(lineSplit[3])));
                            break;

                    }
                }
            } else if (!line.contains(",")) {
                break;
            }
        }

        fileReader.close();
    }

    //Восстановление истории из файла
    public static List<Task> historyFromString(TaskManager taskManager) throws IOException {
        FileReader fileReader = new FileReader("file.csv");
        BufferedReader br = new BufferedReader(fileReader);
        boolean isStartHistory = false;

        while (br.ready()) {
            String line = br.readLine();

            if (!line.contains(",") && !isStartHistory) {
                isStartHistory = true;

            } else if (isStartHistory) {

                String[] lineSplit = line.split(",");

                for (String lineTemp : lineSplit) {
                    if (taskManager.getEpicById(taskManager.getEpicHashMap(), Integer.valueOf(lineTemp)) != null) {
                        Task task = taskManager.getEpicById(taskManager.getEpicHashMap(), Integer.valueOf(lineTemp));
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

            } else {
            }
        }
        return taskManager.getInMemoryHistoryManager();
    }

    public static void loadFromFile(FileReader fileReader, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        fromString(fileBackedTasksManager, fileReader);
    }

    public static FileWriter getFileWriter() {
        return fileWriter;
    }
}