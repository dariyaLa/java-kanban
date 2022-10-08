import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class Epic extends Task {
    public List<SubTask> subTaskList = new ArrayList<>();

    public Epic(String name, String discription, int id, List<SubTask> subTaskList, Enum status) {
        this.name = name;
        this.discription = discription;
        this.id = id;
        this.subTaskList = subTaskList;
        this.status = status;

    }

    public static Epic getRandom(int epicId) {
        String name = RandomStringUtils.randomAlphabetic(10);
        String discription = RandomStringUtils.randomAlphabetic(10);
        int id = epicId;
        return new Epic(name, discription, id, null, Status.NEW);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "\n" +
                "name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}' +
                "\n";
    }
}
