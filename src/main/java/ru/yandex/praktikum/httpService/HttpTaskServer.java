package ru.yandex.praktikum.httpService;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private Gson gson;
    private HttpServer server;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());

    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/epic", this::handleEpic);
        server.createContext("/tasks/subTask", this::handleSubTask);
        server.createContext("/tasks/task", this::handleTask);
        server.createContext("/tasks/history", this::handleTaskHistory);

    }


    private void handleEpic(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        String response = gson.toJson(taskManager.getEpicHashMapList());
                        sendText(httpExchange, response);

                    } else httpExchange.sendResponseHeaders(405, 0);
                    return;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epicNew = gson.fromJson(body, Epic.class);
                        taskManager.createEpicAllFields(epicNew);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    return;
                }
                case "DELETE": {
                    String pathQuery = httpExchange.getRequestURI().getQuery();
                    if (pathQuery != null) {
                        if (Pattern.matches("^id=\\d+$", pathQuery)) {
                            String queryId = pathQuery.replace("id=", "");
                            int id = parsePathId(queryId);
                            if (id != -1) {
                                taskManager.removeEpicId(id);
                                httpExchange.sendResponseHeaders(200, 0);
                                return;
                            }
                        }
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        taskManager.removeAllEpics();
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                }
                default: {
                    System.out.println("Метод не иденцифирован");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception | NotFoundExeption exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubTask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/subTask$", path)) {
                        String response = gson.toJson(taskManager.getSubTaskHashMapList());
                        sendText(httpExchange, response);

                    } else httpExchange.sendResponseHeaders(405, 0);
                    return;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/subTask$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subTaskNew = gson.fromJson(body, SubTask.class);
                        taskManager.createSubTaskAllFields(subTaskNew);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    return;
                }
                case "DELETE": {
                    String pathQuery = httpExchange.getRequestURI().getQuery();
                    if (pathQuery != null) {
                        if (Pattern.matches("^id=\\d+$", pathQuery)) {
                            String queryId = pathQuery.replace("id=", "");
                            int id = parsePathId(queryId);
                            if (id != -1) {
                                taskManager.removeSubTaskId(id);
                                httpExchange.sendResponseHeaders(200, 0);
                                return;
                            }
                        }
                    } else if (Pattern.matches("^/tasks/subTask$", path)) {
                        taskManager.removeAlLSubTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                }
                default: {
                    System.out.println("Метод не иденцифирован");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception | NotFoundExeption exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleTask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/task$", path)) {
                        String response = gson.toJson(taskManager.getTaskHashMapList());
                        sendText(httpExchange, response);

                    } else httpExchange.sendResponseHeaders(405, 0);
                    return;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task taskNew = gson.fromJson(body, Task.class);
                        taskManager.createTaskAllFields(taskNew);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    return;
                }
                case "DELETE": {
                    String pathQuery = httpExchange.getRequestURI().getQuery();
                    if (pathQuery != null) {
                        if (Pattern.matches("^id=\\d+$", pathQuery)) {
                            String queryId = pathQuery.replace("id=", "");
                            int id = parsePathId(queryId);
                            if (id != -1) {
                                taskManager.removeTaskId(id);
                                httpExchange.sendResponseHeaders(200, 0);
                                return;
                            }
                        }
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        taskManager.removeTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                }
                default: {
                    System.out.println("Метод не иденцифирован");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception | NotFoundExeption exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleTaskHistory(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/history/$", path)) {
                        String response = gson.toJson(taskManager.getInMemoryHistoryManager());
                        sendText(httpExchange, response);

                    } else httpExchange.sendResponseHeaders(405, 0);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

/*    public static void main(String[] args) throws IOException, NotFoundExeption, InterruptedException {
        TaskManager taskManager = Managers.getDefaultFile("file.csv");
        FileBackedTasksManager.loadFromFile(taskManager);
        HttpTaskServer httpServer = new HttpTaskServer(taskManager);
        httpServer.start();
    }*/

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        //System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}