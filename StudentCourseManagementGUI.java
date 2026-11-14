
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class StudentCourseManagementGUI extends JFrame {

    private CourseManager manager;

    private JTextField codeField;
    private JTextField titleField;
    private JTextField unitsField;
    private JTextField searchField;

    private JTable courseTable;
    private DefaultTableModel tableModel;

    private JLabel totalUnitsLabel;

    private static final String FILE_NAME = "courses.txt";

    // Constructor
    public StudentCourseManagementGUI() {

        manager = new CourseManager();

        setTitle("Student Course Management System");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main heading
        JLabel heading = new JLabel(
                "STUDENT COURSE MANAGEMENT SYSTEM",
                SwingConstants.CENTER);
        JLabel heading = new JLabel(
                "STUDENT COURSE MANAGE",
                SwingConstants.CENTER);


        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(new Color(25, 70, 130));
        heading.setBorder(BorderFactory.createEmptyBorder(
                20, 10, 15, 10));

        add(heading, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        inputPanel.setBorder(BorderFactory.createTitledBorder(
                "Course Information"));

        codeField = new JTextField();
        titleField = new JTextField();
        unitsField = new JTextField();
        searchField = new JTextField();

        inputPanel.add(new JLabel("Course Code:"));
        inputPanel.add(codeField);

        inputPanel.add(new JLabel("Course Title:"));
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Credit Units:"));
        inputPanel.add(unitsField);

        inputPanel.add(new JLabel("Search Course Code:"));
        inputPanel.add(searchField);

        // Table
        String[] columns = {
            "Course Code", "Course Title", "Credit Units"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(25);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(courseTable);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        centerPanel.setBorder(BorderFactory.createEmptyBorder(
                10, 15, 10, 15));

        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(
                new GridLayout(2, 4, 10, 10));

        JButton addButton = new JButton("Add Course");
        JButton viewButton = new JButton("View Courses");
        JButton searchButton = new JButton("Search Course");
        JButton deleteButton = new JButton("Delete Course");
        JButton totalButton = new JButton("Total Units");
        JButton saveButton = new JButton("Save Courses");
        JButton loadButton = new JButton("Load Courses");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(totalButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
                5, 15, 15, 15));

        totalUnitsLabel = new JLabel("Total Credit Units: 0");
        totalUnitsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        bottomPanel.add(totalUnitsLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addCourse());
        viewButton.addActionListener(e -> viewCourses());
        searchButton.addActionListener(e -> searchCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        totalButton.addActionListener(e -> showTotalUnits());
        saveButton.addActionListener(e -> saveCourses());
        loadButton.addActionListener(e -> loadCourses());
        exitButton.addActionListener(e -> exitApplication());

        // Exit confirmation when closing the window
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(
                    java.awt.event.WindowEvent e) {
                exitApplication();
            }
        });

        setVisible(true);
    }

    // Add course
    private void addCourse() {

        try {
            String code = codeField.getText().trim()
                    .toUpperCase();

            String title = titleField.getText().trim();

            String unitsText = unitsField.getText().trim();

            if (code.isEmpty() || title.isEmpty()
                    || unitsText.isEmpty()) {

                throw new IllegalArgumentException(
                        "Please fill in all course details.");
            }

            if (code.contains("\t") || code.contains("\n")
                    || title.contains("\t") || title.contains("\n")) {

                throw new IllegalArgumentException(
                        "Course details cannot contain tabs or new lines.");
            }

            int units = Integer.parseInt(unitsText);

            if (units <= 0) {
                throw new IllegalArgumentException(
                        "Credit units must be greater than zero.");
            }

            Course course = new Course(code, title, units);

            manager.addCourse(course);

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(this,
                    "Course added successfully!");

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Credit units must be a valid whole number.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Display all courses
    private void viewCourses() {

        refreshTable();

        if (manager.getAllCourses().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "No courses have been registered yet.");
        }
    }

    // Search for a course
    private void searchCourse() {

        String code = searchField.getText().trim();

        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter a course code to search.");
            return;
        }

        Course course = manager.searchCourse(code);

        if (course != null) {

            refreshTable();

            for (int i = 0; i < tableModel.getRowCount(); i++) {

                if (tableModel.getValueAt(i, 0)
                        .toString().equalsIgnoreCase(code)) {

                    courseTable.setRowSelectionInterval(i, i);
                    courseTable.scrollRectToVisible(
                            courseTable.getCellRect(i, 0, true));
                    break;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Course Found!\n\n"
                    + "Course Code: " + course.getCourseCode()
                    + "\nCourse Title: " + course.getCourseTitle()
                    + "\nCredit Units: " + course.getUnits());

        } else {

            JOptionPane.showMessageDialog(this,
                    "Course not found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Delete course
    private void deleteCourse() {

        int selectedRow = courseTable.getSelectedRow();

        if (selectedRow < 0) {

            JOptionPane.showMessageDialog(this,
                    "Please select a course from the table.");
            return;
        }

        String code = tableModel.getValueAt(
                selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + code + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            manager.deleteCourse(code);

            refreshTable();

            JOptionPane.showMessageDialog(this,
                    "Course deleted successfully.");
        }
    }

    // Calculate total units
    private void showTotalUnits() {

        int total = manager.calculateTotalUnits();

        totalUnitsLabel.setText("Total Credit Units: " + total);

        JOptionPane.showMessageDialog(this,
                "Total Credit Units: " + total);
    }

    // Refresh table
    private void refreshTable() {

        tableModel.setRowCount(0);

        List<Course> courses = manager.getAllCourses();

        for (Course course : courses) {

            Object[] row = {
                course.getCourseCode(),
                course.getCourseTitle(),
                course.getUnits()
            };

            tableModel.addRow(row);
        }

        totalUnitsLabel.setText(
                "Total Credit Units: "
                + manager.calculateTotalUnits());
    }

    // Save courses
    private void saveCourses() {

        try {

            manager.saveCourses(FILE_NAME);

            JOptionPane.showMessageDialog(this,
                    "Courses saved successfully to "
                    + FILE_NAME);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(this,
                    "Error saving courses: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load courses
    private void loadCourses() {

        try {

            manager.loadCourses(FILE_NAME);

            refreshTable();

            JOptionPane.showMessageDialog(this,
                    "Courses loaded successfully!");

        } catch (IOException e) {

            JOptionPane.showMessageDialog(this,
                    "Error loading courses: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clear input fields
    private void clearFields() {

        codeField.setText("");
        titleField.setText("");
        unitsField.setText("");
    }

    // Exit application
    private void exitApplication() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Do you want to exit the application?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentCourseManagementGUI();
        });
    }
}
