import org.apache.commons.lang3.RandomStringUtils;


public class SubTask extends Task{
    public int epicId;


    public SubTask(String name, String discription, int epicId, int id, Status status) {
        this.name=name;
        this.discription=discription;
        this.epicId = epicId;
        this.id=id;
        this.status=status;
    }

    public static SubTask getRandomSubTask (int epicId, int subTaskId){
        String name = RandomStringUtils.randomAlphabetic(10);
        String discription = RandomStringUtils.randomAlphabetic(10);
        return new SubTask(name,discription,epicId,subTaskId, Status.NEW);
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "\n" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}' +
                "\n";
    }
}
