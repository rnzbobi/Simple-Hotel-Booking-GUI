import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room room;
    private double totalPrice;
    private String discountCode;

    public Reservation(String guestName, LocalDate checkInDate, LocalDate checkOutDate, Room room, String discountCode) {
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.discountCode = discountCode;
        this.totalPrice = calculateTotalPrice();
        room.book(checkInDate.getDayOfMonth(), checkOutDate.getDayOfMonth());
    }

    private double calculateTotalPrice() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = days * room.getPricePerNight();
        double discount = 0.0;

        switch (discountCode) {
            case "I_WORK_HERE":
                discount = basePrice * 0.10;
                break;
            case "STAY4_GET1":
                if (days >= 5) {
                    discount = room.getPricePerNight();
                }
                break;
            case "PAYDAY":
                if ((checkInDate.getDayOfMonth() == 15 || checkInDate.getDayOfMonth() == 30) || 
                    (checkOutDate.getDayOfMonth() == 15 || checkOutDate.getDayOfMonth() == 30)) {
                    discount = basePrice * 0.07;
                }
                break;
        }
        return basePrice - discount;
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
