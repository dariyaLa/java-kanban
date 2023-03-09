package ru.yandex.praktikum.taskManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.praktikum.httpService.KVTaskClient;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String url) throws IOException {
        super(url);
        client = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    @Override
    public void save(String task) {
        client.put("epics", gson.toJson(getEpicHashMapList()));
        client.put("subTasks", gson.toJson(getSubTaskHashMapList()));
        client.put("tasks", gson.toJson(getTaskHashMapList()));
    }

    @Override
    public Epic getEpicByIdClient(int taskId) {
        for (Epic i : getEpicHashMapList()) {
            if (i.getId() == taskId) {
                inMemoryHistoryManager.add(i);
                client.put("history", gson.toJson(getInMemoryHistoryManager().stream()
                        .map(Task::getId)
                        .collect(Collectors.toList()))); //добавили вызов сервера
                return i;
            }
        }
        return null;
    }

    public static HttpTaskManager fromServer(String url) throws IOException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        String json = httpTaskManager.client.load("epics");
        if (!json.isEmpty()) {
            Type epicsType = new TypeToken<List<Epic>>() {
            }.getType();
            List<Epic> epicList = httpTaskManager.getGson().fromJson(json, epicsType);
            for (Epic epic : epicList) {
                httpTaskManager.getEpicHashMap().put(epic.getId(), epic);
            }
        }
        json = httpTaskManager.client.load("subTasks");
        if (!json.isEmpty()) {
            Type subTaskType = new TypeToken<List<SubTask>>() {
            }.getType();
            List<SubTask> subTaskList = httpTaskManager.getGson().fromJson(json, subTaskType);
            for (SubTask subTask : subTaskList) {
                httpTaskManager.getSubTaskHashMap().put(subTask.getId(), subTask);
            }
        }
        json = httpTaskManager.client.load("tasks");
        if (!json.isEmpty()) {
            Type taskType = new TypeToken<List<Task>>() {
            }.getType();
            List<Task> taskList = httpTaskManager.getGson().fromJson(json, taskType);
            for (Task task : taskList) {
                httpTaskManager.getTaskHashMap().put(task.getId(), task);
            }
        }

        json = httpTaskManager.client.load("history");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> taskList = httpTaskManager.getGson().fromJson(json, type);
            for (Integer task : taskList) {
                if (httpTaskManager.getEpicHashMap().get(task) != null) {
                    httpTaskManager.inMemoryHistoryManager.add(httpTaskManager.getEpicHashMap().get(task));
                } else if (httpTaskManager.getSubTaskHashMap().get(task) != null) {
                    httpTaskManager.inMemoryHistoryManager.add(httpTaskManager.getSubTaskHashMap().get(task));
                } else if (httpTaskManager.getTaskHashMap().get(task) != null) {
                    httpTaskManager.inMemoryHistoryManager.add(httpTaskManager.getTaskHashMap().get(task));
                }
            }
        }

        return httpTaskManager;
    }

    public KVTaskClient getClient() {
        return client;
    }

    public Gson getGson() {
        return gson;
    }
}