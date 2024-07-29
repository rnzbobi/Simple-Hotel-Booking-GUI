import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room room;
    private double totalPrice;

    public Reservation(String guestName, LocalDate checkInDate, LocalDate checkOutDate, Room room) {
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.totalPrice = calculateTotalPrice();
        room.book(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth());
    }

    private double calculateTotalPrice() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return days * room.getPricePerNight();
    }

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Room getRoom() {
        return room;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void cancel() {
        room.cancelBooking(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth());
    }
}
