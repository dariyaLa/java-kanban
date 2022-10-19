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
        if (epicHashMap.get(epicId).getId() == epicId) {
            subTask = SubTask.create(subTask, ++taskId);
            subTaskHashMap.put(taskId, subTask);
            epicHashMap.get(epicId).getSubTaskList().add(subTask);
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

    public void removeAlLSubTasks() {
        if (!subTaskHashMap.isEmpty()) {
            subTaskHashMap.clear();
        }
    }

    public void removeAllEpics() {
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

    //обновляем лист подзадач в эпике
    public HashMap<Integer, Epic> updateListSubTaskEpic(SubTask subTask) {
        List<SubTask> subTaskListTemp = new ArrayList<>();
        subTaskListTemp = epicHashMap.get(subTask.getEpicId()).getSubTaskList();

        if (!subTaskListTemp.isEmpty()) {
            for (int i = 0; i < subTaskListTemp.size(); i++) {
                if (subTaskListTemp.get(i).id == subTask.id) {
                    subTaskListTemp.get(i).setStatus(subTask.getStatus());
                    subTaskListTemp.get(i).setName(subTask.getName());
                    subTaskListTemp.get(i).setDiscription(subTask.getDiscription());
                }
            }
            Epic epicTemp = new Epic(epicHashMap.get(subTask.getEpicId()).name,
                    epicHashMap.get(subTask.getEpicId()).discription, epicHashMap.get(subTask.getEpicId()).id,
                    subTaskListTemp, Manage.checkStatusEpic(subTaskListTemp));

            epicHashMap.put(epicHashMap.get(subTask.getEpicId()).id, epicTemp);
        }
        return epicHashMap;
    }

    //возвращаем список подзадач эпика
    public static List<SubTask> getSubTaskEpic(List<Epic> epicList, int epicId) {
        for (Epic j : epicList) {
            if (j.getId() == epicId) {
                return j.getSubTaskList();
            }
        }
        return null;
    }

    //удаление подзадачи в листе подзадач эпика
    public HashMap<Integer, Epic> removeTaskSubTaskListEpic(int taskId) {
        int epicId = subTaskHashMap.get(taskId).getEpicId();

        for (int i = 0; i < epicHashMap.get(epicId).getSubTaskList().size(); i++) {
            if (epicHashMap.get(epicId).getSubTaskList().get(i).id == taskId) {
                epicHashMap.get(epicId).getSubTaskList().remove(i);
            }
        }
        return epicHashMap;
    }

    //удаление подзадачи
    public void removeSubTaskId(int taskId) {
        if (subTaskHashMap.isEmpty()) {
            System.out.println("Подзадача не создана или удалена");
        } else {
            //удаление подзадачи из листа эпика
            removeTaskSubTaskListEpic(taskId);
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
        updateListSubTaskEpic(subTask);
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);

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

    //получаем подзадачу по идентификатору
    public SubTask getSubTaskById(int taskId) {
        return subTaskHashMap.get(taskId);
    }

    //получаем задачу по идентификатору
    public Task getTaskById(int taskId) {
        return taskHashMap.get(taskId);
    }

    //получаем эпик по идентификатору
    public Epic getEpicById(List<Epic> epicList, int taskId) {
        for (Epic i : epicList) {
            if (i.getId() == taskId) {
                return i;
            }
        }
        return null;
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