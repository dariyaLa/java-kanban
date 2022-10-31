package ru.yandex.praktikum.utilits;

import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.history.InMemoryHistoryManager;
import ru.yandex.praktikum.taskManager.InMemoryTaskManager;
import ru.yandex.praktikum.taskManager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
