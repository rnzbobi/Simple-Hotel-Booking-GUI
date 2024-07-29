import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a reservation made by a guest in the hotel reservation system.
 * It holds details about the guest, the room booked, and the duration of the stay.
 */
public class Reservation {

    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room room;
    private double totalPrice;


    /**
     * Constructs a new Reservation instance.
     * Automatically calculates the total price based on the duration of stay and books the room.
     *
     * @param guestName The name of the guest making the reservation.
     * @param checkInDate The check-in date of the reservation.
     * @param checkOutDate The check-out date of the reservation.
     * @param room The room being reserved.
     */
    public Reservation(String guestName, LocalDate checkInDate, LocalDate checkOutDate, Room room) {
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.totalPrice = calculateTotalPrice();
        room.book(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth());
    }

    /**
     * Calculates the total price for the reservation based on the number of nights stayed and the nightly rate of the room.
     *
     * @return The total price for the reservation.
     */
    private double calculateTotalPrice() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return days * room.getPricePerNight();
    }

    /**
     * Returns the guest name associated with this reservation.
     *
     * @return The guest name.
     */
    public String getGuestName() {
        return guestName;
    }

     /**
     * Returns the check-in date for the reservation.
     *
     * @return The check-in date.
     */
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    /**
     * Returns the check-out date for the reservation.
     *
     * @return The check-out date.
     */
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Returns the room associated with this reservation.
     *
     * @return The room.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns the total price for the reservation.
     *
     * @return The total price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Cancels this reservation and frees up the booked room for the specified dates.
     */
    public void cancel() {
        room.cancelBooking(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth());
    }
}
