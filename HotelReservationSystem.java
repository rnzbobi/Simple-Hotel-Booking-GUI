import java.util.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HotelReservationSystem {

    private Map<String, Hotel> hotels = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

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
            
            int option = getIntInput();
            
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

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    public void createHotel() {
        System.out.print("Enter hotel name: ");
        String name = scanner.nextLine();
        if (hotels.containsKey(name)) {
            System.out.println("Hotel name must be unique.");
            return;
        }
        System.out.print("Enter number of rooms (1-50): ");
        int roomCount = getIntInput();
        if (roomCount < 1 || roomCount > 50) {
            System.out.println("Invalid number of rooms.");
            return;
        }

        Hotel hotel = new Hotel(name, roomCount, 1299.0);

        for (int i = 1; i <= roomCount; i++) {
            String roomNumber = String.format("Room%02d", i);
            hotel.addRoom(name + "_" + roomNumber, Room.RoomType.STANDARD);  // Default type
        }

        hotels.put(name, hotel);
        System.out.println("Hotel created successfully.");
    }

    public void viewHotel() {
        Hotel hotel = selectHotel();
        if (hotel == null) return;
        System.out.println("Hotel: " + hotel.getName());
        System.out.println("Total number of rooms: " + hotel.getRooms().size());
        System.out.println("Estimated earnings for the month: " + formatPrice(hotel.getEstimatedEarnings()));
        System.out.println("1. View room availability");
        System.out.println("2. View room details");
        System.out.println("3. View reservation details");
        
        int option = getIntInput();
        
        switch (option) {
            case 1:
                viewRoomAvailability(hotel);
                break;
            case 2:
                viewRoomDetails(hotel);
                break;
            case 3:
                viewReservationDetails(hotel);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void viewRoomAvailability(Hotel hotel) {
        System.out.print("Enter date (yyyy-mm-dd): ");
        LocalDate date = parseDate(scanner.nextLine());
        if (date == null) {
            System.out.println("Invalid date.");
            return;
        }
        int day = date.getDayOfMonth();
        int availableRooms = hotel.getAvailableRoomsCount(day);
        int bookedRooms = hotel.getRooms().size() - availableRooms;
        System.out.println("Available rooms: " + availableRooms);
        System.out.println("Booked rooms: " + bookedRooms);
    }

    private void viewRoomDetails(Hotel hotel) {
        System.out.print("Enter room name: ");
        String roomName = scanner.nextLine();
        Room room = hotel.getRooms().stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        System.out.print("Enter date (yyyy-mm-dd): ");
        LocalDate date = parseDate(scanner.nextLine());
        if (date == null) {
            System.out.println("Invalid date.");
            return;
        }
        int day = date.getDayOfMonth();
        System.out.println("Room: " + room.getName());
        System.out.println("Type: " + room.getType());
        System.out.println("Price per night: " + formatPrice(room.getPricePerNight()));
        System.out.println("Availability on day " + day + ": " + (room.isAvailable(day) ? "Available" : "Booked"));
    }

    private void viewReservationDetails(Hotel hotel) {
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
        System.out.println("Type: " + reservation.getRoom().getType());
        System.out.println("Total price: " + formatPrice(reservation.getTotalPrice()));
    }

    public void manageHotel() {
        Hotel hotel = selectHotel();
        if (hotel == null) return;
        System.out.println("1. Change hotel name");
        System.out.println("2. Add room(s)");
        System.out.println("3. Remove room(s)");
        System.out.println("4. Update base price for a room");
        System.out.println("5. Set date price modifier");
        System.out.println("6. Remove reservation");
        System.out.println("7. Remove hotel");
        
        int option = getIntInput();
        
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
                System.out.print("Enter room name: ");
                String roomName = scanner.nextLine();
                Room.RoomType type = selectRoomType();
                hotel.addRoom(roomName, type);
                System.out.println("Room added successfully.");
                break;
            case 3:
                System.out.print("Enter room name to remove: ");
                String roomNameToRemove = scanner.nextLine();
                hotel.removeRoom(roomNameToRemove);
                System.out.println("Room removed successfully.");
                break;
            case 4:
                if (!hotel.getReservations().isEmpty()) {
                    System.out.println("Cannot update base price when there are active reservations.");
                    return;
                }
                System.out.print("Enter new base price: ");
                double newBasePrice = getIntInput();
                if (newBasePrice < 100.0) {
                    System.out.println("Base price must be >= 100.0.");
                    return;
                }
                hotel.setBasePrice(newBasePrice);
                System.out.println("Base price updated successfully.");
                break;
            case 5:
                setDatePriceModifier(hotel);
                break;
            case 6:
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
            case 7:
                hotels.remove(hotel.getName());
                System.out.println("Hotel removed successfully.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private Room.RoomType selectRoomType() {
        System.out.println("Select room type:");
        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Executive");
        int typeOption = getIntInput();
        switch (typeOption) {
            case 1:
                return Room.RoomType.STANDARD;
            case 2:
                return Room.RoomType.DELUXE;
            case 3:
                return Room.RoomType.EXECUTIVE;
            default:
                System.out.println("Invalid option. Defaulting to Standard.");
                return Room.RoomType.STANDARD;
        }
    }    

    private void setDatePriceModifier(Hotel hotel) {
        System.out.print("Enter room name: ");
        String roomName = scanner.nextLine();
        Room room = hotel.getRooms().stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        System.out.print("Enter start date (yyyy-mm-dd): ");
        LocalDate startDate = parseDate(scanner.nextLine());
        if (startDate == null) {
            System.out.println("Invalid date.");
            return;
        }
        System.out.print("Enter end date (yyyy-mm-dd): ");
        LocalDate endDate = parseDate(scanner.nextLine());
        if (endDate == null) {
            System.out.println("Invalid date.");
            return;
        }
        System.out.print("Enter price modifier (0.5 [50%] to 1.5 [150%]): ");
        double modifier = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
    
        try {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                room.setDatePriceModifier(date.getDayOfMonth(), modifier);
            }
            System.out.println("Date price modifier set successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error setting date price modifier: " + e.getMessage());
        }
    }    

    public void simulateBooking() {
        Hotel hotel = selectHotel();
        if (hotel == null) return;
        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine();
        
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;
        
        while (checkInDate == null) {
            System.out.print("Enter check-in date (yyyy-mm-dd): ");
            checkInDate = parseDate(scanner.nextLine());
        }
        
        while (checkOutDate == null) {
            System.out.print("Enter check-out date (yyyy-mm-dd): ");
            checkOutDate = parseDate(scanner.nextLine());
        }
        
        int checkInDay = checkInDate.getDayOfMonth();
        int checkOutDay = checkOutDate.getDayOfMonth();
        
        if (checkInDay == 31 || checkOutDay == 1) {
            System.out.println("Invalid booking dates. Bookings cannot start on the 31st or end on the 1st.");
            return;
        }

        System.out.println("Select room type:");
        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Executive");
        int typeOption = getIntInput();
        Room.RoomType selectedType;
        switch (typeOption) {
            case 1:
                selectedType = Room.RoomType.STANDARD;
                break;
            case 2:
                selectedType = Room.RoomType.DELUXE;
                break;
            case 3:
                selectedType = Room.RoomType.EXECUTIVE;
                break;
            default:
                System.out.println("Invalid option. Defaulting to Standard.");
                selectedType = Room.RoomType.STANDARD;
        }
        
        Room availableRoom = hotel.getRooms().stream().filter(room -> room.isAvailable(checkInDay)).findFirst().orElse(null);
        if (availableRoom == null) {
            System.out.println("No available rooms for the selected dates.");
            return;
        }

        // Change room type
        availableRoom.setType(selectedType);

        System.out.print("Enter discount code (if any): ");
        String discountCode = scanner.nextLine().trim();

        Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, availableRoom, discountCode);
        hotel.addReservation(reservation);
        System.out.println("Booking successful. Total price: " + formatPrice(reservation.getTotalPrice()));
        System.out.println("Room Assigned: " + availableRoom.getName() + " (" + availableRoom.getType() + ")");
    }
    
    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-mm-dd.");
            return null;
        }
    }

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

    public String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}
