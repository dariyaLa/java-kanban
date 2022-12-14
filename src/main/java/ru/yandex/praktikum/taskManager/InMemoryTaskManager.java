package ru.yandex.praktikum.taskManager;

import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    private int taskId = 0;

    @Override
    public Epic createEpic(Epic epic) {
        if (!epicHashMap.isEmpty()) {
            for (int i : epicHashMap.keySet()) {
                if (epicHashMap.get(i).getName() == epic.getName()) {
                    System.out.println("Эпик с таким наименованием уже существует");
                    break;
                } else {
                    ++taskId;
                    epic = Epic.create(this.taskId, epic);
                    this.epicHashMap.put(taskId, epic);
                    break;
                }
            }
        } else {
            ++taskId;
            epic = Epic.create(this.taskId, epic);
            this.epicHashMap.put(taskId, epic);
        }
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        if (epicHashMap.get(epicId).getId() == epicId) {
            subTask = SubTask.create(subTask, ++taskId);
            subTaskHashMap.put(taskId, subTask);
            epicHashMap.get(epicId).getSubTaskList().add(subTask);
        }

        return subTask;
    }

    @Override
    public Task createTask(Task task) {
        if (!taskHashMap.isEmpty()) {
            for (int i : taskHashMap.keySet()) {
                if (taskHashMap.get(i).getName() == task.getName()) {
                    System.out.println("Задача с таким наименованием уже существует");
                    break;
                } else {
                    ++taskId;
                    task = Task.create(this.taskId, task);
                    this.taskHashMap.put(taskId, task);
                    break;
                }
            }
        } else {
            ++taskId;
            task = Task.create(this.taskId, task);
            this.taskHashMap.put(taskId, task);
        }
        return task;
    }

    @Override
    public void removeTasks() {
        if (!taskHashMap.isEmpty()) {
            for (Map.Entry<Integer, Task> task : taskHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.remove(task.getKey());
            }
            taskHashMap.clear();
        }
    }

    @Override
    public void removeAlLSubTasks() {
        if (!subTaskHashMap.isEmpty()) {
            for (Map.Entry<Integer, SubTask> task : subTaskHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.remove(task.getKey());
            }
            subTaskHashMap.clear();
        }
    }

    @Override
    public void removeAllEpics() {
        if (!epicHashMap.isEmpty()) {
            for (Map.Entry<Integer, Epic> task : epicHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.remove(task.getKey());
            }
            epicHashMap.clear();
        }
        if (!subTaskHashMap.isEmpty()) {
            for (Map.Entry<Integer, SubTask> task : subTaskHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.remove(task.getKey());
            }
            subTaskHashMap.clear();
        }
    }

    @Override
    public void removeEpicId(int taskId) {
        if (epicHashMap.isEmpty()) {
            System.out.println("Эпик не создан или удален");
        } else {
            if (!epicHashMap.get(taskId).getSubTaskList().isEmpty()) {
                removeFromHistoryTasks(epicHashMap.get(taskId).getSubTaskList()); //удаляем из истории просмотр сабтасков
                removeSubTasksHashMap(epicHashMap.get(taskId).getSubTaskList());
            }
            inMemoryHistoryManager.remove(taskId);
            epicHashMap.remove(taskId);
        }
    }

    //подчищаем из истории просмотр сабтасков после удаления эпика
    public void removeFromHistoryTasks(List<SubTask> listTasks) {
        for (Task task : listTasks) {
            inMemoryHistoryManager.remove(task.getId());
        }
    }

    //чистим хешмапу сабтасков от удаленных сабтасков
    @Override
    public void removeSubTasksHashMap(List<SubTask> subTaskList) {
        for (int i = 0; i < subTaskList.size(); i++) {
            for (int j : subTaskHashMap.keySet()) {
                if (subTaskHashMap.get(j).getId() == subTaskList.get(i).getId()) {
                    subTaskHashMap.remove(j);
                    break;
                }
            }
        }
    }

    //обновляем лист подзадач в эпике
    @Override
    public HashMap<Integer, Epic> updateListSubTaskEpic(SubTask subTask) {
        List<SubTask> subTaskListTemp = epicHashMap.get(subTask.getEpicId()).getSubTaskList();

        if (!subTaskListTemp.isEmpty()) {
            for (int i = 0; i < subTaskListTemp.size(); i++) {
                if (subTaskListTemp.get(i).getId() == subTask.getId()) {
                    subTaskListTemp.get(i).setStatus(subTask.getStatus());
                    subTaskListTemp.get(i).setName(subTask.getName());
                    subTaskListTemp.get(i).setDiscription(subTask.getDiscription());
                }
            }
            Epic epicTemp = new Epic(epicHashMap.get(subTask.getEpicId()).getName(),
                    epicHashMap.get(subTask.getEpicId()).getDiscription(), epicHashMap.get(subTask.getEpicId()).getId(),
                    subTaskListTemp, checkStatusEpic(subTaskListTemp));

            epicHashMap.put(epicHashMap.get(subTask.getEpicId()).getId(), epicTemp);
        }
        return epicHashMap;
    }

    //возвращаем список подзадач эпика
    @Override
    public List<SubTask> getSubTaskEpic(List<Epic> epicList, int epicId) {
        for (Epic j : epicList) {
            if (j.getId() == epicId) {
                return j.getSubTaskList();
            }
        }
        return null;
    }

    //удаление подзадачи в листе подзадач эпика
    @Override
    public HashMap<Integer, Epic> removeTaskSubTaskListEpic(int taskId) {
        int epicId = subTaskHashMap.get(taskId).getEpicId();

        for (int i = 0; i < epicHashMap.get(epicId).getSubTaskList().size(); i++) {
            if (epicHashMap.get(epicId).getSubTaskList().get(i).getId() == taskId) {
                epicHashMap.get(epicId).getSubTaskList().remove(i);
            }
        }
        return epicHashMap;
    }

    //удаление подзадачи
    @Override
    public void removeSubTaskId(int taskId) {
        if (subTaskHashMap.isEmpty()) {
            System.out.println("Подзадача не создана или удалена");
        } else {
            //удаление подзадачи из листа эпика
            inMemoryHistoryManager.remove(taskId);//удаление из истории просмотров
            removeTaskSubTaskListEpic(taskId);
            subTaskHashMap.remove(taskId);
            System.out.println("Подзадача с id " + taskId + " удалена");
        }
    }

    //удаление задачи
    @Override
    public void removeTaskId(int taskId) {
        if (taskHashMap.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            inMemoryHistoryManager.remove(taskId);
            taskHashMap.remove(taskId);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        //обновляем лист подзадач в эпике
        updateListSubTaskEpic(subTask);
    }

    @Override
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);

    }

    //рассчитываем статус эпика в зависимости от статуса задач
    @Override
    public Status checkStatusEpic(List<SubTask> subTaskList) {
        int countSubTaskDoneEpic = 0;

        for (int i = 0; i < subTaskList.size(); i++) {
            if (subTaskList.get(i).getStatus() == Status.IN_PROGRESS) {
                return Status.IN_PROGRESS;
            } else if (subTaskList.get(i).getStatus() == Status.DONE) {
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
    @Override
    public SubTask getSubTaskById(int taskId) {
        inMemoryHistoryManager.add(subTaskHashMap.get(taskId));
        return subTaskHashMap.get(taskId);
    }

    //получаем задачу по идентификатору
    @Override
    public Task getTaskById(int taskId) {
        inMemoryHistoryManager.add(taskHashMap.get(taskId));
        return taskHashMap.get(taskId);
    }

    //получаем эпик по идентификатору
    @Override
    public Epic getEpicById(List<Epic> epicList, int taskId) {
        for (Epic i : epicList) {
            if (i.getId() == taskId) {
                inMemoryHistoryManager.add(i);
                return i;
            }
        }
        return null;
    }

    @Override
    public List<Epic> getEpicHashMap() {
        ArrayList<Epic> taskArrayList = new ArrayList<>(epicHashMap.values());
        return taskArrayList;
    }

    @Override
    public List<SubTask> getSubTaskHashMap() {
        ArrayList<SubTask> taskArrayList = new ArrayList<>(subTaskHashMap.values());
        return taskArrayList;
    }

    @Override
    public List<Task> getTaskHashMap() {
        ArrayList<Task> taskArrayList = new ArrayList<>(taskHashMap.values());
        return taskArrayList;
    }

    @Override
    public List<Task> getInMemoryHistoryManager() {
        return inMemoryHistoryManager.getHistory();
    }

    public HistoryManager getInMemoryHistoryManagerDefault() {
        return inMemoryHistoryManager;
    }

    public HashMap<Integer, Epic> setEpicHashMap() {
        epicHashMap = new HashMap<>();
        return epicHashMap;
    }

    public HashMap<Integer, SubTask> setSubTaskHashMap() {
        subTaskHashMap = new HashMap<>();
        return subTaskHashMap;
    }

    public HashMap<Integer, Task> setTaskHashMap() {
        taskHashMap = new HashMap<>();
        return taskHashMap;
    }
}