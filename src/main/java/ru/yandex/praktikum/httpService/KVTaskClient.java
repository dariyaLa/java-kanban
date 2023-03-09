package ru.yandex.praktikum.httpService;

import com.google.gson.Gson;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.utilits.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private static Gson gson = Managers.getGson();
    private String url;
    private HttpClient client;
    private String API_TOKEN;

    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "/register");
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Код состояния: " + response.statusCode() +
                        "Ответ: " + response.body());
            }
            API_TOKEN = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Код состояния: " + response.statusCode() +
                        "Ответ: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Код состояния: " + response.statusCode() +
                        "Ответ: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return response.body();
    }
}