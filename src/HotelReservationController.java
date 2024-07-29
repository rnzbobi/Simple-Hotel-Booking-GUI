import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class HotelReservationController {
    private Hotel hotel;
    private HotelReservationView view;

    public HotelReservationController(Hotel hotel, HotelReservationView view) {
        this.hotel = hotel;
        this.view = view;

        view.setRoomNames(hotel.getRooms().stream().map(Room::getName).toArray(String[]::new));

        view.addReserveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guestName = view.getGuestName();
                String roomName = view.getSelectedRoomName();
                reserveRoom(roomName, guestName);
            }
        });
    }

    public void reserveRoom(String roomName, String guestName) {
        for (Room room : hotel.getRooms()) {
            if (room.getName().equals(roomName) && room.isAvailable(1)) {
                // Assume check-in and check-out dates for simplicity
                LocalDate checkInDate = LocalDate.now();
                LocalDate checkOutDate = checkInDate.plusDays(1);
                Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, room, "");
                hotel.addReservation(reservation);
                JOptionPane.showMessageDialog(null, "Room " + roomName + " reserved for " + guestName);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Room " + roomName + " is not available.");
    }
}
