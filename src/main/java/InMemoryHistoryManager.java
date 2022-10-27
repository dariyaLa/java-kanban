import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final int COUNT_TASK_IN_LIST_HISTORY = 10;
    private final List<Task> taskListHistory = new ArrayList<>();

    @Override
    public void add(Task task){
        if (taskListHistory.size()<COUNT_TASK_IN_LIST_HISTORY){
            taskListHistory.add(task);
        } else {
            taskListHistory.remove(0);
            taskListHistory.add(0,task);
        }
    }

    @Override
    public List<Task> getHistory(){
        return taskListHistory;
    }
}
