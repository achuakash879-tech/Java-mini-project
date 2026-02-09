import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;



public class StudentRecordManager extends JFrame {

    abstract class Person {
        protected String name;
        public abstract String displayDetails();
    }

    class Student extends Person {
        private int rollNo;
        private String department;
        private int marks;

        public Student(int rollNo, String name, String department, int marks) {
            this.rollNo = rollNo;
            this.name = name;
            this.department = department;
            this.marks = marks;
        }

        public int getRollNo() {
            return rollNo;
        }

        @Override
        public String displayDetails() {
            return "Roll: " + rollNo +
                   " | Name: " + name +
                   " | Dept: " + department +
                   " | Marks: " + marks;
        }
    }

    class SaveThread extends Thread {
        public void run() {
            try {
                Thread.sleep(1000); // simulate background save
                System.out.println("Data saved in background thread");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<Student> students = new ArrayList<>();

    JTextField rollField, nameField, deptField, marksField;
    JTextArea displayArea;

    public StudentRecordManager() {

        setTitle("Student Record Manager");
        setSize(500, 500);
        setLayout(new FlowLayout());

        rollField = new JTextField(10);
        nameField = new JTextField(10);
        deptField = new JTextField(10);
        marksField = new JTextField(10);

        displayArea = new JTextArea(12, 40);
        displayArea.setEditable(false);

        JButton addBtn = new JButton("Add Student");
        JButton viewBtn = new JButton("View All");

        add(new JLabel("Roll No:"));
        add(rollField);

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Department:"));
        add(deptField);

        add(new JLabel("Marks:"));
        add(marksField);

        add(addBtn);
        add(viewBtn);

        add(new JScrollPane(displayArea));

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    int roll = Integer.parseInt(rollField.getText());
                    int marks = Integer.parseInt(marksField.getText());
                    String name = nameField.getText();
                    String dept = deptField.getText();

                    if (name.isEmpty() || dept.isEmpty()) {
                        throw new Exception("Fields cannot be empty");
                    }

                    Student s = new Student(roll, name, dept, marks);
                    students.add(s);

                    new SaveThread().start(); // Multithreading

                    JOptionPane.showMessageDialog(null, "Student Added Successfully");

                    rollField.setText("");
                    nameField.setText("");
                    deptField.setText("");
                    marksField.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Roll No and Marks must be numbers");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("");
                for (Student s : students) {
                    displayArea.append(s.displayDetails() + "\n");
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StudentRecordManager();
    }
}