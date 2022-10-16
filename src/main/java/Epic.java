import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTaskList = new ArrayList<>();

    public Epic(String name, String discription, int id, List<SubTask> subTaskList, Enum status) {
       super(name,discription,id,status);
        this.subTaskList = subTaskList;
    }

    public Epic(String name, String discription) {
        super(name,discription);
    }

    public Epic() {
    }

    public static Epic create(int epicId, Epic epic) {
        return new Epic(epic.name, epic.discription, epicId, new ArrayList<>(), Status.NEW);
    }

    //рассчитываем статус эпика в зависимости от статуса задач
    public static Status checkStatusEpic(List<SubTask> subTaskList) {
        int countSubTaskDoneEpic = 0;

        for (int i = 0; i < subTaskList.size(); i++) {
            if (subTaskList.get(i).status == Status.IN_PROGRESS) {
                return Status.IN_PROGRESS;
            } else if (subTaskList.get(i).status == Status.DONE) {
                countSubTaskDoneEpic++;
            }
        }

        if (countSubTaskDoneEpic == subTaskList.size()) {
            return Status.DONE;
        } else {
            return Status.NEW;
        }
    }

    //обновляем лист подзадач в эпике
    public static HashMap<Integer, Epic> updateListSubTaskEpic(HashMap<Integer, Epic> epicHashMap, SubTask subTask) {
        List<SubTask> subTaskListTemp = epicHashMap.get(subTask.getEpicId()).getSubTaskList();
        for (int j : epicHashMap.keySet()) {
            if (epicHashMap.get(j).id == subTask.getEpicId()) {
                subTaskListTemp = epicHashMap.get(j).getSubTaskList();
            }

            for (int i = 0; i < subTaskListTemp.size(); i++) {
                if (subTaskListTemp.get(i).id == subTask.id) {
                    subTaskListTemp.get(i).setStatus(subTask.getStatus());
                    subTaskListTemp.get(i).setName(subTask.getName());
                    subTaskListTemp.get(i).setDiscription(subTask.getDiscription());
                }
            }
            Epic epicTemp = new Epic(epicHashMap.get(subTask.getEpicId()).name,
                    epicHashMap.get(subTask.getEpicId()).discription, epicHashMap.get(subTask.getEpicId()).id,
                    subTaskListTemp, Epic.checkStatusEpic(subTaskListTemp));

            epicHashMap.put(epicHashMap.get(subTask.getEpicId()).id, epicTemp);

        }
        return epicHashMap;
    }

    //возвращаем список подзадач эпика
    public static List<SubTask> getSubTaskEpic (List<Epic> epicList, int epicId) {
        for (Epic j : epicList) {
            if (j.getId() == epicId) {
                return j.getSubTaskList();
            }
        }
        return null;
    }

    //удаление подзадачи в листе подзадач эпика
    public static HashMap<Integer, Epic> removeTaskSubTaskListEpic(HashMap<Integer, Epic> epicHashMap, HashMap<Integer, SubTask> subTaskHashMap, int taskId) {
        int epicId = 0;
        for (int j : subTaskHashMap.keySet()) {
            if (subTaskHashMap.get(j).id == taskId) {
                epicId = subTaskHashMap.get(j).getEpicId();
                break;
            }
        }

        for (int i = 0; i < epicHashMap.get(epicId).getSubTaskList().size(); i++) {
            if (epicHashMap.get(epicId).getSubTaskList().get(i).id == taskId) {
                epicHashMap.get(epicId).getSubTaskList().remove(i);
            }
        }
        return epicHashMap;
    }

    //получаем эпик по идентификатору
    public static Epic getEpicById(HashMap<Integer, Epic> epicHashMap, int taskId) {

        for (int i : epicHashMap.keySet()) {
            if (i == taskId) {
                return epicHashMap.get(i);
            }
        }
        return null;
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