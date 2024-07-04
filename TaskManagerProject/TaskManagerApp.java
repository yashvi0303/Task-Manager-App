import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class TaskManagerApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private TaskManager taskManager;

    public TaskManagerApp() {
        taskManager = new TaskManager();
        frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Title", "Description", "Due Date", "Category"}, 0);
        table = new JTable(tableModel);
        taskManager.getTasks().forEach(task -> tableModel.addRow(new Object[]{task.getTitle(), task.getDescription(), task.getDueDate(), task.getCategory()}));

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new AddTaskActionListener());
        controlPanel.add(addButton);

        JButton editButton = new JButton("Edit Task");
        editButton.addActionListener(new EditTaskActionListener());
        controlPanel.add(editButton);

        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new RemoveTaskActionListener());
        controlPanel.add(removeButton);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class AddTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TaskDialog dialog = new TaskDialog(frame, "Add Task");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                Task task = dialog.getTask();
                taskManager.addTask(task);
                tableModel.addRow(new Object[]{task.getTitle(), task.getDescription(), task.getDueDate(), task.getCategory()});
            }
        }
    }

    private class EditTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Task oldTask = taskManager.getTasks().get(selectedRow);
                TaskDialog dialog = new TaskDialog(frame, "Edit Task", oldTask);
                dialog.setVisible(true);

                if (dialog.isSubmitted()) {
                    Task newTask = dialog.getTask();
                    taskManager.updateTask(oldTask, newTask);
                    tableModel.setValueAt(newTask.getTitle(), selectedRow, 0);
                    tableModel.setValueAt(newTask.getDescription(), selectedRow, 1);
                    tableModel.setValueAt(newTask.getDueDate(), selectedRow, 2);
                    tableModel.setValueAt(newTask.getCategory(), selectedRow, 3);
                }
            }
        }
    }

    private class RemoveTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Task task = taskManager.getTasks().get(selectedRow);
                taskManager.removeTask(task);
                tableModel.removeRow(selectedRow);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerApp::new);
    }
}

class TaskDialog extends JDialog {
    private boolean submitted;
    private JTextField titleField;
    private JTextArea descriptionField;
    private JTextField dueDateField;
    private JTextField categoryField;

    public TaskDialog(Frame owner, String title) {
        super(owner, title, true);
        setupDialog();
    }

    public TaskDialog(Frame owner, String title, Task task) {
        super(owner, title, true);
        setupDialog();
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        dueDateField.setText(task.getDueDate().toString());
        categoryField.setText(task.getCategory());
    }

    private void setupDialog() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        titleField = new JTextField();
        descriptionField = new JTextArea(3, 20);
        dueDateField = new JTextField("yyyy-mm-dd");
        categoryField = new JTextField();

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JScrollPane(descriptionField));
        inputPanel.add(new JLabel("Due Date:"));
        inputPanel.add(dueDateField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Task getTask() {
        return new Task(
                titleField.getText(),
                descriptionField.getText(),
                LocalDate.parse(dueDateField.getText()),
                categoryField.getText()
        );
    }
}
