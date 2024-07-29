import java.util.*;

/**
 * Represents a hotel in the hotel reservation system.
 * This class manages hotel properties, including rooms, reservations, and pricing.
 */
public class Hotel {

    private String name;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private double basePrice;

    /**
     * Constructs a new Hotel instance with a specified number of rooms.
     * Initializes all rooms with the same base price.
     *
     * @param name The name of the hotel.
     * @param roomCount The number of rooms to initialize in the hotel.
     */
    public Hotel(String name, int roomCount) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.basePrice = 1299.0;
    
        for (int i = 1; i <= roomCount; i++) {
            String roomNumber = String.format("Room%02d", i);
            rooms.add(new Room(name + "_" + roomNumber, basePrice));
        }
    }

    /**
     * Returns the name of the hotel.
     *
     * @return The hotel name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a list of rooms in the hotel.
     *
     * @return The list of rooms.
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Returns a list of reservations made at the hotel.
     *
     * @return The list of reservations.
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Sets the name of the hotel.
     *
     * @param name The new name of the hotel.
     */
    public void setName(String name) {
        this.name = name;
        for (int i = 0; i < rooms.size(); i++) {
            String roomNumber = String.format("Room%02d", i + 1);
            rooms.get(i).setName(name + "_" + roomNumber);
        }
    }

    /**
     * Returns the base price for rooms in the hotel.
     *
     * @return The base price per night.
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the base price for all rooms in the hotel and updates existing rooms to this new price.
     *
     * @param basePrice The new base price per night.
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
        for (Room room : rooms) {
            room.setPricePerNight(basePrice);
        }
    }

    /**
     * Adds a new room to the hotel with the given name and the hotel's current base price.
     *
     * @param roomName The name of the new room.
     */
    public void addRoom(String roomName) {
        if (rooms.size() >= 50) {
            System.out.println("Cannot add more than 50 rooms.");
            return;
        }
        rooms.add(new Room(roomName, basePrice));
    }

    /**
     * Removes a room from the hotel based on the room name, only if it is available.
     *
     * @param roomName The name of the room to remove.
     */
    public void removeRoom(String roomName) {
        rooms.removeIf(room -> room.getName().equals(roomName) && room.isAvailable(1));
    }

    /**
     * Adds a reservation to the hotel.
     *
     * @param reservation The reservation to add.
     */
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Removes a reservation from the hotel and cancels the booking.
     *
     * @param reservation The reservation to remove.
     */
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.cancel();
    }

    /**
     * Calculates the estimated earnings from all reservations.
     *
     * @return The estimated total earnings.
     */
    public double getEstimatedEarnings() {
        return reservations.stream().mapToDouble(Reservation::getTotalPrice).sum();
    }

    /**
     * Counts the number of available rooms in the hotel on a specific day.
     *
     * @param day The day of the month for which to check room availability.
     * @return The count of available rooms.
     */
    public int getAvailableRoomsCount(int day) {
        return (int) rooms.stream().filter(room -> room.isAvailable(day)).count();
    }

    /**
     * Counts the number of booked rooms in the hotel on a specific day.
     *
     * @param day The day of the month for which to check room bookings.
     * @return The count of booked rooms.
     */
    public int getBookedRoomsCount(int day) {
        return (int) rooms.stream().filter(room -> !room.isAvailable(day)).count();
    }
}

