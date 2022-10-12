import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTaskList = new ArrayList<>();

    public Epic(String name, String discription, int id, List<SubTask> subTaskList, Enum status) {
        this.name = name;
        this.discription = discription;
        this.id = id;
        this.subTaskList = subTaskList;
        this.status = status;
    }

    public Epic(String name, String discription) {
        this.name = name;
        this.discription = discription;
    }

    public static Epic createEpic(int epicId, Epic epic) {
        return new Epic(epic.name, epic.discription, epicId, new ArrayList<>(), Status.NEW);
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

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }
}
