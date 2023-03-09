package ru.yandex.praktikum.httpService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private final Gson gson = Managers.getGson();
    private TaskManager taskManager;
    private final String PATH = "http://localhost:8080";

    @Test
    public void getEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);
    }

    @Test
    public void getSubTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/subTask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);
    }


    @Test
    public void getTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);

    }

    @Test
    public void createEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/epic");
        Epic epicNew = new Epic("Эпик New", "Эпик New Описание", 5, new ArrayList<>(), Status.NEW,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        String json = gson.toJson(epicNew);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, taskManager.getEpicHashMapList().size(), "Эпик не создан");

    }

    @Test
    public void createSubTaskTest() throws IOException, InterruptedException, NotFoundExeption {
        final int EXPECTED_COUNT_SUBTASK = 3;
        final int ID_EPIC = 1;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/subTask");
        SubTask subTaskNew = new SubTask("Подзадача New", "Подзадача New Описание", 6, Status.NEW, 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        String json = gson.toJson(subTaskNew);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_SUBTASK, taskManager.getSubTaskHashMapList().size(), "Подзадача не создана");
        assertEquals(EXPECTED_COUNT_SUBTASK, taskManager.getEpicById(ID_EPIC).getSubTaskList().size(), "Кол-во подзадач в эпике не увеличилось");
    }

    @Test
    public void createTaskTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_TASK = 2;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/task");
        Task taskNew = new Task("Задача New", "Задача New Описание", 7, Status.NEW,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        String json = gson.toJson(taskNew);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_TASK, taskManager.getTaskHashMapList().size(), "Задача не создана");
    }

    @Test
    public void deleteEpicsTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_EPICS = 0;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_EPICS, taskManager.getEpicHashMapList().size(), "Эпики не удалены");
    }

    @Test
    public void deleteEpicIdTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_EPICS = 0;
        final int ID_EPIC_FOR_DELETE = 1;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/epic?id=" + ID_EPIC_FOR_DELETE);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_EPICS, taskManager.getEpicHashMapList().size(), "Эпик не удален");
    }

    @Test
    public void deleteSubTasksTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_TASKS = 0;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/subTask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_TASKS, taskManager.getSubTaskHashMapList().size(), "Подзадачи не удалены");
    }

    @Test
    public void deleteSubTaskIdTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_SUBTASKS = 1;
        final int ID_SUBTASK_FOR_DELETE = 2;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/subTask?id=" + ID_SUBTASK_FOR_DELETE);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_SUBTASKS, taskManager.getSubTaskHashMapList().size(), "Подзадача не удалена");
    }

    @Test
    public void deleteTasksTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_TASKS = 0;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_TASKS, taskManager.getTaskHashMapList().size(), "Задачи не удалены");
    }

    @Test
    public void deleteTaskIdTest() throws IOException, InterruptedException {
        final int EXPECTED_COUNT_TASKS = 0;
        final int ID_SUBTASK_FOR_DELETE = 4;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PATH + "/tasks/task?id=" + ID_SUBTASK_FOR_DELETE);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(EXPECTED_COUNT_TASKS, taskManager.getTaskHashMapList().size(), "Задача не удалена");
    }


    @BeforeEach
    void setUp() throws IOException, NotFoundExeption {
        taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание",
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        taskManager.createEpic(epic);
        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(11, 0)), Duration.ofMinutes(60));
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        taskManager.createSubTask(subTaskOneEpicOne);
        taskManager.createSubTask(subTaskTwoEpicOne);
        Task task = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.of(LocalDate.of(2022, 2, 20), LocalTime.of(12, 0)), Duration.ofMinutes(60));
        taskManager.createTask(task);
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }
}