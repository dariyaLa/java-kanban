import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manage {

    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();

    private int taskId = 0;

    public void createEpic(Epic epic) {
        if (!epicHashMap.isEmpty()) {
            for (int i : epicHashMap.keySet()) {
                if (epicHashMap.get(i).getName() == epic.getName()) {
                    System.out.println("Эпик с таким наименованием уже существует");
                    break;
                } else {
                    this.epicHashMap.put(++taskId, Epic.create(this.taskId, epic));
                    break;
                }
            }
        } else {
            this.epicHashMap.put(++taskId, Epic.create(this.taskId, epic));
        }
    }

    public void createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        for (int j : epicHashMap.keySet()) {
            if (epicHashMap.get(j).getId() == epicId) {
                subTask = SubTask.create(subTask, ++taskId);
                subTaskHashMap.put(taskId, subTask);
                epicHashMap.get(subTask).getSubTaskList().add(subTask);
            }
        }
    }

    public void createTask(Task task) {
        if (!taskHashMap.isEmpty()) {
            for (int i : taskHashMap.keySet()) {
                if (taskHashMap.get(i).getName() == task.getName()) {
                    System.out.println("Задача с таким наименованием уже существует");
                    break;
                } else {
                    this.taskHashMap.put(++taskId, Task.create(this.taskId, task));
                    break;
                }
            }
        } else {
            this.taskHashMap.put(++taskId, Task.create(this.taskId, task));
        }
    }

    public void removeTasks() {
        taskHashMap.clear();
    }

    public void removeAllEpicsAndSubTasks() {
        if (!epicHashMap.isEmpty()) {
            epicHashMap.clear();
        }
        if (!subTaskHashMap.isEmpty()) {
            subTaskHashMap.clear();
        }
    }

    public void removeEpicId(int taskId) {
        if (epicHashMap.isEmpty()) {
            System.out.println("Эпик не создан или удален");
        } else {
            if (!epicHashMap.get(taskId).getSubTaskList().isEmpty()) {
                removeSubTasksHashMap(epicHashMap.get(taskId).getSubTaskList());
            }
            epicHashMap.remove(taskId);
        }
    }

    //чистим хешмапу сабтасков от удаленных сабтасков
    public void removeSubTasksHashMap(List<SubTask> subTaskList) {
        for (int i = 0; i < subTaskList.size(); i++) {
            for (int j : subTaskHashMap.keySet()) {
                if (subTaskHashMap.get(j).getId() == subTaskList.get(i).id) {
                    subTaskHashMap.remove(j);
                    break;
                }
            }
        }
    }

    //удаление подзадачи
    public void removeSubTaskId(int taskId) {
        if (subTaskHashMap.isEmpty()) {
            System.out.println("Подзадача не создана или удалена");
        } else {
            //удаление подзадачи из листа эпика
            Epic.removeTaskSubTaskListEpic(epicHashMap, subTaskHashMap, taskId);
            subTaskHashMap.remove(taskId);
            System.out.println("Подзадача с id " + taskId + " удалена");
        }
    }

    //удаление задачи
    public void removeTaskId(int taskId) {
        if (taskHashMap.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            taskHashMap.remove(taskId);
        }
    }

    public void updateSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        //обновляем лист подзадач в эпике
        Epic.updateListSubTaskEpic(epicHashMap, subTask);
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);

    }

    public List<Epic> getEpicHashMap() {
        ArrayList<Epic> taskArrayList = new ArrayList<>(epicHashMap.values());
        return taskArrayList;
    }

    public List<SubTask> getSubTaskHashMap() {
        ArrayList<SubTask> taskArrayList = new ArrayList<>(subTaskHashMap.values());
        return taskArrayList;
    }

    public List<Task> getTaskHashMap() {
        ArrayList<Task> taskArrayList = new ArrayList<>(taskHashMap.values());
        return taskArrayList;
    }
}