import lombok.Getter;
import lombok.Setter;

public class Task {
    @Setter
    @Getter
    protected String name;
    @Setter
    @Getter
    protected String discription;
    @Setter
    @Getter
    protected int id;
    @Setter
    @Getter
    protected Enum status;

}
