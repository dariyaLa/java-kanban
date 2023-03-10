package ru.yandex.praktikum.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.taskManager.FileBackedTasksManager;
import ru.yandex.praktikum.taskManager.TaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    FileBackedTasksManagerTest() {
    }

    public TaskManager getManagerEmptyFile() throws IOException, NotFoundExeption {
        FileBackedTasksManager fileBackedTasksManagerEmpty = new FileBackedTasksManager("fileEmpty.csv");
        FileBackedTasksManager.loadFromFile(fileBackedTasksManagerEmpty);
        return fileBackedTasksManagerEmpty;
    }

    public TaskManager getManagerEpicWithoutSubTask() throws IOException, NotFoundExeption {
        FileBackedTasksManager fileBackedTasksManagerEmpty = new FileBackedTasksManager("fileEpicWithoutSubTask.csv");
        FileBackedTasksManager.loadFromFile(fileBackedTasksManagerEmpty);
        return fileBackedTasksManagerEmpty;
    }

    @Override
    public TaskManager getManager() throws IOException, NotFoundExeption {
        String pathFileManagerOut = "file.csv";
        FileBackedTasksManager fileBackedTasksManagerFromFile = new FileBackedTasksManager(pathFileManagerOut);
        FileBackedTasksManager.loadFromFile(fileBackedTasksManagerFromFile);
        return fileBackedTasksManagerFromFile;
    }

    //пустой список подзадач
    @Test
    public void createSubTaskEmptyFile() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmptyFile();
        assertEquals(0, manager.getSubTaskHashMapList().size(), "Список подзадач не пуст");
    }

    //пустой список задач
    @Test
    public void createTaskEmptyFile() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmptyFile();
        assertEquals(0, manager.getTaskHashMapList().size(), "Список задач не пуст");
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