import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * CodeAlpha Java Programming Internship - Task 3
 * Console-based hotel reservation system with file persistence.
 *
 * Reservations are stored in bookings.txt in the current working directory.
 * Payments are simulated; this application does not process real payments.
 */
public class HotelReservationSystem {

    private static final Path DATA_FILE = Path.of("bookings.txt");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Room> ROOMS = Arrays.asList(
            new Room("STD-101", "Standard", 1800),
            new Room("STD-102", "Standard", 1800),
            new Room("STD-103", "Standard", 1800),
            new Room("DLX-201", "Deluxe", 3000),
            new Room("DLX-202", "Deluxe", 3000),
            new Room("DLX-203", "Deluxe", 3000),
            new Room("STE-301", "Suite", 5000),
            new Room("STE-302", "Suite", 5000)
    );

    private static final List<Booking> BOOKINGS = new ArrayList<>();

    static class Room {
        final String id;
        final String category;
        final double pricePerNight;

        Room(String id, String category, double pricePerNight) {
            this.id = id;
            this.category = category;
            this.pricePerNight = pricePerNight;
        }
    }

    static class Booking {
        final String id;
        final String guestName;
        final String roomId;
        final String category;
        final LocalDate checkIn;
        final LocalDate checkOut;
        final double totalPrice;
        String status;

        Booking(String id, String guestName, String roomId, String category,
                LocalDate checkIn, LocalDate checkOut, double totalPrice, String status) {
            this.id = id;
            this.guestName = guestName;
            this.roomId = roomId;
            this.category = category;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.totalPrice = totalPrice;
            this.status = status;
        }

        String toDataLine() {
            return String.join("|", id, clean(guestName), roomId, category,
                    checkIn.toString(), checkOut.toString(),
                    Double.toString(totalPrice), status);
        }

        static Booking fromDataLine(String line) {
            String[] parts = line.split("\\|", -1);
            if (parts.length != 8) {
                return null;
            }
            try {
                return new Booking(parts[0], parts[1], parts[2], parts[3],
                        LocalDate.parse(parts[4]), LocalDate.parse(parts[5]),
                        Double.parseDouble(parts[6]), parts[7]);
            } catch (RuntimeException ex) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        loadBookings();
        int choice;

        do {
            System.out.println("\n========== HOTEL RESERVATION SYSTEM ==========");
            System.out.println("1. View rooms and rates");
            System.out.println("2. Search room availability");
            System.out.println("3. Book a room");
            System.out.println("4. View booking details");
            System.out.println("5. Cancel a booking");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            choice = readInt();

            switch (choice) {
                case 1 -> showRooms();
                case 2 -> searchAvailability();
                case 3 -> bookRoom();
                case 4 -> viewBooking();
                case 5 -> cancelBooking();
                case 6 -> System.out.println("Thank you for using the hotel reservation system.");
                default -> System.out.println("Invalid option. Choose a number from 1 to 6.");
            }
        } while (choice != 6);

        SCANNER.close();
    }

    private static void showRooms() {
        System.out.println("\nAvailable room inventory and rates (per night):");
        System.out.printf("%-12s %-12s %s%n", "Room ID", "Category", "Rate (INR)");
        for (Room room : ROOMS) {
            System.out.printf("%-12s %-12s ₹%.2f%n", room.id, room.category, room.pricePerNight);
        }
        System.out.println("Availability depends on the requested dates.");
    }

    private static void searchAvailability() {
        LocalDate[] dates = readStayDates();
        if (dates == null) return;
        String category = readCategory();

        List<Room> available = findAvailableRooms(category, dates[0], dates[1]);
        System.out.printf("%nAvailable %s rooms from %s to %s:%n",
                category == null ? "all-category" : category, dates[0], dates[1]);

        if (available.isEmpty()) {
            System.out.println("No rooms match those dates.");
            return;
        }
        for (Room room : available) {
            System.out.printf("%s | %s | ₹%.2f per night%n",
                    room.id, room.category, room.pricePerNight);
        }
    }

    private static void bookRoom() {
        System.out.print("Guest full name: ");
        String guestName = clean(SCANNER.nextLine().trim());
        if (guestName.isBlank()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }

        LocalDate[] dates = readStayDates();
        if (dates == null) return;

        String category = readCategory();
        List<Room> available = findAvailableRooms(category, dates[0], dates[1]);
        if (available.isEmpty()) {
            System.out.println("Sorry, no rooms are available for those dates and category.");
            return;
        }

        System.out.println("\nAvailable rooms:");
        for (int i = 0; i < available.size(); i++) {
            Room room = available.get(i);
            System.out.printf("%d. %s (%s) — ₹%.2f per night%n",
                    i + 1, room.id, room.category, room.pricePerNight);
        }

        System.out.print("Choose a room number: ");
        int roomChoice = readInt();
        if (roomChoice < 1 || roomChoice > available.size()) {
            System.out.println("Invalid room selection.");
            return;
        }

        Room selected = available.get(roomChoice - 1);
        long nights = ChronoUnit.DAYS.between(dates[0], dates[1]);
        double total = selected.pricePerNight * nights;

        System.out.println("\n----------- BOOKING SUMMARY -----------");
        System.out.println("Guest: " + guestName);
        System.out.println("Room: " + selected.id + " (" + selected.category + ")");
        System.out.println("Check-in: " + dates[0]);
        System.out.println("Check-out: " + dates[1]);
        System.out.println("Nights: " + nights);
        System.out.printf("Total amount: ₹%.2f%n", total);
        System.out.println("---------------------------------------");
        System.out.print("Simulate payment of this amount? (yes/no): ");

        String payment = SCANNER.nextLine().trim();
        if (!payment.equalsIgnoreCase("yes") && !payment.equalsIgnoreCase("y")) {
            System.out.println("Payment not completed. No booking was created.");
            return;
        }

        // Recheck availability before confirming, even though this is a single-user console app.
        if (!findAvailableRooms(selected.category, dates[0], dates[1]).stream()
                .anyMatch(room -> room.id.equals(selected.id))) {
            System.out.println("That room is no longer available for those dates.");
            return;
        }

        String bookingId = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking booking = new Booking(bookingId, guestName, selected.id, selected.category,
                dates[0], dates[1], total, "CONFIRMED");
        BOOKINGS.add(booking);

        if (saveBookings()) {
            System.out.println("Simulated payment successful. Reservation confirmed!");
            printBooking(booking);
        } else {
            BOOKINGS.remove(booking);
            System.out.println("Could not save the booking. Please try again.");
        }
    }

    private static void viewBooking() {
        System.out.print("Enter booking ID: ");
        String id = SCANNER.nextLine().trim();
        Booking booking = findBooking(id);
        if (booking == null) {
            System.out.println("Booking not found.");
        } else {
            printBooking(booking);
        }
    }

    private static void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        String id = SCANNER.nextLine().trim();
        Booking booking = findBooking(id);

        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }
        if (!"CONFIRMED".equals(booking.status)) {
            System.out.println("This booking is already cancelled.");
            return;
        }

        System.out.printf("Cancel reservation %s for %s? (yes/no): ",
                booking.id, booking.guestName);
        String answer = SCANNER.nextLine().trim();
        if (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("y")) {
            System.out.println("Cancellation aborted.");
            return;
        }

        booking.status = "CANCELLED";
        if (saveBookings()) {
            System.out.println("Booking cancelled. The room is available again for those dates.");
            System.out.println("Note: this demo does not process real refunds.");
        } else {
            booking.status = "CONFIRMED";
            System.out.println("Could not save cancellation. No changes were kept.");
        }
    }

    private static LocalDate[] readStayDates() {
        try {
            System.out.print("Check-in date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(SCANNER.nextLine().trim());
            System.out.print("Check-out date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(SCANNER.nextLine().trim());

            if (!checkOut.isAfter(checkIn)) {
                System.out.println("Check-out must be after check-in.");
                return null;
            }
            if (checkIn.isBefore(LocalDate.now())) {
                System.out.println("Check-in cannot be in the past.");
                return null;
            }
            return new LocalDate[]{checkIn, checkOut};
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date. Use YYYY-MM-DD, for example 2026-12-25.");
            return null;
        }
    }

    private static String readCategory() {
        System.out.println("Room category: 1. Any  2. Standard  3. Deluxe  4. Suite");
        System.out.print("Choose category: ");
        return switch (readInt()) {
            case 1 -> null;
            case 2 -> "Standard";
            case 3 -> "Deluxe";
            case 4 -> "Suite";
            default -> {
                System.out.println("Invalid category; searching all categories.");
                yield null;
            }
        };
    }

    private static List<Room> findAvailableRooms(String category, LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room room : ROOMS) {
            if (category != null && !room.category.equalsIgnoreCase(category)) {
                continue;
            }
            boolean occupied = false;
            for (Booking booking : BOOKINGS) {
                if ("CONFIRMED".equals(booking.status)
                        && room.id.equals(booking.roomId)
                        && checkIn.isBefore(booking.checkOut)
                        && checkOut.isAfter(booking.checkIn)) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) available.add(room);
        }
        return available;
    }

    private static Booking findBooking(String id) {
        for (Booking booking : BOOKINGS) {
            if (booking.id.equalsIgnoreCase(id)) return booking;
        }
        return null;
    }

    private static void printBooking(Booking booking) {
        System.out.println("\n========== BOOKING DETAILS ==========");
        System.out.println("Booking ID: " + booking.id);
        System.out.println("Guest: " + booking.guestName);
        System.out.println("Room: " + booking.roomId + " (" + booking.category + ")");
        System.out.println("Check-in: " + booking.checkIn);
        System.out.println("Check-out: " + booking.checkOut);
        System.out.printf("Total: ₹%.2f%n", booking.totalPrice);
        System.out.println("Status: " + booking.status);
        System.out.println("=====================================");
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.print("Enter a valid whole number: ");
            }
        }
    }

    private static String clean(String value) {
        return value.replace("|", "/").replace("\\r", " ").replace("\\n", " ").trim();
    }

    private static void loadBookings() {
        if (!Files.exists(DATA_FILE)) return;
        try {
            for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
                Booking booking = Booking.fromDataLine(line);
                if (booking != null) BOOKINGS.add(booking);
            }
        } catch (IOException ex) {
            System.out.println("Warning: could not read saved bookings: " + ex.getMessage());
        }
    }

    private static boolean saveBookings() {
        List<String> lines = new ArrayList<>();
        for (Booking booking : BOOKINGS) lines.add(booking.toDataLine());
        try {
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException ex) {
            System.out.println("File error: " + ex.getMessage());
            return false;
        }
    }
}
