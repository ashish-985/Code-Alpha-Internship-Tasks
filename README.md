# CodeAlpha Internship Tasks

Java-based projects for the CodeAlpha Java Programming Internship.

## Task 1: Student Grade Tracker

**Location:** `Task1/StudentGradeTracker.java`

A console-based Java application to manage student grades and generate a class summary report.

### Features

- Add student names and marks.
- Validate names and numeric input.
- Accept marks from 0 to 100.
- Assign a grade to each student.
- Display every student's marks and grade.
- Calculate the class average, highest marks, and lowest marks.
- Handle an empty report and invalid menu input.

### Grade scale

The following grade boundaries are example rules used by this implementation (the task brief does not prescribe a grading scale):

| Marks | Grade |
| --- | --- |
| 90–100 | A+ |
| 80–89.99 | A |
| 70–79.99 | B |
| 60–69.99 | C |
| 50–59.99 | D |
| 33–49.99 | E |
| 0–32.99 | F |

### Requirements

- Java JDK installed (includes `javac` and `java`).

### Compile and run

From the repository root:

```bash
javac Task1/StudentGradeTracker.java
java -cp Task1 StudentGradeTracker
```

### How to use

1. Choose **1** to add a student.
2. Enter the student's name and marks between 0 and 100.
3. Choose **2** to view the summary report.
4. Choose **3** to exit.

Student records are held in memory for the current run and are cleared when the program exits.
