import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manage {

    @Getter
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    @Getter
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();

    private int taskId = 0;

    public void createEpic(Epic epic) {
        this.epicHashMap.put(++taskId, Epic.createEpic(this.taskId, epic));
    }

    public void createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        for (int j : epicHashMap.keySet()) {
            if (epicHashMap.get(j).id == epicId) {
                subTask = SubTask.createSubTask(subTask, ++taskId);
                subTaskHashMap.put(taskId, subTask);
                epicHashMap.get(j).getSubTaskList().add(subTask);
            }
        }
    }

    public void removeAllTask() {
        epicHashMap.clear();
        subTaskHashMap.clear();
        taskHashMap.clear();
        System.out.println("Все задачи удалены");
    }

    public Object getTaskWithId(int taskId) {
        Object object = new Object();
        boolean isFound = false;

        for (int j : epicHashMap.keySet()) {
            if (j == taskId) {
                object = epicHashMap.get(j);
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            for (int j : subTaskHashMap.keySet()) {
                if (j == taskId) {
                    System.out.println(subTaskHashMap.get(j));
                    object = epicHashMap.get(j);
                    isFound = true;
                    break;
                }
            }
        }

        if (!isFound) {
            System.out.println("Задач с таким id не найдено");
        }
        return object;
    }

    public List<SubTask> getSubTaskEpic(int epicId) {
        boolean isEpic = false;
        List<SubTask> subTaskListTemp = new ArrayList<>();
        for (int j : epicHashMap.keySet()) {
            if (j == epicId) {
                subTaskListTemp = epicHashMap.get(j).getSubTaskList();
                isEpic = true;
                break;
            }
        }
        if (!isEpic) {
            System.out.println("Эпика с таким идентификатором не найдено");
        }
        return subTaskListTemp;
    }

    public void removeTaskId(int taskId) {
        if (epicHashMap.isEmpty()) {
            System.out.println("Эпики не созданы или удалены");
        } else {
            for (int j : epicHashMap.keySet()) {
                if (j == taskId) {
                    if (!epicHashMap.get(j).getSubTaskList().isEmpty()) {
                        System.out.println("Задача с таким id является Эпиком с непустым списком подзадач. Сначала необходимо удалить подзадачи эпика");
                        break;
                    } else {
                        epicHashMap.remove(j);
                        System.out.println("Эпик с id " + taskId + "удален");
                    }
                }
            }

            for (int j : subTaskHashMap.keySet()) {
                if (j == taskId) {
                    //удаление подзадачи из листа эпика
                    removeTaskSubTaskListEpic(taskId);
                    subTaskHashMap.remove(j);
                    System.out.println("Подзадача с id " + taskId + " удалена");
                    break;
                }
            }
        }
    }

    //удаление подзадачи в листе подзадач эпика
    public void removeTaskSubTaskListEpic(int taskId) {
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
    }

    public void updateTask(SubTask subTask) {
        for (int j : subTaskHashMap.keySet()) {
            if (subTask.id == subTaskHashMap.get(j).id) {
                subTaskHashMap.put(subTaskHashMap.get(j).id, new SubTask(subTask.name, subTask.discription, subTask.getEpicId(),
                        subTaskHashMap.get(j).id, subTask.getStatus()));
                break;
            }
        }
        //обновляем лист подзадач в эпике
        updateListSubTaskEpic(subTask);
    }

    public void updateListSubTaskEpic(SubTask subTask) {
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
            epicHashMap.put(epicHashMap.get(subTask.getEpicId()).id, new Epic(epicHashMap.get(subTask.getEpicId()).name,
                    epicHashMap.get(subTask.getEpicId()).discription, epicHashMap.get(subTask.getEpicId()).id,
                    subTaskListTemp, checkStatusEpic(subTaskListTemp)));

        }
    }

    //рассчитываем статус эпика
    public Status checkStatusEpic(List<SubTask> subTaskList) {
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

}
    
    







