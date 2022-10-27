import java.util.List;

public class Managers {

    private static TaskManager manager = new InMemoryTaskManager();
    private static HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public static TaskManager getDefault(){
       return  manager;
    }

    public static List<Task> getDefaultHistory(){
        return inMemoryHistoryManager.getHistory();
    }
}
