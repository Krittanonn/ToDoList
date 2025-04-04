package UI;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class TodoListAppView extends JFrame {
    private JTextField nameField;
    private JComboBox<String> categoryComboBox, filterCategoryComboBox;
    private JTable pendingTable, completedTable;
    private DefaultTableModel pendingTableModel, completedTableModel;

    public TodoListAppView() {
        setTitle("Todo List");
        setSize(900, 560);
        setResizable(false);  // ห้ามปรับขนาดหน้าต่าง
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==== Panel for Input Fields and Filter ====
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // ใช้ FlowLayout สำหรับให้ทุกอย่างอยู่ในแถวเดียวกัน
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Task Name
        nameField = new JTextField(10); // ขนาดฟิลด์เล็กลง
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(nameField);

        // Category
        categoryComboBox = new JComboBox<>(new String[]{"Work", "Personal", "Study", "Shopping", "Health", "Travel", "Hobby", "Finance", "Home"});
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);

        // Add Task Button
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(this::onAddTask);
        inputPanel.add(addButton);

        // Filter Section
        filterCategoryComboBox = new JComboBox<>(new String[]{"All", "Work", "Personal", "Study", "Shopping", "Health", "Travel", "Hobby", "Finance", "Home"});
        inputPanel.add(new JLabel("Filter:"));
        inputPanel.add(filterCategoryComboBox);

        JButton filterButton = new JButton("Apply Filter");
        filterButton.addActionListener(this::onFilter);
        inputPanel.add(filterButton);

        // ==== Tabbed Pane: Pending and Completed Tasks ====
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pending Tasks Table
        pendingTableModel = new DefaultTableModel(new String[]{"Done", "Name", "Category"}, 0);
        pendingTable = new JTable(pendingTableModel);
        pendingTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        JScrollPane pendingScroll = new JScrollPane(pendingTable);
        tabbedPane.addTab("Pending Tasks", pendingScroll);

        // Completed Tasks Table
        completedTableModel = new DefaultTableModel(new String[]{"Done", "Name", "Category"}, 0);
        completedTable = new JTable(completedTableModel);
        completedTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        JScrollPane completedScroll = new JScrollPane(completedTable);
        tabbedPane.addTab("Completed Tasks", completedScroll);

        // Add Table Model Listener to handle checkbox changes
        pendingTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    boolean isDone = (boolean) pendingTableModel.getValueAt(row, 0);
                    if (isDone) {
                        moveRowToCompleted(row);
                    }
                }
            }
        });

        completedTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    boolean isDone = (boolean) completedTableModel.getValueAt(row, 0);
                    if (!isDone) {
                        moveRowToPending(row);
                    }
                }
            }
        });

        // ==== Add Delete Button ====
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(this::onDeleteTask);
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        inputPanel.add(deleteButton);

        // ==== Combine ====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH); // Add the input panel at the top

        // Add a small gap between the input panel and the tabbed pane
        JPanel gapPanel = new JPanel();
        gapPanel.setPreferredSize(new Dimension(10, 10)); // Set a small gap
        mainPanel.add(gapPanel, BorderLayout.CENTER); // Add the gap panel

        mainPanel.add(tabbedPane, BorderLayout.SOUTH); // Add the tabbed pane below the gap

        add(mainPanel, BorderLayout.CENTER);

        // Load data from CSV
        loadFromCSV();

        // When closing, save the data to CSV
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveToCSV(); // Save the data when closing the application
            }
        });

        Theme.applyRecursively(this);
        setVisible(true);
    }

    // Add task to the pending table
    private void onAddTask(ActionEvent e) {
        String name = nameField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();

        if (!name.isEmpty() && !category.isEmpty()) {
            // Add task to Pending table
            pendingTableModel.addRow(new Object[]{false, name, category});
            nameField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter task name and category.");
        }
    }

    // Filter tasks based on the selected category
    private void onFilter(ActionEvent e) {
        String selectedCategory = (String) filterCategoryComboBox.getSelectedItem();
        filterTasks(selectedCategory);
    }

    // Filter tasks in the pending and completed tables
    private void filterTasks(String category) {
        // Clear the current tables
        pendingTableModel.setRowCount(0);
        completedTableModel.setRowCount(0);

        // Re-add tasks based on the selected category filter
        for (int i = 0; i < pendingTableModel.getRowCount(); i++) {
            String taskCategory = (String) pendingTableModel.getValueAt(i, 2);
            if (category.equals("All") || taskCategory.equals(category)) {
                // Add task to the respective table
                Object[] row = new Object[3];
                for (int j = 0; j < 3; j++) {
                    row[j] = pendingTableModel.getValueAt(i, j);
                }
                pendingTableModel.addRow(row);
            }
        }

        for (int i = 0; i < completedTableModel.getRowCount(); i++) {
            String taskCategory = (String) completedTableModel.getValueAt(i, 2);
            if (category.equals("All") || taskCategory.equals(category)) {
                // Add task to the respective table
                Object[] row = new Object[3];
                for (int j = 0; j < 3; j++) {
                    row[j] = completedTableModel.getValueAt(i, j);
                }
                completedTableModel.addRow(row);
            }
        }
    }

    // Move a row from pending to completed
    private void moveRowToCompleted(int rowIndex) {
        Object[] rowData = new Object[3];
        for (int i = 0; i < 3; i++) {
            rowData[i] = pendingTableModel.getValueAt(rowIndex, i);
        }
        completedTableModel.addRow(rowData);
        pendingTableModel.removeRow(rowIndex);
    }

    // Move a row from completed to pending
    private void moveRowToPending(int rowIndex) {
        Object[] rowData = new Object[3];
        for (int i = 0; i < 3; i++) {
            rowData[i] = completedTableModel.getValueAt(rowIndex, i);
        }
        pendingTableModel.addRow(rowData);
        completedTableModel.removeRow(rowIndex);
    }

    // Delete selected task from the appropriate table
    private void onDeleteTask(ActionEvent e) {
        int selectedRowPending = pendingTable.getSelectedRow();
        int selectedRowCompleted = completedTable.getSelectedRow();

        if (selectedRowPending != -1) {
            pendingTableModel.removeRow(selectedRowPending);
        } else if (selectedRowCompleted != -1) {
            completedTableModel.removeRow(selectedRowCompleted);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.");
        }
    }

    // Save data to a CSV file
    private void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("todo_list.csv"))) {
            // Write header
            writer.write("Done,Name,Category\n");

            // Save pending tasks
            for (int i = 0; i < pendingTableModel.getRowCount(); i++) {
                boolean done = (boolean) pendingTableModel.getValueAt(i, 0);
                String name = (String) pendingTableModel.getValueAt(i, 1);
                String category = (String) pendingTableModel.getValueAt(i, 2);
                writer.write(done + "," + name + "," + category + "\n");
            }

            // Save completed tasks
            for (int i = 0; i < completedTableModel.getRowCount(); i++) {
                boolean done = (boolean) completedTableModel.getValueAt(i, 0);
                String name = (String) completedTableModel.getValueAt(i, 1);
                String category = (String) completedTableModel.getValueAt(i, 2);
                writer.write(done + "," + name + "," + category + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    // Load data from a CSV file
    private void loadFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("todo_list.csv"))) {
            String line;
            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    boolean done = Boolean.parseBoolean(fields[0]);
                    String name = fields[1];
                    String category = fields[2];

                    if (done) {
                        completedTableModel.addRow(new Object[]{done, name, category});
                    } else {
                        pendingTableModel.addRow(new Object[]{done, name, category});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TodoListAppView();
    }
}
