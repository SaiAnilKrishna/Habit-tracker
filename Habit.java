import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit implements Serializable {
    private String name;
    private List<LocalDate> completionDates;

    public Habit(String name) {
        this.name = name;
        this.completionDates = new ArrayList<>();
    }

    public void completeToday() {
        LocalDate today = LocalDate.now();
        if (!completionDates.contains(today)) {
            completionDates.add(today);
        }
    }

    public boolean isCompletedToday() {
        return completionDates.contains(LocalDate.now());
    }

    public String getName() { return name; }
    public int getStreak() { return completionDates.size(); }
}