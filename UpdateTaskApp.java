import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

// Abstract Task class
abstract class Task {
    protected String title;
    protected String description;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public abstract String getType();

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "[" + getType() + "] " + title + ": " + description;
    }
}

// Subclasses for different types of tasks
class NamazTask extends Task {
    public NamazTask(String title, String description) {
        super(title, description);
    }
    @Override
    public String getType() { return "Namaz"; }
}

class GymTask extends Task {
    public GymTask(String title, String description) {
        super(title, description);
    }
    @Override
    public String getType() { return "Gym"; }
}

class GeneralTask extends Task {
    public GeneralTask(String title, String description) {
        super(title, description);
    }
    @Override
    public String getType() { return "General"; }
}

// Factory for creating tasks
class TaskFactory {
    public static Task createTask(String type, String title, String desc) {
        return switch (type.toLowerCase()) {
            case "namaz" -> new NamazTask(title, desc);
            case "gym" -> new GymTask(title, desc);
            default -> new GeneralTask(title, desc);
        };
    }
}

// Manages tasks
class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void updateTask(int index, Task updatedTask) {
        if (index >= 0 && index < tasks.size()) {
            tasks.set(index, updatedTask);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }
}

// UI for updating tasks
public class UpdateTaskApp {
    private TaskManager manager = new TaskManager();
    private DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private JList<Task> taskJList = new JList<>(taskListModel);

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Update Task");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        preloadTasks(); // Load sample tasks before UI setup

        // Fields and Components
        JTextField titleField = new JTextField(15);
        JTextField descField = new JTextField(15);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"General", "Namaz", "Gym"});
        JButton updateBtn = new JButton("Update Selected Task");

        // List selection listener
        taskJList.addListSelectionListener(e -> {
            Task selected = taskJList.getSelectedValue();
            if (selected != null) {
                titleField.setText(selected.getTitle());
                descField.setText(selected.getDescription());
                typeCombo.setSelectedItem(selected.getType());
            }
        });

        // Update button logic
      updateBtn.addActionListener(e -> {
    int selectedIndex = taskJList.getSelectedIndex();
    if (selectedIndex != -1) {
        String newTitle = titleField.getText();
        String newDesc = descField.getText();
        String newType = typeCombo.getSelectedItem().toString();
        Task updated = TaskFactory.createTask(newType, newTitle, newDesc);
        manager.updateTask(selectedIndex, updated);
        taskListModel.set(selectedIndex, updated);
        JOptionPane.showMessageDialog(frame, "Task updated successfully!");
    } else {
        JOptionPane.showMessageDialog(frame, "Select a task to update.");
    }
});


        // Input form panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("")); // empty cell
        inputPanel.add(updateBtn);

        // Final UI layout
        frame.setLayout(new BorderLayout(10,10));
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(taskJList), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // center the window
        frame.setVisible(true); // show the window
    }

    private void preloadTasks() {
        Task t1 = TaskFactory.createTask("Namaz", "Fajr", "Morning prayer");
        Task t2 = TaskFactory.createTask("Gym", "Leg Day", "Do squats");
        Task t3 = TaskFactory.createTask("General", "Study", "Java practice");

        manager.addTask(t1);
        manager.addTask(t2);
        manager.addTask(t3);

        taskListModel.addElement(t1);
        taskListModel.addElement(t2);
        taskListModel.addElement(t3);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UpdateTaskApp().createAndShowGUI();
        });
    }
}