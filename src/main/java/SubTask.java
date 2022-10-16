import java.util.HashMap;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String discription, int id, Enum status, int epicId) {
        super(name,discription, id, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String discription, int epicId) {
        super(name,discription);
        this.epicId = epicId;
    }

    public SubTask() {
    }

    public static SubTask create(SubTask subTask, int subTaskId) {
        return new SubTask(subTask.name, subTask.discription, subTaskId, Status.NEW, subTask.epicId);
    }

    //получаем подзадачу по идентификатору
    public SubTask getSubTaskById(HashMap<Integer,SubTask> subTaskHashMap, int taskId) {

        for (int i : subTaskHashMap.keySet()) {
            if (i == taskId) {
                return subTaskHashMap.get(i);
            }
        }
        return null;
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

    //для проверки обновления используем этот метод,
    //задаем новые параметры для задачи
    public int setEpicId(int epicId) {
        return epicId;
    }
}