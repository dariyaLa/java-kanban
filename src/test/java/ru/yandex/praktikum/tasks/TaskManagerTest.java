package ru.yandex.praktikum.tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.taskManager.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {

    Task expectedTask = new Task("Задача 1", "Описание задачи 1", 4, Status.NEW,
            LocalDateTime.of(LocalDate.of(2022, 2, 20), LocalTime.of(10, 0)), Duration.ofMinutes(60));

    SubTask expectedSubTaskOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 2, Status.NEW, 1,
            LocalDateTime.of(LocalDate.of(2022, 2, 20),
                    LocalTime.of(11, 0)), Duration.ofMinutes(60));
    SubTask expectedSubTaskTwo = new SubTask("Подзадача 2", "Подзадача 2 Описание", 4, Status.NEW, 1,
            LocalDateTime.of(LocalDate.of(2022, 2, 20),
                    LocalTime.of(10, 0)), Duration.ofMinutes(60));

    Epic expectedEpic = new Epic("Эпик 1", "Эпик 1 Описание", 1, addSubTaskEpic(), Status.NEW,
            LocalDateTime.of(LocalDate.of(2022, 2, 20),
                    LocalTime.of(10, 0)), Duration.ofMinutes(60));

    NotFoundExeption exeption = new NotFoundExeption();
    final String EXPECTED_NOT_FOUND_EXEPTION = "Сущность с таким id не найдена, не создана или удалена";
    final LocalDateTime EXPECTED_END_TIME_EPIC = LocalDateTime.of(LocalDate.of(2022, 2, 20),
            LocalTime.of(12, 0));
    final LocalDateTime EXPECTED_END_TIME_SUBTASK = LocalDateTime.of(LocalDate.of(2022, 2, 20),
            LocalTime.of(12, 0));


    public List<SubTask> addSubTaskEpic() {
        List<SubTask> expectedSubTaskList = new ArrayList<>();
        expectedSubTaskList.add(expectedSubTaskOne);
        expectedSubTaskList.add(expectedSubTaskTwo);
        return expectedSubTaskList;
    }

    public abstract TaskManager getManager() throws IOException, NotFoundExeption;

    //успешное создание задачи
    @Test
    protected void createTaskTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        int INDEX_TASK = 0;
        Task task = manager.getTaskById(manager.getTaskHashMap().get(INDEX_TASK).getId());
        List<Task> tasks = manager.getTaskHashMap();


        assertNotNull(task, "Задача не найдена.");
        assertEquals(expectedTask, task, "Задачи не совпадают.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(expectedTask, tasks.get(0), "Задачи не совпадают.");
        assertEquals(expectedTask.getName(), tasks.get(0).getName(), "Неверное наименование задачи");
    }

    //успешное создание эпика
    @Test
    protected void createEpicTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        int INDEX_TASK = 0;
        Epic epic = manager.getEpicHashMap().get(INDEX_TASK);
        List<Epic> epicList = manager.getEpicHashMap();

        assertNotNull(epic, "Эпик не найден.");
        assertEquals(expectedEpic, epic, "Эпики не совпадают.");
        assertNotNull(epicList, "Эпики не возвращаются.");
        assertEquals(1, epicList.size(), "Неверное количество эпиков.");
        assertEquals(expectedEpic, epicList.get(INDEX_TASK), "Задачи не совпадают.");
        assertEquals(expectedEpic.getName(), epicList.get(INDEX_TASK).getName(), "Неверное наименование эпика");
        assertEquals(expectedEpic.getStatus(), epicList.get(INDEX_TASK).getStatus(), "Статус не равен NEW");
    }

    //пустой или несуществующий идентификатор эпика
/*    @Test
    protected void createEpicEmptyTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManagerEmpty();
        final int INDEX_TASK = 0;
        int idEpic = 0;

        final NotFoundExeption exception = assertThrows(
                // класс ошибки
                NotFoundExeption.class,
                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() throws NotFoundExeption {
                        // здесь блок кода, который хотим проверить
                        int idEpic = manager.getEpicHashMap().get(INDEX_TASK).getId();
                    }
                });

        assertEquals(EXPECTED_NOT_FOUND_EXEPTION, exception.getMessage(), "Найдена задача с таким идентификатором");
    }*/

    //успешное создание подзадачи
    @Test
    protected void createSubTaskTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        final int INDEX_TASK = 0;
        final SubTask subTask = manager.getSubTaskHashMap().get(INDEX_TASK);
        final List<SubTask> subTaskList = manager.getSubTaskHashMap();

        assertNotNull(subTask, "Подзадача не найдена.");
        assertEquals(expectedSubTaskOne, subTask, "Подзадачи не совпадают.");
        assertNotNull(subTaskList, "Подзадачи не возвращаются.");
        assertEquals(2, subTaskList.size(), "Неверное количество подзадач.");
        assertEquals(expectedSubTaskOne.getName(), subTaskList.get(INDEX_TASK).getName(), "Неверное наименование подзадачи");
        assertEquals(expectedSubTaskOne.getEpicId(), subTaskList.get(INDEX_TASK).getEpicId(), "Идентификатор эпика в подзадаче либо некорректный либо отсутствует");
    }


    //удаление эпика по id
    @Test
    protected void removeEpicId() throws NotFoundExeption, IOException {
        final int EPIC_ID = 1;
        TaskManager manager = getManager();
        manager.removeEpicId(EPIC_ID);
        assertNull(manager.getEpicById(EPIC_ID), "Найдена задача с таким идентификатором, задача не удалена");
    }

    //удаление подзадачи по id
    @Test
    protected void removeSubTaskId() throws NotFoundExeption, IOException {
        final int SUBTASK_ID = 2;
        final int EPIC_ID = 1;
        List<SubTask> expectedListSubtask;
        TaskManager manager = getManager();
        manager.removeSubTaskId(SUBTASK_ID);

        NotFoundExeption exception = assertThrows(NotFoundExeption.class, () -> {
            manager.getTaskById(SUBTASK_ID);
        });
        assertEquals(EXPECTED_NOT_FOUND_EXEPTION, exception.getMessage(), "Подзадача не удалена, найдена в списке");
        //ищем подзадачу в эпике, в лист складываем задачи, которые совпали по идентификатору,
        //если лист пуст, значит подпзада отстусвует в списке, значит удалена
        expectedListSubtask = manager.getSubTaskEpic(EPIC_ID).stream()
                .filter(p -> p.getId() == SUBTASK_ID)
                .collect(Collectors.toList());
        assertTrue(expectedListSubtask.isEmpty(), "Подзадача в эпике не удалена, найдена в эпике");
    }

    //удаление задачи по id
    @Test
    protected void removeTaskIdTest() throws NotFoundExeption, IOException {
        final int TASK_ID = 4;
        TaskManager manager = getManager();
        NotFoundExeption exception = assertThrows(NotFoundExeption.class, () -> {
            manager.removeTaskId(TASK_ID);
        manager.getTaskById(TASK_ID);
        });
        assertEquals("Сущность с таким id не найдена, не создана или удалена", exception.getMessage(), "Найдена задача с таким идентификатором, задача не удалена");

    }

    //удаление эпика по id, id не существует
    @Test
    protected void removeEpicFictiveIdTest() throws NotFoundExeption, IOException {
        final int EPIC_ID = 8;
        TaskManager manager = getManager();

        final NotFoundExeption exception = assertThrows(
                // класс ошибки
                NotFoundExeption.class,
                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() throws NotFoundExeption {
                        // здесь блок кода, который хотим проверить
                        manager.removeEpicId(EPIC_ID);
                    }
                });

        assertEquals("Сущность с таким id не найдена, не создана или удалена", exception.getMessage(), "Найден эпик с таким идентификатором");
    }

    @Test
    public void removeTasks() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        manager.removeTasks();
        assertEquals(0, manager.getTaskHashMap().size(), "Список задач не пуст");
    }

    @Test
    public void removeAlLSubTasksTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        manager.removeAlLSubTasks();
        assertEquals(0, manager.getSubTaskHashMap().size(), "Список подзадач не пуст");
    }

    @Test
    public void removeAllEpicsTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        manager.removeAllEpics();
        assertEquals(0, manager.getEpicHashMap().size(), "Список эпиков не пуст");
    }

    //список подзадач эпика
    @Test
    public void getSubTaskEpicTest() throws NotFoundExeption, IOException {
        final int EPIC_ID = 1;
        TaskManager manager = getManager();
        manager.getSubTaskEpic(EPIC_ID);
        assertEquals(2, manager.getEpicById(EPIC_ID).getSubTaskList().size(), "Количество подзада эпика некорректное");
    }

    //расчет статуса эпика
    @Test
    protected void checkStatusEpicTest() throws NotFoundExeption, IOException {
        final int EPIC_ID = 1;
        final int INDEX_SUBTASK1 = 0;
        TaskManager manager = getManager();
        Epic actualEpic = manager.getEpicByIdClient(EPIC_ID);
        assertEquals(Status.NEW, manager.checkStatusEpic(actualEpic.getSubTaskList()), "Статус эпика не равен NEW");
        actualEpic.getSubTaskList().get(INDEX_SUBTASK1).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.checkStatusEpic(actualEpic.getSubTaskList()), "Статус эпика не равен IN_PROGRESS");
        actualEpic.getSubTaskList().stream().forEach(p -> p.setStatus(Status.DONE));
        assertEquals(Status.DONE, manager.checkStatusEpic(actualEpic.getSubTaskList()), "Статус эпика не равен DONE");

    }

    //проверка истории
    @Test
    public void getInMemoryHistoryManagerTest() throws NotFoundExeption, IOException {
        final int EPIC_ID = 1;
        TaskManager manager = getManager();
        manager.getEpicByIdClient(EPIC_ID);
        assertEquals(1, manager.getInMemoryHistoryManager().size(), "История пустая");

    }

    //проверка завершения времени
    @Test
    public void getEndTime() throws NotFoundExeption, IOException {
        final int EPIC_ID = 1;
        final int SUBTASK_ID = 2;
        TaskManager manager = getManager();
        assertEquals(EXPECTED_END_TIME_EPIC, manager.getEndTime(manager.getEpicById(EPIC_ID)), "Время завершения эпика некорректное");
        assertEquals(EXPECTED_END_TIME_SUBTASK, manager.getEndTime(manager.getSubTaskById(SUBTASK_ID)), "Время завершения задачи некорректное");
    }

    @Test
    public void updateSubTaskTest() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        final int SUBTASK_ID = 3;
        final int EPIC_ID = 1;
        SubTask subTaskForUpdate = manager.getSubTaskById(SUBTASK_ID);
        SubTask subTaskNew = new SubTask(subTaskForUpdate.getName(),subTaskForUpdate.getDiscription(),3,Status.IN_PROGRESS,1,subTaskForUpdate.getStartTime(),subTaskForUpdate.getDuration());
        //проверяем статус задачи до обновления
        assertEquals(Status.NEW, manager.getSubTaskById(SUBTASK_ID).getStatus(), "Статус задачи не равен NEW");
        manager.updateSubTask(subTaskNew);
        assertEquals(Status.IN_PROGRESS, manager.getSubTaskById(SUBTASK_ID).getStatus(), "Статус задачи не равен IN_PROGRESS");
        assertEquals(Status.IN_PROGRESS, manager.checkStatusEpic(manager.getEpicById(EPIC_ID).getSubTaskList()), "Статус эпика не равен IN_PROGRESS");
    }

    @Test
    public void getTaskTreeSetPrioritized() throws NotFoundExeption, IOException {
        TaskManager manager = getManager();
        SubTask subTaskNotStartTimeEpicOne = new SubTask("Подзадача 5", "Подзадача 5 Описание", 1,
                null, Duration.ofMinutes(60));
        manager.createSubTask(subTaskNotStartTimeEpicOne);
        TreeSet<Task> prioritetList = manager.getTaskTreeSetPrioritized();
        assertEquals(subTaskNotStartTimeEpicOne.getName(),prioritetList.last().getName(), "Задача без старта не в конце списка");
        assertEquals(manager.START_TIME_NOT_SET,prioritetList.last().getStartTime(), "Старт времени задачи не равен null");
        assertEquals(expectedSubTaskTwo.getName(),prioritetList.first().getName(), "На первом месте в списке задача с самым ранним стартом");

    }




}
