/**
 * The Driver class serves as the entry point for the Hotel Reservation System application.
 * It contains the main method that creates an instance of the HotelReservationSystem
 * and starts the application.
 */
public class Driver {
    
    /**
     * The main method is the entry point for the Java application.
     * It creates an instance of the HotelReservationSystem and calls the run method to start the system.
     *
     * @param args The command-line arguments, not used in this application.
     */
    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        system.run();
    }
}
