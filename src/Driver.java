public class Driver {
    public static void main(String[] args) {
        HotelReservationSystemGUI view = new HotelReservationSystemGUI();
        HotelReservationController controller = new HotelReservationController(view);

        view.createAndShowGUI();
    }
}
