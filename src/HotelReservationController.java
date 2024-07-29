import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class HotelReservationController {
    private Map<String, Hotel> hotels;
    private HotelReservationSystemGUI view;

    public HotelReservationController(HotelReservationSystemGUI view) {
        this.hotels = new HashMap<>();
        this.view = view;

        view.addCreateHotelListener(new CreateHotelListener());
        view.addViewHotelListener(new ViewHotelListener());
        view.addManageHotelListener(new ManageHotelListener());
        view.addSimulateBookingListener(new SimulateBookingListener());
        view.addExitListener(new ExitListener());
    }

    private class CreateHotelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String hotelName = JOptionPane.showInputDialog("Enter hotel name:");
            if (hotelName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            if (hotels.containsKey(hotelName)) {
                view.setOutputText("Hotel name must be unique.");
                return;
            }

            int roomCount;
            try {
                String roomCountStr = JOptionPane.showInputDialog("Enter number of rooms (1-50):");
                if (roomCountStr == null) {
                    view.setOutputText("Action Canceled");
                    return;
                }
                roomCount = Integer.parseInt(roomCountStr);
                if (roomCount < 1 || roomCount > 50) {
                    view.setOutputText("Invalid number of rooms.");
                    return;
                }
            } catch (NumberFormatException ex) {
                view.setOutputText("Invalid number format.");
                return;
            }

            Hotel hotel = new Hotel(hotelName, roomCount, 1299.0);
            for (int i = 1; i <= roomCount; i++) {
                String roomNumber = "Room" + i;
                hotel.addRoom(hotelName + "_" + roomNumber, Room.RoomType.STANDARD);  // Default type
            }

            hotels.put(hotelName, hotel);
            view.setOutputText("Hotel created successfully.");
        }
    }

    private class ViewHotelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (hotels.isEmpty()) {
                view.setOutputText("No hotels available.");
                return;
            }
            String hotelName = JOptionPane.showInputDialog("Enter hotel name:");
            if (hotelName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Hotel hotel = hotels.get(hotelName);
            if (hotel == null) {
                view.setOutputText("Hotel not found.");
                return;
            }

            String[] viewOptions = {"High-level hotel information", "Room availability", "Room details", "Reservation details"};
            String selection = (String) JOptionPane.showInputDialog(null, "Select an option:", "View Hotel", JOptionPane.QUESTION_MESSAGE, null, viewOptions, viewOptions[0]);
            if (selection == null) {
                view.setOutputText("Action Canceled");
                return;
            }

            switch (selection) {
                case "High-level hotel information":
                    viewHighLevelHotelInfo(hotel);
                    break;
                case "Room availability":
                    viewRoomAvailability(hotel);
                    break;
                case "Room details":
                    viewRoomDetails(hotel);
                    break;
                case "Reservation details":
                    viewReservationDetails(hotel);
                    break;
                default:
                    view.setOutputText("Invalid option.");
            }
        }

        private void viewHighLevelHotelInfo(Hotel hotel) {
            StringBuilder sb = new StringBuilder();
            sb.append("Hotel: ").append(hotel.getName()).append("\n");
            sb.append("Total number of rooms: ").append(hotel.getRooms().size()).append("\n");
            sb.append("Estimated earnings for the month: ").append(hotel.getEstimatedEarnings()).append("\n");

            view.setOutputText(sb.toString());
        }

        private void viewRoomAvailability(Hotel hotel) {
            LocalDate checkInDate = parseDate(JOptionPane.showInputDialog("Enter check-in date (yyyy-mm-dd):"));
            if (checkInDate == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            LocalDate checkOutDate = parseDate(JOptionPane.showInputDialog("Enter check-out date (yyyy-mm-dd):"));
            if (checkOutDate == null) {
                view.setOutputText("Action Canceled");
                return;
            }

            int availableRooms = 0;
            int bookedRooms = 0;
            for (Room room : hotel.getRooms()) {
                if (room.isAvailable(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth())) {
                    availableRooms++;
                } else {
                    bookedRooms++;
                }
            }

            view.setOutputText("Available rooms: " + availableRooms + "\nBooked rooms: " + bookedRooms);
        }

        private void viewRoomDetails(Hotel hotel) {
            String roomName = JOptionPane.showInputDialog("Enter room name:");
            if (roomName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Room room = hotel.getRooms().stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
            if (room == null) {
                view.setOutputText("Room not found.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Room: ").append(room.getName()).append("\n");
            sb.append("Type: ").append(room.getType()).append("\n");
            sb.append("Price per night: ").append(room.getPricePerNight()).append("\n");
            sb.append("Availability:\n");
            for (int day = 1; day <= 30; day++) {
                sb.append(day).append(": ").append(room.isAvailable(day) ? "Available" : "Booked").append("\n");
            }
            view.setOutputText(sb.toString());
        }

        private void viewReservationDetails(Hotel hotel) {
            String guestName = JOptionPane.showInputDialog("Enter guest name:");
            if (guestName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Reservation reservation = hotel.getReservations().stream().filter(r -> r.getGuestName().equals(guestName)).findFirst().orElse(null);
            if (reservation == null) {
                view.setOutputText("Reservation not found.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Guest: ").append(reservation.getGuestName()).append("\n");
            sb.append("Check-in date: ").append(reservation.getCheckInDate()).append("\n");
            sb.append("Check-out date: ").append(reservation.getCheckOutDate()).append("\n");
            sb.append("Room: ").append(reservation.getRoom().getName()).append("\n");
            sb.append("Type: ").append(reservation.getRoom().getType()).append("\n");
            sb.append("Total price: ").append(reservation.getTotalPrice()).append("\n");
            view.setOutputText(sb.toString());
        }

        private LocalDate parseDate(String date) {
            try {
                return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }

    private class ManageHotelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (hotels.isEmpty()) {
                view.setOutputText("No hotels available.");
                return;
            }
            String hotelName = JOptionPane.showInputDialog("Enter hotel name:");
            if (hotelName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Hotel hotel = hotels.get(hotelName);
            if (hotel == null) {
                view.setOutputText("Hotel not found.");
                return;
            }

            String[] options = {"Change hotel name", "Add room(s)", "Remove room(s)", "Update base price for a room", "Set date price modifier", "Remove reservation", "Remove hotel"};
            String selection = (String) JOptionPane.showInputDialog(null, "Select an option:", "Manage Hotel", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (selection == null) {
                view.setOutputText("Action Canceled");
                return;
            }

            switch (selection) {
                case "Change hotel name":
                    String newName = JOptionPane.showInputDialog("Enter new hotel name:");
                    if (newName == null) {
                        view.setOutputText("Action Canceled");
                        return;
                    }
                    if (hotels.containsKey(newName)) {
                        view.setOutputText("Hotel name must be unique.");
                        return;
                    }
                    hotels.remove(hotel.getName());
                    hotel.setName(newName);
                    hotels.put(newName, hotel);
                    view.setOutputText("Hotel name updated successfully.");
                    break;
                case "Add room(s)":
                    String roomName = JOptionPane.showInputDialog("Enter room name:");
                    if (roomName == null) {
                        view.setOutputText("Action Canceled");
                        return;
                    }
                    String[] roomTypes = {"STANDARD", "DELUXE", "EXECUTIVE"};
                    String selectedType = (String) JOptionPane.showInputDialog(null, "Select room type:", "Room Type", JOptionPane.QUESTION_MESSAGE, null, roomTypes, roomTypes[0]);
                    if (selectedType == null) {
                        view.setOutputText("Action Canceled");
                        return;
                    }
                    hotel.addRoom(roomName, Room.RoomType.valueOf(selectedType));
                    view.setOutputText("Room added successfully.");
                    break;
                case "Remove room(s)":
                    String roomNameToRemove = JOptionPane.showInputDialog("Enter room name to remove:");
                    if (roomNameToRemove == null) {
                        view.setOutputText("Action Canceled");
                        return;
                    }
                    hotel.removeRoom(roomNameToRemove);
                    view.setOutputText("Room removed successfully.");
                    break;
                case "Update base price for a room":
                    String newBasePriceStr = JOptionPane.showInputDialog("Enter new base price:");
                    if (newBasePriceStr == null) {
                        view.setOutputText("Action Canceled");
                        return;
                    }
                    double newBasePrice;
                    try {
                        newBasePrice = Double.parseDouble(newBasePriceStr);
                    } catch (NumberFormatException ex) {
                        view.setOutputText("Invalid price format.");
                        return;
                    }
                    hotel.setBasePrice(newBasePrice);
                    view.setOutputText("Base price updated successfully.");
                    break;
                case "Set date price modifier":
                    setDatePriceModifier(hotel);
                    break;
                case "Remove reservation":
                    String guestName = JOptionPane.showInputDialog("Enter guest name to remove reservation:");
                    if (guestName == null) {
                        view.setOutputText("Action Canceled");
                        return;
                    }
                    Reservation reservation = hotel.getReservations().stream().filter(r -> r.getGuestName().equals(guestName)).findFirst().orElse(null);
                    if (reservation != null) {
                        hotel.removeReservation(reservation);
                        view.setOutputText("Reservation removed successfully.");
                    } else {
                        view.setOutputText("Reservation not found.");
                    }
                    break;
                case "Remove hotel":
                    hotels.remove(hotel.getName());
                    view.setOutputText("Hotel removed successfully.");
                    break;
                default:
                    view.setOutputText("Invalid option.");
            }
        }

        private void setDatePriceModifier(Hotel hotel) {
            String roomName = JOptionPane.showInputDialog("Enter room name:");
            if (roomName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Room room = hotel.getRooms().stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
            if (room == null) {
                view.setOutputText("Room not found.");
                return;
            }
            LocalDate startDate = LocalDate.parse(JOptionPane.showInputDialog("Enter start date (yyyy-mm-dd):"));
            if (startDate == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            LocalDate endDate = LocalDate.parse(JOptionPane.showInputDialog("Enter end date (yyyy-mm-dd):"));
            if (endDate == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            String modifierStr = JOptionPane.showInputDialog("Enter price modifier (0.5 [50%] to 1.5 [150%]):");
            if (modifierStr == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            double modifier;
            try {
                modifier = Double.parseDouble(modifierStr);
            } catch (NumberFormatException ex) {
                view.setOutputText("Invalid modifier format.");
                return;
            }

            try {
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    room.setDatePriceModifier(date.getDayOfMonth(), modifier);
                }
                view.setOutputText("Date price modifier set successfully.");
            } catch (IllegalArgumentException ex) {
                view.setOutputText("Error setting date price modifier: " + ex.getMessage());
            }
        }
    }

    private class SimulateBookingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (hotels.isEmpty()) {
                view.setOutputText("No hotels available.");
                return;
            }
            String hotelName = JOptionPane.showInputDialog("Enter hotel name:");
            if (hotelName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Hotel hotel = hotels.get(hotelName);
            if (hotel == null) {
                view.setOutputText("Hotel not found.");
                return;
            }

            String guestName = JOptionPane.showInputDialog("Enter guest name:");
            if (guestName == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter check-in date (yyyy-mm-dd):"));
            if (checkInDate == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            LocalDate checkOutDate = LocalDate.parse(JOptionPane.showInputDialog("Enter check-out date (yyyy-mm-dd):"));
            if (checkOutDate == null) {
                view.setOutputText("Action Canceled");
                return;
            }

            String[] roomTypes = {"STANDARD", "DELUXE", "EXECUTIVE"};
            String selectedType = (String) JOptionPane.showInputDialog(null, "Select room type:", "Room Type", JOptionPane.QUESTION_MESSAGE, null, roomTypes, roomTypes[0]);
            if (selectedType == null) {
                view.setOutputText("Action Canceled");
                return;
            }
            Room.RoomType roomType = Room.RoomType.valueOf(selectedType);

            String discountCode = JOptionPane.showInputDialog("Enter discount code (if any):");
            if (discountCode == null) {
                view.setOutputText("Action Canceled");
                return;
            }

            Room availableRoom = hotel.getRooms().stream().filter(room -> room.isAvailable(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth())).findFirst().orElse(null);
            if (availableRoom == null) {
                view.setOutputText("No available rooms for the selected dates.");
                return;
            }

            availableRoom.setType(roomType);

            Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, availableRoom, discountCode);
            hotel.addReservation(reservation);
            view.setOutputText("Booking successful. Total price: " + reservation.getTotalPrice() + "\nRoom Assigned: " + availableRoom.getName() + " (" + availableRoom.getType() + ")");
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
