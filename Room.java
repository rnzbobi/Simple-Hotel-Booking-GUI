import java.util.*;


/**
 * Represents a room within a hotel in the hotel reservation system.
 * It holds details about the room's name, price per night, and availability status for each day of the month.
 */
public class Room {

    private String name;
    private double pricePerNight;
    private boolean[] availability;
    
    /**
     * Constructs a new Room instance with a specified name and nightly price.
     * Initializes availability for a month, assuming a month of 31 days.
     *
     * @param name The name of the room.
     * @param pricePerNight The nightly rate for the room.
     */
    public Room(String name, double pricePerNight) {
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.availability = new boolean[31];
        Arrays.fill(this.availability, true);
    }

    /**
     * Returns the name of the room.
     *
     * @return The name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the nightly rate for the room.
     *
     * @return The price per night.
     */
    public double getPricePerNight() {
        return pricePerNight;
    }

    /**
     * Sets the nightly rate for the room.
     *
     * @param pricePerNight The new nightly rate to be set.
     */
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    /**
     * Checks if the room is available on a specific day.
     *
     * @param day The day of the month to check for availability.
     * @return true if the room is available, false otherwise.
     */
    public boolean isAvailable(int day) {
        return availability[day - 1];
    }

    /**
     * Books the room for a specified range of days.
     * Marks the room as unavailable for the days between check-in and check-out.
     *
     * @param checkInDay The day of the month when the check-in occurs.
     * @param checkOutDay The day of the month following the last night of stay.
     */
    public void book(int checkInDay, int checkOutDay) {
        for (int i = checkInDay; i < checkOutDay; i++) {
            availability[i - 1] = false;
        }
    }

    /**
     * Cancels the booking of the room for a specified range of days.
     * Marks the room as available for the days between check-in and check-out.
     *
     * @param checkInDay The day of the month when the check-in occurs.
     * @param checkOutDay The day of the month following the last night of stay.
     */
    public void cancelBooking(int checkInDay, int checkOutDay) {
        for (int i = checkInDay; i < checkOutDay; i++) {
            availability[i - 1] = true;
        }
    }

    public void setName(String name){
        this.name = name;
    }
     
}