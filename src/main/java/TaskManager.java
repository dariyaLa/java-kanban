import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void createTask(Task task);

    void removeTasks();

    void removeAlLSubTasks();

    void removeAllEpics();

    void removeEpicId(int taskId);

    //чистим хешмапу сабтасков от удаленных сабтасков
    void removeSubTasksHashMap(List<SubTask> subTaskList);

    //обновляем лист подзадач в эпике
    HashMap<Integer, Epic> updateListSubTaskEpic(SubTask subTask);

    //возвращаем список подзадач эпика
    static List<SubTask> getSubTaskEpic(List<Epic> epicList, int epicId) {
        {
            for (Epic j : epicList) {
                if (j.getId() == epicId) {
                    return j.getSubTaskList();
                }
            }
            return null;
        }
    }

    //удаление подзадачи в листе подзадач эпика
    HashMap<Integer, Epic> removeTaskSubTaskListEpic(int taskId);

    //удаление подзадачи
    void removeSubTaskId(int taskId);

    //удаление задачи
    void removeTaskId(int taskId);

    void updateSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    //рассчитываем статус эпика в зависимости от статуса задач
    static Status checkStatusEpic(List<SubTask> subTaskList) {
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
    SubTask getSubTaskById(int taskId);

    //получаем задачу по идентификатору
    Task getTaskById(int taskId);

    //получаем эпик по идентификатору
    Epic getEpicById(List<Epic> epicList, int taskId);

    List<Epic> getEpicHashMap();

    List<SubTask> getSubTaskHashMap();

    List<Task> getTaskHashMap();
}