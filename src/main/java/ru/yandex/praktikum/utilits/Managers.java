package ru.yandex.praktikum.utilits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.history.InMemoryHistoryManager;
import ru.yandex.praktikum.httpService.DurationAdapter;
import ru.yandex.praktikum.httpService.LocalDateAdapter;
import ru.yandex.praktikum.taskManager.FileBackedTasksManager;
import ru.yandex.praktikum.taskManager.HttpTaskManager;
import ru.yandex.praktikum.taskManager.InMemoryTaskManager;
import ru.yandex.praktikum.taskManager.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFile(String path) throws IOException {
        return new FileBackedTasksManager(path);
    }

    public static TaskManager getDefaultServer(String s) throws IOException {
        return new HttpTaskManager(s);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}