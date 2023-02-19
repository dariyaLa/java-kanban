package ru.yandex.praktikum.taskManager;

import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    static TaskComparator taskComparator = new TaskComparator();
    private static TreeSet<Task> taskTreeSetPrioritized = new TreeSet<>(taskComparator);

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
    public SubTask createSubTask(SubTask subTask) throws NotFoundExeption {
        validateStartTimeSubTask(subTask);
        int epicId = subTask.getEpicId();
        if (epicHashMap.get(epicId).getId() == epicId) {
            subTask = SubTask.create(subTask, ++taskId);
            subTaskHashMap.put(taskId, subTask);
            epicHashMap.get(epicId).getSubTaskList().add(subTask);
        }
        prioritizedTasks(subTask);
        return subTask;
    }

    @Override
    public Task createTask(Task task) throws NotFoundExeption {
        validateStartTimeTask(task);
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
        prioritizedTasks(task); //добавляем к приоритезированному списку
        return task;
    }

    @Override
    public void removeTasks() {
        if (!taskHashMap.isEmpty()) {
            for (Map.Entry<Integer, Task> task : taskHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.removeTask(task.getKey());
            }
            taskHashMap.clear();
        }
    }

    @Override
    public void removeAlLSubTasks() {
        if (!subTaskHashMap.isEmpty()) {
            for (Map.Entry<Integer, SubTask> task : subTaskHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.removeTask(task.getKey());
            }
            subTaskHashMap.clear();
        }
    }

    @Override
    public void removeAllEpics() {
        if (!epicHashMap.isEmpty()) {
            for (Map.Entry<Integer, Epic> task : epicHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.removeTask(task.getKey());
            }
            epicHashMap.clear();
        }
        if (!subTaskHashMap.isEmpty()) {
            for (Map.Entry<Integer, SubTask> task : subTaskHashMap.entrySet()) { //удаляем из истории просмотров
                inMemoryHistoryManager.removeTask(task.getKey());
            }
            subTaskHashMap.clear();
        }
    }

    @Override
    public void removeEpicId(int taskId) throws NotFoundExeption {
        if (epicHashMap.isEmpty() || epicHashMap.get(taskId) == null) {
            //System.out.println("Эпик не создан или удален");
            throw new NotFoundExeption();
        } else {
            if (!epicHashMap.get(taskId).getSubTaskList().isEmpty()) {
                removeFromHistoryTasks(epicHashMap.get(taskId).getSubTaskList()); //удаляем из истории просмотр сабтасков
                removeSubTasksHashMap(epicHashMap.get(taskId).getSubTaskList());
            }
            inMemoryHistoryManager.removeTask(taskId);
            epicHashMap.remove(taskId);
        }
    }

    //подчищаем из истории просмотр сабтасков после удаления эпика
    public void removeFromHistoryTasks(List<SubTask> listTasks) {
        for (Task task : listTasks) {
            inMemoryHistoryManager.removeTask(task.getId());
        }
    }

    //чистим хешмапу сабтасков от удаленных сабтасков
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
                    subTaskListTemp, checkStatusEpic(subTaskListTemp), epicHashMap.get(subTask.getEpicId()).getStartTime(),
                    epicHashMap.get(subTask.getEpicId()).getDuration());

            epicHashMap.put(epicHashMap.get(subTask.getEpicId()).getId(), epicTemp);
        }
        return epicHashMap;
    }

    //возвращаем список подзадач эпика
    @Override
    public List<SubTask> getSubTaskEpic(int epicId) {
        for (Epic j : getEpicHashMap()) {
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
            inMemoryHistoryManager.removeTask(taskId);//удаление из истории просмотров
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
            inMemoryHistoryManager.removeTask(taskId);
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
        int countDone = 0;
        int countNew = 0;

        if (subTaskList.size() > 0) {
            for (int i = 0; i < subTaskList.size(); i++) {
                if (subTaskList.get(i).getStatus() == Status.IN_PROGRESS) {
                    return Status.IN_PROGRESS;
                } else if (subTaskList.get(i).getStatus() == Status.DONE) {
                    countDone++;
                } else if (subTaskList.get(i).getStatus() == Status.NEW) {
                    countNew++;
                }
            }
            if (countDone == subTaskList.size()) {
                return Status.DONE;
            } else if (countNew == subTaskList.size()) {
                return Status.NEW;
            } else return Status.IN_PROGRESS;
        } else return Status.NEW;
    }


    //получаем подзадачу по идентификатору
    @Override
    public SubTask getSubTaskById(int taskId) {
        inMemoryHistoryManager.add(subTaskHashMap.get(taskId));
        return subTaskHashMap.get(taskId);
    }

    //получаем задачу по идентификатору
    @Override
    public Task getTaskById(int taskId) throws NotFoundExeption {
        if (taskHashMap.isEmpty() || taskHashMap.get(taskId) == null) {
            //System.out.println("Эпик не создан или удален");
            throw new NotFoundExeption();
        } else {
            inMemoryHistoryManager.add(taskHashMap.get(taskId));
            return taskHashMap.get(taskId);
        }
    }

    //клиет получает эпик по идентификатору
    @Override
    public Epic getEpicByIdClient(int taskId){
        for (Epic i : getEpicHashMap()) {
            if (i.getId() == taskId) {
                inMemoryHistoryManager.add(i);
                return i;
            }
        }
        return null;
    }

    //получаем эпик по идентификатору
    public Epic getEpicById(int taskId) throws NotFoundExeption {
        for (Epic i : getEpicHashMap()) {
            if (i.getId() == taskId) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<Epic> getEpicHashMap(){
        if (epicHashMap.isEmpty()) {
            ArrayList<Epic> taskArrayList = new ArrayList<>();
            return taskArrayList;
        } else {
            ArrayList<Epic> taskArrayList = new ArrayList<>(epicHashMap.values());
            return taskArrayList;
        }
    }

    @Override
    public List<SubTask> getSubTaskHashMap() {
        if (subTaskHashMap.isEmpty()) {
            ArrayList<SubTask> taskArrayList = new ArrayList<>();
            return taskArrayList;
        } else {
            ArrayList<SubTask> taskArrayList = new ArrayList<>(subTaskHashMap.values());
            return taskArrayList;
        }
    }

    @Override
    public List<Task> getTaskHashMap() {
        if (taskHashMap.isEmpty()) {
            ArrayList<Task> taskArrayList = new ArrayList<>();
            return taskArrayList;
        } else {
            ArrayList<Task> taskArrayList = new ArrayList<>(taskHashMap.values());
            return taskArrayList;
        }
    }

    @Override
    public List<Task> getInMemoryHistoryManager() {
        return inMemoryHistoryManager.getHistory();
    }

    //рассчитываем время завершения задачи
    @Override
    public LocalDateTime getEndTime(Task task) {

        if (task instanceof Epic) {
            return calculationStartEndDurationEpic((Epic) task).getEndTime();
        } else {
            return task.getStartTime().plus(task.getDuration());
        }
    }

    public Epic calculationStartEndDurationEpic(Epic epic) {
        List<SubTask> tempList = epic.getSubTaskList();
        SubTask[] tempMasSubtasks = tempList.toArray(new SubTask[0]);

        Arrays.sort(tempMasSubtasks, (first, second) -> {
            if (first.getStartTime().isBefore(second.getStartTime())) {
                return -1;
            } else return 0;
        });
        //устанавливаем время начала эпика из первой задачи отсортированного списка
        epic.setStartTime(tempMasSubtasks[0].getStartTime());
        //устанавливаем время конца эпика из последней задачи отсортированного списка
        epic.setEndTime(getEndTime(tempMasSubtasks[tempMasSubtasks.length - 1]));
        return epic;
    }

    public static void prioritizedTasks(Task task) {
        taskTreeSetPrioritized.add(task);
    }

    public void validateStartTimeSubTask(Task task) {
        if (!getSubTaskHashMap().isEmpty()) {
            List<Task> crossTask = getSubTaskHashMap().stream()
                    .filter(t -> task.getStartTime().isAfter(t.getStartTime())
                            && task.getStartTime().isBefore(getEndTimeSubTaskAndTask(t))
                            || t.getStartTime().isEqual(task.getStartTime()))
                    .collect(Collectors.toList());

            if (!crossTask.isEmpty()) {
                System.out.println("Подзадача \"" + task.getName() +
                        "\" имеет пересечение по времени с подзадачами с ID" + crossTask.stream().map(Task::getId).collect(Collectors.toList()));
            }
        }
    }
    public void validateStartTimeTask(Task task) throws NotFoundExeption {
            if (!getTaskHashMap().isEmpty()) {
                List<Task> crossTask = getTaskHashMap().stream()
                        .filter(t -> task.getStartTime().isAfter(t.getStartTime())
                                && task.getStartTime().isBefore(getEndTimeSubTaskAndTask(t))
                                || t.getStartTime().isEqual(task.getStartTime()))
                        .collect(Collectors.toList());

                if (!crossTask.isEmpty()) {
                    System.out.println("Задача \"" + task.getName() +
                            "\" имеет пересечение по времени с задачами с ID" + crossTask.stream().map(Task::getId).collect(Collectors.toList()));
                }
            }
    }

    public LocalDateTime getEndTimeSubTaskAndTask(Task task) {
        LocalDateTime endTime = task.getStartTime().plusMinutes(task.getDuration().toMinutes());
        return endTime;
    }


    @Override
    public Task foundTask(int id) {
        if (epicHashMap.get(id) != null) {
            return epicHashMap.get(id);
        } else if (subTaskHashMap.get(id) != null) {
            return subTaskHashMap.get(id);
        } else if (taskHashMap.get(id) != null) {
            return taskHashMap.get(id);
        }
        return null;
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

    public TreeSet<Task> getTaskTreeSetPrioritized() {
        return taskTreeSetPrioritized;
    }

    static class TaskComparator implements Comparator<Task> {

        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getStartTime().equals(START_TIME_NOT_SET)) {
                return 1;
            }
            if (t2.getStartTime().equals(START_TIME_NOT_SET)) {
                return -1;
            }
            if (t1.getStartTime() == t2.getStartTime()) {
                return 0;
            }
            if (t1.getStartTime().isAfter(t2.getStartTime())) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}