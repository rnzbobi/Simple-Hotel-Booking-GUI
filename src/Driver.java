public class Driver {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Grand Hotel", 50, 100.0);

        HotelReservationSystemGUI view = new HotelReservationSystemGUI();
        HotelReservationController controller = new HotelReservationController(hotel, view);

        view.createAndShowGUI();
    }
}
