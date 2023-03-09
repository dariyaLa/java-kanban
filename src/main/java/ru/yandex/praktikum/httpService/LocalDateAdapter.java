package ru.yandex.praktikum.httpService;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime startTime) throws IOException {
        jsonWriter.value(startTime.format(formatterWriter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}