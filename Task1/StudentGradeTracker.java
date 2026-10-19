import java.util.ArrayList;
import java.util.Scanner;

/**
 * CodeAlpha Java Programming Internship - Task 1
 * Student Grade Tracker
 *
 * Stores student names and marks, assigns a grade, and reports
 * the class average, highest marks, and lowest marks.
 */
public class StudentGradeTracker {

    static class Student {
        private final String name;
        private final double marks;

        Student(String name, double marks) {
            this.name = name;
            this.marks = marks;
        }

        String getGrade() {
            if (marks >= 90) return "A+";
            if (marks >= 80) return "A";
            if (marks >= 70) return "B";
            if (marks >= 60) return "C";
            if (marks >= 50) return "D";
            if (marks >= 33) return "E";
            return "F";
        }
    }

    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== STUDENT GRADE TRACKER =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Summary Report");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    displayReport();
                    break;
                case 3:
                    System.out.println("Thank you for using Student Grade Tracker!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter marks (0-100): ");
        double marks = readDouble();

        if (marks < 0 || marks > 100) {
            System.out.println("Marks must be between 0 and 100.");
            return;
        }

        students.add(new Student(name, marks));
        System.out.println("Student added successfully!");
    }

    private static void displayReport() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        double total = 0;
        Student topper = students.get(0);
        Student lowestStudent = students.get(0);

        System.out.println("\n========== STUDENT SUMMARY REPORT ==========");
        System.out.printf("%-20s %-10s %-10s%n", "Student Name", "Marks", "Grade");
        System.out.println("--------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-20s %-10.2f %-10s%n",
                    student.name, student.marks, student.getGrade());

            total += student.marks;

            if (student.marks > topper.marks) {
                topper = student;
            }
            if (student.marks < lowestStudent.marks) {
                lowestStudent = student;
            }
        }

        double average = total / students.size();

        System.out.println("--------------------------------------------");
        System.out.println("Total Students: " + students.size());
        System.out.printf("Class Average: %.2f%n", average);
        System.out.printf("Highest Marks: %.2f (%s)%n", topper.marks, topper.name);
        System.out.printf("Lowest Marks: %.2f (%s)%n", lowestStudent.marks, lowestStudent.name);
        System.out.println("============================================");
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid whole number: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (Double.isFinite(value)) {
                    return value;
                }
                System.out.print("Enter a finite number: ");
            } catch (NumberFormatException e) {
                System.out.print("Enter valid numeric marks: ");
            }
        }
    }
}
