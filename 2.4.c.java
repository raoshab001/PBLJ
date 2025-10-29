import java.sql.*;
import java.util.*;

// ===========================
// MODEL CLASS: Student
// ===========================
class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    // Getters
    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}

// ===========================
// CONTROLLER CLASS: StudentDAO (Database Operations)
// ===========================
class StudentDAO {
    private Connection con;

    // Constructor establishes DB connection
    public StudentDAO() throws Exception {
        // Update database details as needed
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "yourpassword";

        // Load JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, user, password);
    }

    // Create: Add student
    public void addStudent(Student s) throws Exception {
        String query = "INSERT INTO Student VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, s.getStudentID());
        ps.setString(2, s.getName());
        ps.setString(3, s.getDepartment());
        ps.setDouble(4, s.getMarks());
        ps.executeUpdate();
        ps.close();
    }

    // Read: Get all students
    public List<Student> getAllStudents() throws Exception {
        List<Student> list = new ArrayList<>();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Student");
        while (rs.next()) {
            list.add(new Student(rs.getInt(1), rs.getString(2),
                                 rs.getString(3), rs.getDouble(4)));
        }
        rs.close();
        st.close();
        return list;
    }

    // Update: Update student marks
    public void updateStudent(int id, double marks) throws Exception {
        String query = "UPDATE Student SET Marks = ? WHERE StudentID = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setDouble(1, marks);
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close();
    }

    // Delete: Remove student
    public void deleteStudent(int id) throws Exception {
        String query = "DELETE FROM Student WHERE StudentID = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }

    // Close connection when done
    public void closeConnection() throws Exception {
        if (con != null) con.close();
    }
}

// ===========================
// VIEW CLASS: Main Menu / UI
// ===========================
public class StudentManagementApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            StudentDAO dao = new StudentDAO();
            int choice;

            do {
                System.out.println("\n=== STUDENT MANAGEMENT SYSTEM ===");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student Marks");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Student ID: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // consume newline
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Department: ");
                        String dept = sc.nextLine();
                        System.out.print("Enter Marks: ");
                        double marks = sc.nextDouble();

                        dao.addStudent(new Student(id, name, dept, marks));
                        System.out.println("✅ Student added successfully!");
                        break;

                    case 2:
                        List<Student> students = dao.getAllStudents();
                        System.out.println("\nID | Name | Department | Marks");
                        System.out.println("------------------------------------");
                        for (Student s : students) {
                            System.out.println(s.getStudentID() + " | " +
                                               s.getName() + " | " +
                                               s.getDepartment() + " | " +
                                               s.getMarks());
                        }
                        break;

                    case 3:
                        System.out.print("Enter Student ID to update: ");
                        int sid = sc.nextInt();
                        System.out.print("Enter new Marks: ");
                        double newMarks = sc.nextDouble();
                        dao.updateStudent(sid, newMarks);
                        System.out.println("✅ Student marks updated!");
                        break;

                    case 4:
                        System.out.print("Enter Student ID to delete: ");
                        int did = sc.nextInt();
                        dao.deleteStudent(did);
                        System.out.println("✅ Student deleted!");
                        break;

                    case 5:
                        dao.closeConnection();
                        System.out.println("Exiting program...");
                        break;

                    default:
                        System.out.println("❌ Invalid choice, try again!");
                }

            } while (choice != 5);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
