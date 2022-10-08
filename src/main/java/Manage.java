import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Manage {

    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    public List<SubTask> subTaskList;

    private int taskId = 0;

    public void createEpicRandom() {
        int сountEpic = 1; //Task.randomCountTask();
        for (int i = сountEpic; i > 0; i--) {
            this.epicHashMap.put(++taskId, Epic.getRandom(this.taskId));
        }
    }

    public void createSubTaskRandom() {
        //Epic epicTemp = new Epic();
        SubTask subTask;
        int countSubTask = 2; //Task.randomCountTask();
        for (int j : epicHashMap.keySet()) {
            subTaskList = new ArrayList<>();
            if (epicHashMap.get(j).subTaskList == null) {
                for (int i = countSubTask; i > 0; i--) {
                    subTask = SubTask.getRandomSubTask(epicHashMap.get(j).id, ++taskId);
                    subTaskHashMap.put(taskId, subTask);
                    subTaskList.add(subTask);
                }
            }
            epicHashMap.put(epicHashMap.get(j).id, new Epic(epicHashMap.get(j).name, epicHashMap.get(j).discription, epicHashMap.get(j).id, subTaskList, epicHashMap.get(j).status));
        }
    }

    public void removeAllTask() {
        if (epicHashMap.isEmpty() && subTaskHashMap.isEmpty()) {
            System.out.println("Список эпиков и подзадач пуст или еще не создан");
        } else {
            epicHashMap.clear();
            subTaskHashMap.clear();
            System.out.println("Все задачи удалены");
        }
    }

    public void getTaskWithId() {
        System.out.println("Введите идентификатор задачи, которую хотите получить");
        int userInput = scanner.nextInt();
        boolean isFound = false;

        for (int j : epicHashMap.keySet()) {
            if (j == userInput) {
                System.out.println(epicHashMap.get(j));
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            for (int j : subTaskHashMap.keySet()) {
                if (j == userInput) {
                    System.out.println(subTaskHashMap.get(j));
                    isFound = true;
                    break;
                }
            }
        }

        if (!isFound) {
            System.out.println("Задач с таким id не найдено");
        }

    }

    public void getSubTaskEpic() {
        System.out.println("Введите идентификатор эпика, для получения списка его подзадач");
        int userInput = scanner.nextInt();
        boolean isEpic = false;
        for (int j : epicHashMap.keySet()) {
            if (j == userInput) {
                System.out.println("Список подзадач эпика " + epicHashMap.get(j).id + " :" + epicHashMap.get(j).subTaskList);
                isEpic = true;
                break;
            }
        }
        if (!isEpic) {
            System.out.println("Эпика с таким идентификатором не найдено");
        }
    }

    public void removeTaskId() {
        if (epicHashMap.isEmpty()) {
            System.out.println("Эпики не созданы или удалены");
        } else {
            System.out.println("Введите идентификатор задачи, которую хотите удалить");
            int userInput = scanner.nextInt();

            for (int j : epicHashMap.keySet()) {
                if (j == userInput) {
                    if (!epicHashMap.get(j).subTaskList.isEmpty()) {
                        System.out.println("Задача с таким id является Эпиком с непустым списком подзадач. Сначала необходимо удалить подзадачи эпика");
                        break;
                    } else {
                        epicHashMap.remove(j);
                        System.out.println("Эпик с id " + userInput + "удален");
                    }
                }
            }

            for (int j : subTaskHashMap.keySet()) {
                if (j == userInput) {
                    //удаление подзадачи из листа эпика
                    removeTaskSubTaskListEpic(userInput);
                    subTaskHashMap.remove(j);
                    System.out.println("Подзадача с id " + userInput + " удалена");
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
                epicId = subTaskHashMap.get(j).epicId;
                break;
            }
        }

        for (int i = 0; i < epicHashMap.get(epicId).subTaskList.size(); i++) {
            if (epicHashMap.get(epicId).subTaskList.get(i).id == taskId) {
                epicHashMap.get(epicId).subTaskList.remove(i);
            }
        }
    }

    public void updateEpic() {
        System.out.println("Введите идентификатор эпика, который хотите обновить. В эпик будет добавлена новая подзадача");
        int userInput = scanner.nextInt();
        //создадим и добавим новую подзадачу в эпик
        System.out.println("Эпик до обновления");
        for (int j : epicHashMap.keySet()) {
            if (userInput == epicHashMap.get(j).id) {
                System.out.println("Эпик до обновления" + epicHashMap.get(j) + epicHashMap.get(j).subTaskList);
                SubTask subTask = SubTask.getRandomSubTask(epicHashMap.get(j).id, ++taskId);
                epicHashMap.get(j).subTaskList.add(subTask);
                subTaskHashMap.put(taskId, subTask);
                System.out.println("Эпик после обновления" + epicHashMap.get(j) + epicHashMap.get(j).subTaskList);
                break;
            }
        }
    }

}
    
    







