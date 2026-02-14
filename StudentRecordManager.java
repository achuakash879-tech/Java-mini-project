import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CollegeRecordManager extends JFrame {

    private JTextField rollField, nameField, deptField, marksField;
    private JTextArea displayArea;
    private Connection con;

    public CollegeRecordManager() {

        con = DBConnection.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!");
            System.exit(0);
        }

        setTitle("College Record Manager");
        setSize(650, 500);
        setLayout(new BorderLayout());

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        rollField = new JTextField();
        nameField = new JTextField();
        deptField = new JTextField();
        marksField = new JTextField();

        inputPanel.add(new JLabel("Roll No"));
        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Department"));
        inputPanel.add(new JLabel("Marks"));

        inputPanel.add(rollField);
        inputPanel.add(nameField);
        inputPanel.add(deptField);
        inputPanel.add(marksField);

        add(inputPanel, BorderLayout.NORTH);

        // ===== DISPLAY AREA =====
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton viewBtn = new JButton("View");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        viewBtn.addActionListener(e -> viewStudents());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText());
            int marks = Integer.parseInt(marksField.getText());
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();

            if (name.isEmpty() || dept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields required!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO students VALUES (?, ?, ?, ?)"
            );

            ps.setInt(1, roll);
            ps.setString(2, name);
            ps.setString(3, dept);
            ps.setInt(4, marks);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student Added Successfully!");
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Roll and Marks must be numbers!");
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Roll number already exists!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void updateStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText());
            int marks = Integer.parseInt(marksField.getText());
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE students SET name=?, department=?, marks=? WHERE roll_no=?"
            );

            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setInt(3, marks);
            ps.setInt(4, roll);

            int rows = ps.executeUpdate();

            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Record Updated Successfully!");
            else
                JOptionPane.showMessageDialog(this, "Roll Number Not Found!");

            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Roll and Marks must be numbers!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void deleteStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText());

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM students WHERE roll_no=?"
            );

            ps.setInt(1, roll);
            int rows = ps.executeUpdate();

            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Record Deleted!");
            else
                JOptionPane.showMessageDialog(this, "Roll Number Not Found!");

            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid roll number!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void viewStudents() {
        try {
            displayArea.setText("");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                displayArea.append(
                        rs.getInt("roll_no") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("department") + " | " +
                        rs.getInt("marks") + "\n"
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        rollField.setText("");
        nameField.setText("");
        deptField.setText("");
        marksField.setText("");
    }

    public static void main(String[] args) {
        new CollegeRecordManager();
    }
}
