import java.util.*;

public class Room {

    public enum RoomType {
        STANDARD,
        DELUXE,
        EXECUTIVE
    }

    private String name;
    private RoomType type;
    private double pricePerNight;
    private double basePrice; // Add basePrice to store the base price for the room
    private boolean[] availability;
    private Map<Integer, Double> datePriceModifiers; // Date price modifiers
    
    public Room(String name, RoomType type, double basePrice) {
        this.name = name;
        this.type = type;
        this.basePrice = basePrice; // Initialize basePrice
        this.availability = new boolean[31];
        this.datePriceModifiers = new HashMap<>(); // Initialize date price modifiers
        Arrays.fill(this.availability, true);
        setPricePerNight(basePrice); // Set the initial price based on the type
    }

    public String getName() {
        return name;
    }

    public RoomType getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double basePrice) {
        this.basePrice = basePrice; // Update basePrice when setting the price
        switch (type) {
            case STANDARD:
                this.pricePerNight = basePrice;
                break;
            case DELUXE:
                this.pricePerNight = basePrice * 1.2;
                break;
            case EXECUTIVE:
                this.pricePerNight = basePrice * 1.35;
                break;
        }
    }

    public void setDatePriceModifier(int day, double modifier) {
        if (day < 1 || day > 31 || modifier < 0.5 || modifier > 1.5) {
            throw new IllegalArgumentException("Invalid day or modifier");
        }
        datePriceModifiers.put(day, modifier);
    }

    public double getDatePriceModifier(int day) {
        return datePriceModifiers.getOrDefault(day, 1.0); // Default modifier is 1.0 (100%)
    }

    public boolean isAvailable(int day) {
        return availability[day - 1];
    }

    public boolean isAvailable(int checkInDay, int checkOutDay) {
        for (int i = checkInDay; i < checkOutDay; i++) {
            if (!availability[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public void book(int checkInDay, int checkOutDay) {
        for (int i = checkInDay; i < checkOutDay; i++) {
            availability[i - 1] = false;
        }
    }

    public void cancelBooking(int checkInDay, int checkOutDay) {
        for (int i = checkInDay; i < checkOutDay; i++) {
            availability[i - 1] = true;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(RoomType type) {
        this.type = type;
        setPricePerNight(basePrice); // Update price based on the new type
    }
}
