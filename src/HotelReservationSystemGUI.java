import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HotelReservationSystemGUI {

    private Map<String, Hotel> hotels = new HashMap<>();
    private JFrame frame;
    private JTextField guestNameField;
    private JComboBox<String> roomNameComboBox;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField discountCodeField;
    private JButton reserveButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HotelReservationSystemGUI().createAndShowGUI();
        });
    }

    public HotelReservationSystemGUI() {
        guestNameField = new JTextField();
        roomNameComboBox = new JComboBox<>();
        checkInField = new JTextField();
        checkOutField = new JTextField();
        discountCodeField = new JTextField();
        reserveButton = new JButton("Reserve Room");
    }

    public void createAndShowGUI() {
        frame = new JFrame("Hotel Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());

        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton createHotelButton = new JButton("Create Hotel");
        JButton viewHotelButton = new JButton("View Hotel");
        JButton manageHotelButton = new JButton("Manage Hotel");
        JButton simulateBookingButton = new JButton("Simulate Booking");
        JButton exitButton = new JButton("Exit");

        createHotelButton.addActionListener(e -> showCreateHotelPanel(mainPanel));
        viewHotelButton.addActionListener(e -> showViewHotelPanel(mainPanel));
        manageHotelButton.addActionListener(e -> showManageHotelPanel(mainPanel));
        simulateBookingButton.addActionListener(e -> showSimulateBookingPanel(mainPanel));
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(createHotelButton);
        menuPanel.add(viewHotelButton);
        menuPanel.add(manageHotelButton);
        menuPanel.add(simulateBookingButton);
        menuPanel.add(exitButton);

        mainPanel.add(menuPanel, "Menu");

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "Menu");
    }

    private void showCreateHotelPanel(JPanel mainPanel) {
        JPanel createHotelPanel = new JPanel(new GridLayout(4, 2));
        JLabel nameLabel = new JLabel("Enter hotel name:");
        JTextField nameField = new JTextField();
        JLabel roomCountLabel = new JLabel("Enter number of rooms (1-50):");
        JTextField roomCountField = new JTextField();
        JButton createButton = new JButton("Create");
        JButton backButton = new JButton("Back");

        createButton.addActionListener(e -> {
            String name = nameField.getText();
            int roomCount = Integer.parseInt(roomCountField.getText());
            if (hotels.containsKey(name)) {
                JOptionPane.showMessageDialog(frame, "Hotel name must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (roomCount < 1 || roomCount > 50) {
                JOptionPane.showMessageDialog(frame, "Invalid number of rooms.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Hotel hotel = new Hotel(name, roomCount, 1299.0);

            for (int i = 1; i <= roomCount; i++) {
                String roomNumber = String.format("Room%02d", i);
                hotel.addRoom(name + "_" + roomNumber, Room.RoomType.STANDARD);  // Default type
            }

            hotels.put(name, hotel);
            JOptionPane.showMessageDialog(frame, "Hotel created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Menu");
        });

        createHotelPanel.add(nameLabel);
        createHotelPanel.add(nameField);
        createHotelPanel.add(roomCountLabel);
        createHotelPanel.add(roomCountField);
        createHotelPanel.add(createButton);
        createHotelPanel.add(backButton);

        mainPanel.add(createHotelPanel, "CreateHotel");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "CreateHotel");
    }

    private void showViewHotelPanel(JPanel mainPanel) {
        JPanel viewHotelPanel = new JPanel(new BorderLayout());
        JComboBox<String> hotelSelector = new JComboBox<>(hotels.keySet().toArray(new String[0]));
        JTextArea hotelInfoArea = new JTextArea();
        hotelInfoArea.setEditable(false);
        JButton backButton = new JButton("Back");

        hotelSelector.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName != null) {
                Hotel hotel = hotels.get(selectedHotelName);
                hotelInfoArea.setText(getHotelInfo(hotel));
            }
        });

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Menu");
        });

        viewHotelPanel.add(hotelSelector, BorderLayout.NORTH);
        viewHotelPanel.add(new JScrollPane(hotelInfoArea), BorderLayout.CENTER);
        viewHotelPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(viewHotelPanel, "ViewHotel");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "ViewHotel");
    }

    private String getHotelInfo(Hotel hotel) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hotel: ").append(hotel.getName()).append("\n");
        sb.append("Total number of rooms: ").append(hotel.getRooms().size()).append("\n");
        sb.append("Estimated earnings for the month: ").append(formatPrice(hotel.getEstimatedEarnings())).append("\n\n");
        for (Room room : hotel.getRooms()) {
            sb.append("Room: ").append(room.getName()).append("\n");
            sb.append("Type: ").append(room.getType()).append("\n");
            sb.append("Price per night: ").append(formatPrice(room.getPricePerNight())).append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    private void showManageHotelPanel(JPanel mainPanel) {
        JPanel manageHotelPanel = new JPanel(new BorderLayout());
        JComboBox<String> hotelSelector = new JComboBox<>(hotels.keySet().toArray(new String[0]));
        JTextArea manageInfoArea = new JTextArea();
        manageInfoArea.setEditable(false);
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1));
        JButton changeNameButton = new JButton("Change Hotel Name");
        JButton addRoomButton = new JButton("Add Room");
        JButton removeRoomButton = new JButton("Remove Room");
        JButton updateBasePriceButton = new JButton("Update Base Price");
        JButton setDatePriceModifierButton = new JButton("Set Date Price Modifier");
        JButton removeReservationButton = new JButton("Remove Reservation");
        JButton removeHotelButton = new JButton("Remove Hotel");

        hotelSelector.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName != null) {
                Hotel hotel = hotels.get(selectedHotelName);
                manageInfoArea.setText(getHotelInfo(hotel));
            }
        });

        changeNameButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            Hotel hotel = hotels.get(selectedHotelName);
            String newName = JOptionPane.showInputDialog(frame, "Enter new hotel name:");
            if (newName != null && !newName.trim().isEmpty() && !hotels.containsKey(newName)) {
                hotels.remove(hotel.getName());
                hotel.setName(newName);
                hotels.put(newName, hotel);
                hotelSelector.addItem(newName);
                hotelSelector.removeItem(selectedHotelName);
                JOptionPane.showMessageDialog(frame, "Hotel name updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid name or name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addRoomButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            Hotel hotel = hotels.get(selectedHotelName);
            String roomName = JOptionPane.showInputDialog(frame, "Enter room name:");
            Room.RoomType type = selectRoomType();
            hotel.addRoom(roomName, type);
            JOptionPane.showMessageDialog(frame, "Room added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        removeRoomButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            Hotel hotel = hotels.get(selectedHotelName);
            String roomName = JOptionPane.showInputDialog(frame, "Enter room name to remove:");
            hotel.removeRoom(roomName);
            JOptionPane.showMessageDialog(frame, "Room removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        updateBasePriceButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            Hotel hotel = hotels.get(selectedHotelName);
            if (!hotel.getReservations().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Cannot update base price when there are active reservations.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String newBasePriceStr = JOptionPane.showInputDialog(frame, "Enter new base price:");
            double newBasePrice = Double.parseDouble(newBasePriceStr);
            if (newBasePrice >= 100.0) {
                hotel.setBasePrice(newBasePrice);
                JOptionPane.showMessageDialog(frame, "Base price updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Base price must be >= 100.0.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setDatePriceModifierButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            Hotel hotel = hotels.get(selectedHotelName);
            String roomName = JOptionPane.showInputDialog(frame, "Enter room name:");
            Room room = hotel.getRooms().stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
            if (room == null) {
                JOptionPane.showMessageDialog(frame, "Room not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String startDateStr = JOptionPane.showInputDialog(frame, "Enter start date (yyyy-mm-dd):");
            String endDateStr = JOptionPane.showInputDialog(frame, "Enter end date (yyyy-mm-dd):");
            String modifierStr = JOptionPane.showInputDialog(frame, "Enter price modifier (0.5 to 1.5):");
            
            try {
                LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                double modifier = Double.parseDouble(modifierStr);
                
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    room.setDatePriceModifier(date.getDayOfMonth(), modifier);
                }
                JOptionPane.showMessageDialog(frame, "Date price modifier set successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeReservationButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            Hotel hotel = hotels.get(selectedHotelName);
            String guestName = JOptionPane.showInputDialog(frame, "Enter guest name to remove reservation:");
            Reservation reservation = hotel.getReservations().stream().filter(r -> r.getGuestName().equals(guestName)).findFirst().orElse(null);
            if (reservation != null) {
                hotel.removeReservation(reservation);
                JOptionPane.showMessageDialog(frame, "Reservation removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Reservation not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeHotelButton.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName == null) return;
            hotels.remove(selectedHotelName);
            hotelSelector.removeItem(selectedHotelName);
            JOptionPane.showMessageDialog(frame, "Hotel removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Menu");
        });

        buttonPanel.add(changeNameButton);
        buttonPanel.add(addRoomButton);
        buttonPanel.add(removeRoomButton);
        buttonPanel.add(updateBasePriceButton);
        buttonPanel.add(setDatePriceModifierButton);
        buttonPanel.add(removeReservationButton);
        buttonPanel.add(removeHotelButton);

        manageHotelPanel.add(hotelSelector, BorderLayout.NORTH);
        manageHotelPanel.add(new JScrollPane(manageInfoArea), BorderLayout.CENTER);
        manageHotelPanel.add(buttonPanel, BorderLayout.EAST);
        manageHotelPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(manageHotelPanel, "ManageHotel");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "ManageHotel");
    }

    private void showSimulateBookingPanel(JPanel mainPanel) {
        JPanel simulateBookingPanel = new JPanel(new BorderLayout());
        JComboBox<String> hotelSelector = new JComboBox<>(hotels.keySet().toArray(new String[0]));
        JTextArea bookingInfoArea = new JTextArea();
        bookingInfoArea.setEditable(false);
        JButton backButton = new JButton("Back");

        hotelSelector.addActionListener(e -> {
            String selectedHotelName = (String) hotelSelector.getSelectedItem();
            if (selectedHotelName != null) {
                Hotel hotel = hotels.get(selectedHotelName);
                bookingInfoArea.setText(getHotelInfo(hotel));
            }
        });

        JPanel bookingPanel = new JPanel(new GridLayout(7, 2));
        JLabel guestNameLabel = new JLabel("Guest Name:");
        guestNameField = new JTextField();
        JLabel checkInLabel = new JLabel("Check-in Date (yyyy-mm-dd):");
        checkInField = new JTextField();
        JLabel checkOutLabel = new JLabel("Check-out Date (yyyy-mm-dd):");
        checkOutField = new JTextField();
        JLabel discountCodeLabel = new JLabel("Discount Code:");
        discountCodeField = new JTextField();
        reserveButton = new JButton("Book");

        bookingPanel.add(guestNameLabel);
        bookingPanel.add(guestNameField);
        bookingPanel.add(checkInLabel);
        bookingPanel.add(checkInField);
        bookingPanel.add(checkOutLabel);
        bookingPanel.add(checkOutField);
        bookingPanel.add(discountCodeLabel);
        bookingPanel.add(discountCodeField);
        bookingPanel.add(reserveButton);

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Menu");
        });

        simulateBookingPanel.add(hotelSelector, BorderLayout.NORTH);
        simulateBookingPanel.add(new JScrollPane(bookingInfoArea), BorderLayout.CENTER);
        simulateBookingPanel.add(bookingPanel, BorderLayout.EAST);
        simulateBookingPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(simulateBookingPanel, "SimulateBooking");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "SimulateBooking");
    }

    public void setRoomNames(String[] roomNames) {
        for (String roomName : roomNames) {
            roomNameComboBox.addItem(roomName);
        }
    }

    public String getGuestName() {
        return guestNameField.getText();
    }

    public String getCheckInDate() {
        return checkInField.getText();
    }

    public String getCheckOutDate() {
        return checkOutField.getText();
    }

    public String getDiscountCode() {
        return discountCodeField.getText();
    }

    public String getSelectedRoomName() {
        return (String) roomNameComboBox.getSelectedItem();
    }

    public void addReserveButtonListener(ActionListener listener) {
        reserveButton.addActionListener(listener);
    }

    private Room.RoomType selectRoomType() {
        String[] roomTypes = { "STANDARD", "DELUXE", "EXECUTIVE" };
        String selectedType = (String) JOptionPane.showInputDialog(frame, "Select room type:", "Room Type", JOptionPane.QUESTION_MESSAGE, null, roomTypes, roomTypes[0]);
        switch (selectedType) {
            case "DELUXE":
                return Room.RoomType.DELUXE;
            case "EXECUTIVE":
                return Room.RoomType.EXECUTIVE;
            default:
                return Room.RoomType.STANDARD;
        }
    }

    public String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}
