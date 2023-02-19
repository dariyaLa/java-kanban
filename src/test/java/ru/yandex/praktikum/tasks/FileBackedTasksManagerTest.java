package ru.yandex.praktikum.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.taskManager.FileBackedTasksManager;
import ru.yandex.praktikum.taskManager.InMemoryTaskManager;
import ru.yandex.praktikum.taskManager.TaskManager;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    FileBackedTasksManager fileBackedTasksManagerFromFile = new FileBackedTasksManager();

    public TaskManager getManagerEmptyFile() throws IOException, NotFoundExeption {
        FileBackedTasksManager.loadFromFile(new FileReader("fileEmpty.csv"), fileBackedTasksManagerFromFile);
        return fileBackedTasksManagerFromFile;
    }

    public TaskManager getManagerEpicWithoutSubTask() throws IOException, NotFoundExeption {
        FileBackedTasksManager.loadFromFile(new FileReader("fileEpicWithoutSubTask.csv"), fileBackedTasksManagerFromFile);
        return fileBackedTasksManagerFromFile;
    }

    @Override
    public TaskManager getManager() throws IOException, NotFoundExeption {
        FileBackedTasksManager.loadFromFile(new FileReader("file.csv"), fileBackedTasksManagerFromFile);
        return fileBackedTasksManagerFromFile;
    }

    //пустой список подзадач
    @Test
    public void createSubTaskEmptyFile() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmptyFile();
        assertEquals(0, manager.getSubTaskHashMap().size(), "Список подзадач не пуст");
    }

    //пустой список задач
    @Test
    public void createTaskEmptyFile() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmptyFile();
        assertEquals(0, manager.getTaskHashMap().size(), "Список задач не пуст");
    }

    //пустой список истории
    @Test
    public void createHistoryEmptyFile() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmptyFile();
        assertEquals(0, manager.getInMemoryHistoryManager().size(), "Список истории не пуст");
    }

    //пустой список истории
    @Test
    public void createWithoutSubTaskList() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmptyFile();
        assertEquals(0, manager.getInMemoryHistoryManager().size(), "Список истории не пуст");
    }

    @Test
    public void checkHistory() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        assertEquals(1, manager.getInMemoryHistoryManager().size(), "Список истории пуст");
    }

    //эпик без подзадач
    @Test
    public void checkEpicSubTask() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEpicWithoutSubTask();
        assertEquals(0, manager.getEpicById(1).getSubTaskList().size(), "Список подзадач не пуст");
    }

}