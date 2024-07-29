public class Driver {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Grand Hotel", 50, 100.0);

        // Add some rooms
        hotel.addRoom("101", Room.RoomType.STANDARD);
        hotel.addRoom("102", Room.RoomType.DELUXE);
        hotel.addRoom("103", Room.RoomType.EXECUTIVE);

        HotelReservationView view = new HotelReservationView();
        HotelReservationController controller = new HotelReservationController(hotel, view);

        view.display();
    }
}
