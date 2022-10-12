public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String discription, int epicId, int id, Enum status) {
        this.name = name;
        this.discription = discription;
        this.epicId = epicId;
        this.id = id;
        this.status = status;
    }

    public SubTask(String name, String discription, int epicId) {
        this.name = name;
        this.discription = discription;
        this.epicId = epicId;
    }

    public SubTask() {
    }

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

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
