/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TheEmployeeManagementSystem;

/**
 *
 * @author user
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class EMS {

    

    private static final String DB_URL = "jdbc:mysql://localhost:3306/employees";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database");

            MainMenu mainMenu = new MainMenu();

            while (true) {
                mainMenu.menu();
                System.out.print("Please Enter choice: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addEmployee(connection, scanner);
                        break;
                    case 2:
                        showEmployee(connection, scanner);
                        break;
                    case 3:
                        removeEmployee(connection, scanner);
                        break;
                    case 4:
                        updateEmployee(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }

    private static void addEmployee(Connection connection, Scanner scanner) throws SQLException {
        EmployDetail employDetail = new EmployDetail();
        employDetail.getInfo(scanner);

        String sql = "INSERT INTO employees (name, father_name, employ_id, email, position, employ_contact, employ_salary) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employDetail.name);
            statement.setString(2, employDetail.father_name);
            statement.setString(3, employDetail.employ_id);
            statement.setString(4, employDetail.email);
            statement.setString(5, employDetail.position);
            statement.setString(6, employDetail.employ_contact);
            statement.setString(7, employDetail.employ_salary);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee has been added successfully");
            }
        }
    }

    private static void showEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee's ID: ");
        String employ_id = scanner.nextLine();

        String sql = "SELECT * FROM employees WHERE employ_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employ_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Employee Details:");
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Father's Name: " + resultSet.getString("father_name"));
                System.out.println("Employee ID: " + resultSet.getString("employ_id"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Position: " + resultSet.getString("position"));
                System.out.println("Employee Contact: " + resultSet.getString("employ_contact"));
                System.out.println("Employee Salary: " + resultSet.getString("employ_salary"));
            } else {
                System.out.println("Employee not found");
            }
        }
    }

    private static void removeEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee's ID: ");
        String employ_id = scanner.nextLine();

        String sql = "DELETE FROM employees WHERE employ_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employ_id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee has been removed successfully");
            } else {
                System.out.println("Employee not found");
            }
        }
    }

    private static void updateEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee's ID: ");
        String employ_id = scanner.nextLine();

        System.out.println("Please Enter the detail you want to Update:");
        System.out.println("For Example: If you want to Change the Name, then Enter Current Name and Press Enter. Then write the new Name then Press Enter. It will Update the Name.");

        String fieldToUpdate = scanner.nextLine();
        System.out.print("Please Enter the Updated Info: ");
        String newValue = scanner.nextLine();

        String sql = "UPDATE employees SET " + fieldToUpdate + " = ? WHERE employ_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newValue);
            statement.setString(2, employ_id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee details have been updated successfully");
            } else {
                System.out.println("Employee not found");
            }
        }
    }
}

class EmployDetail {
    String name;
    String father_name;
    String email;
    String position;
    String employ_id;
    String employ_salary;
    String employ_contact;

    public void getInfo(Scanner scanner) {
        System.out.print("Enter Employee's name: ");
        name = scanner.nextLine();
        System.out.print("Enter Employee's Father name: ");
        father_name = scanner.nextLine();
        System.out.print("Enter Employee's ID: ");
        employ_id = scanner.nextLine();
        System.out.print("Enter Employee's Email ID: ");
        email = scanner.nextLine();
        System.out.print("Enter Employee's Position: ");
        position = scanner.nextLine();
        System.out.print("Enter Employee contact Info: ");
        employ_contact = scanner.nextLine();
        System.out.print("Enter Employee's Salary: ");
        employ_salary = scanner.nextLine();
    }
}

class MainMenu {
    public void menu() {
        System.out.println("\tEMPLOYEE MANAGEMENT SYSTEM");
        System.out.println("\t\t  BY 110850");
        System.out.println("\nPress 1: To Add an Employee Details");
        System.out.println("Press 2: To See an Employee Details");
        System.out.println("Press 3: To Remove an Employee");
        System.out.println("Press 4: To Update Employee Details");
        System.out.println("Press 5: To Exit the EMS Portal");
    }

}
