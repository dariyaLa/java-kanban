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
                    this.epicHashMap.put(++taskId, Epic.createEpic(this.taskId, epic));
                    break;
                }
            }
        } else {
            this.epicHashMap.put(++taskId, Epic.createEpic(this.taskId, epic));
        }
    }

    public void createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        for (int j : epicHashMap.keySet()) {
            if (epicHashMap.get(j).getId() == epicId) {
                subTask = SubTask.createSubTask(subTask, ++taskId);
                subTaskHashMap.put(taskId, subTask);
                epicHashMap.get(j).getSubTaskList().add(subTask);
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
                    this.taskHashMap.put(++taskId, Task.createTask(this.taskId, task));
                    break;
                }
            }
        } else {
            this.taskHashMap.put(++taskId, Task.createTask(this.taskId, task));
        }
    }

    public void removeTask() {
        taskHashMap.clear();
    }

    public void removeEpic() {
        epicHashMap.clear();
    }

    public void removeSubTask() {
        subTaskHashMap.clear();
    }

    public Task getTaskById(int taskId) {
        boolean isFound = false;

        for (int i : taskHashMap.keySet()) {
            if (i == taskId) {
                isFound = true;
                return taskHashMap.get(i);
            }
        }

        if (!isFound) {
            System.out.println("Задач с таким id не найдено");
            return null;
        }
        return null;
    }

    public Epic getEpicById(int taskId) {
        boolean isFound = false;

        for (int i : epicHashMap.keySet()) {
            if (i == taskId) {
                isFound = true;
                return epicHashMap.get(i);
            }
        }

        if (!isFound) {
            System.out.println("Задач с таким id не найдено");
            return null;
        }
        return null;
    }

    public SubTask getSubTaskById(int taskId) {
        boolean isFound = false;

        for (int i : subTaskHashMap.keySet()) {
            if (i == taskId) {
                isFound = true;
                return subTaskHashMap.get(i);
            }
        }

        if (!isFound) {
            System.out.println("Задач с таким id не найдено");
            return null;
        }
        return null;
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

    public void removeEpicId(int taskId) {
        if (epicHashMap.isEmpty()) {
            System.out.println("Эпик не создан или удален");
        } else {
            for (int i : epicHashMap.keySet()) {
                if (i == taskId) {
                    if (!epicHashMap.get(i).getSubTaskList().isEmpty()) {
                        System.out.println("Задача с таким id является Эпиком с непустым списком подзадач. Сначала необходимо удалить подзадачи эпика");
                        break;
                    } else {
                        epicHashMap.remove(i);
                        System.out.println("Эпик с id " + taskId + "удален");
                    }
                }
            }
        }
    }

    public void removeSubTaskId(int taskId) {
        if (subTaskHashMap.isEmpty()) {
            System.out.println("Подзадача не создана или удалена");
        } else {
            for (int i : subTaskHashMap.keySet()) {
                if (i == taskId) {
                    //удаление подзадачи из листа эпика
                    removeTaskSubTaskListEpic(taskId);
                    subTaskHashMap.remove(i);
                    System.out.println("Подзадача с id " + taskId + " удалена");
                    break;
                }
            }
        }
    }

    public void removeTaskId(int taskId) {
        if (taskHashMap.isEmpty()) {
            System.out.println("Задача не создана или удалена");
        } else {
            for (int i : taskHashMap.keySet()) {
                if (i == taskId) {
                    //удаление подзадачи из листа эпика
                    taskHashMap.remove(i);
                    System.out.println("Задача с id " + taskId + " удалена");
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

    public void updateSubTask(SubTask subTask) {
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

    public void updateTask(Task task) {
        for (int i : taskHashMap.keySet()) {
            if (task.getId() == taskHashMap.get(i).getId()) {
                taskHashMap.put(taskHashMap.get(i).getId(), new Task(task.getName(), task.getDiscription(),
                        taskHashMap.get(i).getId(), task.getStatus()));
                break;
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (int i : epicHashMap.keySet()) {
            if (epic.getId() == epicHashMap.get(i).getId()) {
                epicHashMap.put(epicHashMap.get(i).getId(), new Epic(epic.getName(), epic.getDiscription(),
                        epicHashMap.get(i).getId(), epic.getSubTaskList(), epic.getStatus()));
                break;
            }
        }
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

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public void setEpicHashMap(HashMap<Integer, Epic> epicHashMap) {
        this.epicHashMap = epicHashMap;
    }

    public HashMap<Integer, SubTask> getSubTaskHashMap() {
        return subTaskHashMap;
    }

    public void setSubTaskHashMap(HashMap<Integer, SubTask> subTaskHashMap) {
        this.subTaskHashMap = subTaskHashMap;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public void setTaskHashMap(HashMap<Integer, Task> taskHashMap) {
        this.taskHashMap = taskHashMap;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
    
    







