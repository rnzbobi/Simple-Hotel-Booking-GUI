import java.util.*;

public class Hotel {

    private String name;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private double basePrice;

    public Hotel(String name, int roomCount, double basePrice) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setName(String name) {
        this.name = name;
        for (Room room : rooms) {
            String roomNumber = room.getName().split("_")[1];
            room.setName(name + "_" + roomNumber);
        }
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
        for (Room room : rooms) {
            room.setPricePerNight(basePrice);
        }
    }

    public void addRoom(String roomName, Room.RoomType type) {
        if (rooms.size() >= 50) {
            System.out.println("Cannot add more than 50 rooms.");
            return;
        }
        rooms.add(new Room(roomName, type, basePrice));
    }

    public void removeRoom(String roomName) {
        rooms.removeIf(room -> room.getName().equals(roomName) && room.isAvailable(1, 2));  // Check availability for first day as a placeholder
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.cancel();
    }

    public double getEstimatedEarnings() {
        return reservations.stream().mapToDouble(Reservation::getTotalPrice).sum();
    }

    public int getAvailableRoomsCount(int day) {
        return (int) rooms.stream().filter(room -> room.isAvailable(day)).count();
    }

    public int getBookedRoomsCount(int day) {
        return (int) rooms.stream().filter(room -> !room.isAvailable(day)).count();
    }
}
