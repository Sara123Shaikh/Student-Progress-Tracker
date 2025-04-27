import java.util.*;

// Base class
class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

// Aggregated class
class Course {
    String courseName;

    public Course(String courseName) {
        this.courseName = courseName;
    }
}

// Composed class
class Progress {
    int marks;
    String grade;

    public Progress() {
        this.marks = 0;
        this.grade = "Not Assigned";
    }

    public void updateProgress(int marks) {
        this.marks = marks;
        if (marks >= 90) grade = "A";
        else if (marks >= 75) grade = "B";
        else if (marks >= 60) grade = "C";
        else grade = "F";
    }
}

// Student class with inheritance, aggregation, and composition
class Student extends Person {
    Progress progress;
    Course course;

    public Student(String name, int age, Course course) {
        super(name, age);
        this.course = course;
        this.progress = new Progress();
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Course: " + course.courseName);
        System.out.println("Marks: " + progress.marks + ", Grade: " + progress.grade);
        System.out.println("-----------------------------------");
    }
}

// Thread class to update progress
class ProgressUpdater extends Thread {
    Student student;
    int marks;

    public ProgressUpdater(Student student, int marks) {
        this.student = student;
        this.marks = marks;
    }

    public void run() {
        System.out.println("Updating progress for " + student.name + "...");
        student.progress.updateProgress(marks);
        System.out.println("Progress updated for " + student.name + ".");
    }
}

// Main class
public class StudentProgressTracker2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Course options
        Course java = new Course("Java Programming");
        Course python = new Course("Python Programming");

        List<Student> students = new ArrayList<>();

        System.out.print("Enter number of students: ");
        int n = sc.nextInt();
        sc.nextLine(); // Consume newline

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for student " + (i + 1));
            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Age: ");
            int age = sc.nextInt();
            sc.nextLine(); // Consume newline

            System.out.print("Course (Java/Python): ");
            String courseInput = sc.nextLine();
            Course course = courseInput.equalsIgnoreCase("Java") ? java : python;

            students.add(new Student(name, age, course));
        }

        // Display initial info
        System.out.println("\nInitial Student Info:");
        for (Student s : students) {
            s.displayInfo();
        }

        // Progress update
        System.out.println("\nEnter marks for each student:");
        List<ProgressUpdater> updaters = new ArrayList<>();
        for (Student s : students) {
            System.out.print("Enter marks for " + s.name + ": ");
            int marks = sc.nextInt();
            ProgressUpdater updater = new ProgressUpdater(s, marks);
            updaters.add(updater);
            updater.start();
        }

        // Wait for all threads to complete
        for (ProgressUpdater updater : updaters) {
            try {
                updater.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Display updated info
        System.out.println("\nAfter Progress Update:");
        for (Student s : students) {
            s.displayInfo();
        }

        sc.close();
    }
}