# CodeAlpha Internship Tasks

Java-based console applications for the CodeAlpha Java Programming Internship.

## Task 1: Student Grade Tracker

**Source:** [`Task1/StudentGradeTracker.java`](Task1/StudentGradeTracker.java)

- Add students and marks (0–100), with input validation.
- Assign grades and show all student records.
- Calculate class average, highest marks, and lowest marks.
- Student records are kept in memory for the current run.

Compile and run from the repository root:

```bash
javac Task1/StudentGradeTracker.java
java -cp Task1 StudentGradeTracker
```

The example grade scale is: A+ (90+), A (80+), B (70+), C (60+), D (50+), E (33+), F (below 33). These grade boundaries are implementation choices; the task brief does not prescribe a scale.

## Task 3: Artificial Intelligence Chatbot

**Source:** [`Task3/AIChatbot.java`](Task3/AIChatbot.java)

A rule-based console chatbot demonstrating introductory NLP-style preprocessing and intent detection.

### Features
- Normalises user input to lowercase and removes punctuation.
- Tokenises text and matches common phrases/keywords to intents.
- Responds to greetings, help requests, questions about Java and the internship, contact requests, thanks, and goodbye.
- Provides a fallback response for unrecognised input.
- Runs interactively until the user types `bye`, `exit`, or another recognised goodbye phrase.

### Compile and run

```bash
javac Task3/AIChatbot.java
java -cp Task3 AIChatbot
```

**Scope note:** This is a small rule-based chatbot, not a trained machine-learning model. Its responses are based on predefined phrases and rules.

## Task 4: Hotel Reservation System

**Source:** [`Task4/HotelReservationSystem.java`](Task4/HotelReservationSystem.java)

A console-based hotel booking system with Standard, Deluxe, and Suite rooms.

### Features
- Displays room inventory and nightly rates.
- Searches room availability for a check-in/check-out date range.
- Creates bookings after a simulated payment confirmation.
- Generates a booking ID and displays booking details.
- Looks up and cancels reservations.
- Checks date overlap so a confirmed room cannot be double-booked for overlapping stays.
- Persists bookings and cancellation status in `bookings.txt`.

### Compile and run

```bash
javac Task4/HotelReservationSystem.java
java -cp Task4 HotelReservationSystem
```

Run it from the repository root so the data file is created as `bookings.txt` in that directory. Use dates in `YYYY-MM-DD` format. The room inventory and prices are sample data.

**Payment note:** Payment is simulated for demonstration only. No real payment is collected, and the app does not issue actual refunds.

## Requirements

- Java JDK installed (includes `javac` and `java`).
- Java 14 or later is recommended for the switch-expression syntax used in Task 4.

## Internship submission checklist

- Push the complete source code to GitHub.
- Test each program and capture a short demo video.
- Post the project explanation video on LinkedIn with the repository link and tag `@CodeAlpha`.
- Submit the completed tasks using the submission form shared by CodeAlpha.

The internship brief says participants must complete at least two or three of the four listed tasks; submitting only one task is considered incomplete.
