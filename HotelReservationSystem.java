import java.util.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * The HotelReservationSystem class manages the hotel booking system.
 * It provides functionality to create hotels, view hotel details, manage hotels,
 * and simulate booking reservations.
 */
public class HotelReservationSystem {

    private Map<String, Hotel> hotels = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Starts the main loop of the hotel reservation system, offering various
     * functionalities through a command-line interface.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("Hotel Reservation System");
            System.out.println("1. Create Hotel");
            System.out.println("2. View Hotel");
            System.out.println("3. Manage Hotel");
            System.out.println("4. Simulate Booking");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    createHotel();
                    break;
                case 2:
                    viewHotel();
                    break;
                case 3:
                    manageHotel();
                    break;
                case 4:
                    simulateBooking();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Creates a new hotel with a unique name and specifies the number of rooms.
     * Ensures that the hotel name is unique and that the number of rooms is within allowed limits.
     */
    public void createHotel() {
        System.out.print("Enter hotel name: ");
        String name = scanner.nextLine();
        if (hotels.containsKey(name)) {
            System.out.println("Hotel name must be unique.");
            return;
        }
        System.out.print("Enter number of rooms (1-50): ");
        int roomCount = scanner.nextInt();
        if (roomCount < 1 || roomCount > 50) {
            System.out.println("Invalid number of rooms.");
            return;
        }
        hotels.put(name, new Hotel(name, roomCount));
        System.out.println("Base price per night is set to 1299.0");
        System.out.println("Hotel created successfully.");
    }

    /**
     * Displays details of a selected hotel including room availability, room details,
     * and reservation details based on user input.
     */
    public void viewHotel() {
        Hotel hotel = selectHotel();
        if (hotel == null) return;
        System.out.println("Hotel: " + hotel.getName());
        System.out.println("Total number of rooms: " + hotel.getRooms().size());
        System.out.println("Estimated earnings for the month: " + formatPrice(hotel.getEstimatedEarnings()));
        System.out.println("1. View room availability");
        System.out.println("2. View room details");
        System.out.println("3. View reservation details");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                System.out.print("Enter day (1-31): ");
                int day = scanner.nextInt();
                System.out.println("Available rooms: " + hotel.getAvailableRoomsCount(day));
                System.out.println("Booked rooms: " + hotel.getBookedRoomsCount(day));
                break;
            case 2:
                System.out.print("Enter room name: ");
                String roomName = scanner.nextLine();
                Room room = hotel.getRooms().stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
                if (room == null) {
                    System.out.println("Room not found.");
                    return;
                }
                System.out.println("Room: " + room.getName());
                System.out.println("Price per night: " + formatPrice(room.getPricePerNight()));
                System.out.println("Availability: ");
                for (int i = 1; i <= 31; i++) {
                    System.out.println("Day " + i + ": " + (room.isAvailable(i) ? "Available" : "Booked"));
                }
                break;
            case 3:
                System.out.print("Enter guest name: ");
                String guestName = scanner.nextLine();
                Reservation reservation = hotel.getReservations().stream().filter(r -> r.getGuestName().equals(guestName)).findFirst().orElse(null);
                if (reservation == null) {
                    System.out.println("Reservation not found.");
                    return;
                }
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Check-in date: " + reservation.getCheckInDate());
                System.out.println("Check-out date: " + reservation.getCheckOutDate());
                System.out.println("Room: " + reservation.getRoom().getName());
                System.out.println("Total price: " + formatPrice(reservation.getTotalPrice()));
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Allows the user to manage hotel details such as changing the hotel's name,
     * adding or removing rooms, updating room prices, removing reservations, or deleting the hotel.
     */
    public void manageHotel() {
        Hotel hotel = selectHotel();
        if (hotel == null) return;
        System.out.println("1. Change hotel name");
        System.out.println("2. Add room(s)");
        System.out.println("3. Remove room(s)");
        System.out.println("4. Update base price for a room");
        System.out.println("5. Remove reservation");
        System.out.println("6. Remove hotel");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                System.out.print("Enter new hotel name: ");
                String newName = scanner.nextLine();
                if (hotels.containsKey(newName)) {
                    System.out.println("Hotel name must be unique.");
                    return;
                }
                hotels.remove(hotel.getName());
                hotel.setName(newName);
                hotels.put(newName, hotel);
                System.out.println("Hotel name updated successfully.");
                break;
            case 2:
                System.out.print("Enter number of rooms to add: ");
                int roomsToAdd = scanner.nextInt();
                if (hotel.getRooms().size() + roomsToAdd > 50) {
                    System.out.println("Cannot exceed 50 rooms in total.");
                    return;
                }
                for (int i = 1; i <= roomsToAdd; i++) {
                    hotel.addRoom("Room " + (hotel.getRooms().size() + 1));
                }
                System.out.println("Rooms added successfully.");
                break;
            case 3:
                System.out.print("Enter room name to remove: ");
                String roomName = scanner.nextLine();
                hotel.removeRoom(roomName);
                System.out.println("Room removed successfully.");
                break;
            case 4:
                if (!hotel.getReservations().isEmpty()) {
                    System.out.println("Cannot update base price when there are active reservations.");
                    return;
                }
                System.out.print("Enter new base price: ");
                double newBasePrice = scanner.nextDouble();
                if (newBasePrice < 100.0) {
                    System.out.println("Base price must be >= 100.0.");
                    return;
                }
                hotel.setBasePrice(newBasePrice);
                System.out.println("Base price updated successfully.");
                break;
            case 5:
                System.out.print("Enter guest name to remove reservation: ");
                String guestName = scanner.nextLine();
                Reservation reservation = hotel.getReservations().stream().filter(r -> r.getGuestName().equals(guestName)).findFirst().orElse(null);
                if (reservation == null) {
                    System.out.println("Reservation not found.");
                    return;
                }
                hotel.removeReservation(reservation);
                System.out.println("Reservation removed successfully.");
                break;
            case 6:
                hotels.remove(hotel.getName());
                System.out.println("Hotel removed successfully.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Simulates the booking of a room in a hotel. It allows the user to enter a guest name,
     * select a hotel, and specify check-in and check-out dates. It checks room availability
     * and processes the booking if a room is available.
     */
    public void simulateBooking() {
        Hotel hotel = selectHotel();
        if (hotel == null) return;
        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine();
        
        System.out.print("Enter check-in date (yyyy-mm-dd): ");
        LocalDate checkInDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter check-out date (yyyy-mm-dd): ");

        LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());
        Room availableRoom = hotel.getRooms().stream().filter(room -> room.isAvailable(checkInDate.getDayOfMonth())).findFirst().orElse(null);
        if (availableRoom == null) {
            System.out.println("No available rooms for the selected dates.");
            return;
        }
        Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, availableRoom);
        hotel.addReservation(reservation);
        System.out.println("Booking successful. Total price: " + formatPrice(reservation.getTotalPrice()));
        System.out.println("Room Assigned: " + availableRoom.getName()); 

    }

    /**
     * Simulates the booking of a room in a hotel. It allows the user to enter a guest name,
     * select a hotel, and specify check-in and check-out dates. It checks room availability
     * and processes the booking if a room is available.
     */
    public Hotel selectHotel() {
        if (hotels.isEmpty()) {
            System.out.println("No hotels available.");
            return null;
        }
        System.out.println("Available hotels:");
        hotels.keySet().forEach(System.out::println);
        System.out.print("Enter hotel name: ");
        String hotelName = scanner.nextLine();
        Hotel hotel = hotels.get(hotelName);
        if (hotel == null) {
            System.out.println("Hotel not found.");
        }
        return hotel;
    }

    /**
     * Formats a price to a string with two decimal places.
     *
     * @param price The price to format.
     * @return A string representation of the price formatted to two decimal places.
     */
    public String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}
