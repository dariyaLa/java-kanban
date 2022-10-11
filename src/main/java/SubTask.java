import lombok.Getter;
import lombok.Setter;

public class SubTask extends Task {

    @Getter
    @Setter
    private int epicId;

    protected SubTask(String name, String discription, int epicId, int id, Enum status) {
        this.name = name;
        this.discription = discription;
        this.epicId = epicId;
        this.id = id;
        this.status = status;
    }

    protected SubTask(String name, String discription, int epicId) {
        this.name = name;
        this.discription = discription;
        this.epicId = epicId;
    }

    protected SubTask() {}

    public static SubTask createSubTask(SubTask subTask, int subTaskId) {
        return new SubTask(subTask.name, subTask.discription, subTask.epicId, subTaskId, Status.NEW);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "\n" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}' +
                "\n";
    }

}
