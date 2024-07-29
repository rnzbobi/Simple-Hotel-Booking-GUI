import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HotelReservationController {
    private Hotel hotel;
    private HotelReservationSystemGUI view;

    public HotelReservationController(Hotel hotel, HotelReservationSystemGUI view) {
        this.hotel = hotel;
        this.view = view;

        view.setRoomNames(hotel.getRooms().stream().map(Room::getName).toArray(String[]::new));

        view.addReserveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guestName = view.getGuestName();
                String roomName = view.getSelectedRoomName();
                String checkInDateStr = view.getCheckInDate();
                String checkOutDateStr = view.getCheckOutDate();
                String discountCode = view.getDiscountCode();

                LocalDate checkInDate;
                LocalDate checkOutDate;

                try {
                    checkInDate = LocalDate.parse(checkInDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                    checkOutDate = LocalDate.parse(checkOutDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-mm-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                reserveRoom(roomName, guestName, checkInDate, checkOutDate, discountCode);
            }
        });
    }

    public void reserveRoom(String roomName, String guestName, LocalDate checkInDate, LocalDate checkOutDate, String discountCode) {
        for (Room room : hotel.getRooms()) {
            if (room.getName().equals(roomName) && room.isAvailable(checkInDate.getDayOfMonth())) {
                Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, room, discountCode);
                hotel.addReservation(reservation);
                JOptionPane.showMessageDialog(null, "Room " + roomName + " reserved for " + guestName + ". Total price: " + reservation.getTotalPrice());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Room " + roomName + " is not available.");
    }
}
