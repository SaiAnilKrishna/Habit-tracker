import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class FocusForge {
    private static List<Habit> habits = new ArrayList<>();
    private static final String DATA_FILE = "habits.dat";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadData(); // Thaw saved data on startup

        while (true) {
            displayHeader();
            displayHabits();

            System.out.println("\n1. Add Habit  |  2. Log Progress  |  3. Exit");
            System.out.print("Select Action: ");

            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter Habit Name: ");
                String name = sc.nextLine();
                if (!name.isEmpty()) habits.add(new Habit(name));
            }
            else if (choice.equals("2")) {
                if (habits.isEmpty()) {
                    System.out.println("\033[0;31mNo habits found. Add one first!\033[0m");
                    continue;
                }
                System.out.print("Enter Habit Number to log: ");
                try {
                    int index = Integer.parseInt(sc.nextLine()) - 1;
                    if (index >= 0 && index < habits.size()) {
                        habits.get(index).completeToday();
                        System.out.println("\033[0;32m✔ Progress logged for today!\033[0m");
                    }
                } catch (Exception e) {
                    System.out.println("\033[0;31mInvalid input.\033[0m");
                }
            }
            else if (choice.equals("3")) {
                saveData(); // Freeze data before closing
                System.out.println("Saving data... Goodbye!");
                break;
            }
        }
    }

    private static void displayHeader() {
        LocalDate today = LocalDate.now();
        int pending = 0;

        for (Habit h : habits) {
            if (!h.isCompletedToday()) pending++;
        }

        System.out.println("\n\033[1;95mFOCUSFORGE MANAGEMENT | " + today + "\033[0m");

        if (habits.isEmpty()) {
            System.out.println("Status: Welcome! Add a habit to begin tracking.");
        } else if (pending == 0) {
            System.out.println("Status: \033[0;32mAll caught up for today! ★\033[0m");
        } else {
            System.out.println("Status: \033[0;33m" + pending + " Habits remaining for today.\033[0m");
        }
        System.out.println("------------------------------------------------");
    }

    private static void displayHabits() {
        if (habits.isEmpty()) return;

        for (int i = 0; i < habits.size(); i++) {
            Habit h = habits.get(i);
            int streak = h.getStreak();

            // Generate a 10-segment progress bar
            String bar = "■".repeat(Math.min(streak, 10)) + "□".repeat(Math.max(0, 10 - streak));

            String color = h.isCompletedToday() ? "\033[0;32m" : "\033[0;37m";
            System.out.printf("%d. [%s] %s%-15s\033[0m | Total: %d\n",
                    (i + 1), bar, color, h.getName(), streak);
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(habits);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            habits = (List<Habit>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Starting fresh session...");
        }
    }
}