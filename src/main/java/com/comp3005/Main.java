package com.comp3005;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private final static String url = "jdbc:postgresql://localhost:5432/University";
    private final static String user = "postgres";
    private final static String password = "student";

    private static String readSqlFile(String filePath) throws IOException {
        StringBuilder sqlScript = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sqlScript.append(line).append("\n");
            }
        }
        return sqlScript.toString();
    }

    // CRUD functions
    public void getAllStudents() {
        String sql = "SELECT * FROM comp3005.students";

        try (Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("student_id") + " " 
                        + resultSet.getString("first_name") + " "
                        + resultSet.getString("last_name") + " " 
                        + resultSet.getString("email") + " "
                        + resultSet.getDate("enrollment_date"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addStudent(String first_name, String last_name, String email, Date enrollment_date) {
        String sql = "INSERT INTO comp3005.students (first_name, last_name, email, enrollment_date) VALUES (?,?,?,?);";

        try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setDate(4, enrollment_date);
            statement.executeUpdate();
            System.out.println(first_name + " " + last_name + " added successfully!\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateStudentEmail(int student_id, String new_email) {
        String sql = "UPDATE comp3005.students SET email = ? WHERE student_id = ?;";

        try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, new_email);
            statement.setInt(2, student_id);
            statement.executeUpdate();
            System.out.println("Student " + student_id + " email updated successfully!\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteStudent(int student_id) {
        String sql = "DELETE FROM comp3005.students WHERE student_id = ?;";

        try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, student_id);
            statement.executeUpdate();
            System.out.println("Student " + student_id + " deleted successfully!\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            // SQL script execution
            // Read SQL file
            String sqlString = readSqlFile("src/main/resources/sql/01.0.sql");

            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            Statement statement = connection.createStatement();
            statement.execute(sqlString);

            System.out.println("SQL script executed successfully!");

            // Application, user input
            String options = "1. List all students\n2. Add a new student\n3. Update a student email\n4. Delete a student\n";
            boolean exit = false;
            while (!exit) {
                System.out.println("\nPick an option (or '0' to quit):");
                System.out.println(options);

                BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
                String query = reader.readLine();

                Main main = new Main();
                switch (query) {
                    case "1": // list all students
                        main.getAllStudents();
                        break;
                    case "2": // add a new student
                        System.out.println("Enter student first name: ");
                        String first_name = reader.readLine();
                        System.out.println("Enter student last name: ");
                        String last_name = reader.readLine();
                        System.out.println("Enter student email: ");
                        String email = reader.readLine();
                        System.out.println("Enter student enrollment date (yyyy-mm-dd): ");
                        String enrollment_date = reader.readLine();
                        main.addStudent(first_name, last_name, email, Date.valueOf(enrollment_date));
                        break;
                    case "3": // update a student email
                        System.out.println("Enter student id: ");
                        int student_id = Integer.parseInt(reader.readLine());
                        System.out.println("Enter new email: ");
                        String new_email = reader.readLine();
                        main.updateStudentEmail(student_id, new_email);
                        break;
                    case "4": // delete a student
                        System.out.println("Enter student id: ");
                        int delete_student_id = Integer.parseInt(reader.readLine());
                        main.deleteStudent(delete_student_id);
                        break;
                    case "0": // quit
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            }

            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}