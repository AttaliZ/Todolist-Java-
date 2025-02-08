import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TodoApp {
    private static final int MAX_TASKS = 100;
    private static Task[] taskList = new Task[MAX_TASKS];
    private static int numTasks = 0;

    static class Task {
        String description;
        int priority;
        int day, month, year;
        boolean isCompleted;

        Task(String description, int priority, int day, int month, int year) {
            this.description = description;
            this.priority = priority;
            this.day = day;
            this.month = month;
            this.year = year;
            this.isCompleted = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        JTextArea taskListArea = new JTextArea(10, 40);
        taskListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskListArea);
        taskPanel.add(scrollPane);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        JTextField descriptionField = new JTextField();
        JTextField priorityField = new JTextField();
        JTextField dayField = new JTextField();
        JTextField monthField = new JTextField();
        JTextField yearField = new JTextField();

        inputPanel.add(new JLabel("Task Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Priority (1-10):"));
        inputPanel.add(priorityField);
        inputPanel.add(new JLabel("Day (1-31):"));
        inputPanel.add(dayField);
        inputPanel.add(new JLabel("Month (1-12):"));
        inputPanel.add(monthField);
        inputPanel.add(new JLabel("Year (1900-9999):"));
        inputPanel.add(yearField);

        JButton addButton = new JButton("Add Task");
        JButton saveButton = new JButton("Save Tasks");
        JButton loadButton = new JButton("Load Tasks");

        addButton.addActionListener(e -> {
            String description = descriptionField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            int day = Integer.parseInt(dayField.getText());
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());
            addTask(description, priority, day, month, year);
            updateTaskList(taskListArea);
        });

        saveButton.addActionListener(e -> {
            String folderName = JOptionPane.showInputDialog(frame, "Enter folder name to save tasks:");
            saveTasks(folderName);
        });

        loadButton.addActionListener(e -> {
            String folderName = JOptionPane.showInputDialog(frame, "Enter folder name to load tasks:");
            loadTasks(folderName);
            updateTaskList(taskListArea);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        frame.add(taskPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void addTask(String description, int priority, int day, int month, int year) {
        if (numTasks < MAX_TASKS) {
            taskList[numTasks] = new Task(description, priority, day, month, year);
            numTasks++;
            JOptionPane.showMessageDialog(null, "Task added successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, task list is full.");
        }
    }

    private static void updateTaskList(JTextArea taskListArea) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numTasks; i++) {
            Task task = taskList[i];
            sb.append(i + 1).append(". ")
              .append(task.description).append(" - Priority: ").append(task.priority)
              .append(" - Date: ").append(String.format("%02d/%02d/%d", task.day, task.month, task.year))
              .append(" - ").append(task.isCompleted ? "Complete" : "Incomplete")
              .append("\n");
        }
        taskListArea.setText(sb.toString());
    }

    private static void saveTasks(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new File(folder, "tasks.txt"))) {
            for (int i = 0; i < numTasks; i++) {
                Task task = taskList[i];
                writer.println(task.description + "|" + task.priority + "|" + task.day + "|" + task.month + "|" + task.year + "|" + (task.isCompleted ? 1 : 0));
            }
            JOptionPane.showMessageDialog(null, "Tasks saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving tasks.");
        }
    }

    private static void loadTasks(String folderName) {
        File file = new File(folderName, "tasks.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Error loading tasks.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            numTasks = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                String description = parts[0];
                int priority = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                int month = Integer.parseInt(parts[3]);
                int year = Integer.parseInt(parts[4]);
                boolean isCompleted = Integer.parseInt(parts[5]) == 1;
                taskList[numTasks] = new Task(description, priority, day, month, year);
                taskList[numTasks].isCompleted = isCompleted;
                numTasks++;
            }
            JOptionPane.showMessageDialog(null, "Tasks loaded successfully!");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading tasks.");
        }
    }
}
