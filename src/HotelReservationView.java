import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HotelReservationView {
    private JFrame frame;
    private JTextField guestNameField;
    private JComboBox<String> roomNameComboBox;
    private JButton reserveButton;

    public HotelReservationView() {
        frame = new JFrame("Hotel Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        panel.add(guestNameField);

        panel.add(new JLabel("Room Name:"));
        roomNameComboBox = new JComboBox<>();
        panel.add(roomNameComboBox);

        reserveButton = new JButton("Reserve Room");
        panel.add(reserveButton);

        frame.add(panel);
    }

    public void display() {
        frame.setVisible(true);
    }

    public String getGuestName() {
        return guestNameField.getText();
    }

    public String getSelectedRoomName() {
        return (String) roomNameComboBox.getSelectedItem();
    }

    public void addReserveButtonListener(ActionListener listener) {
        reserveButton.addActionListener(listener);
    }

    public void setRoomNames(String[] roomNames) {
        for (String roomName : roomNames) {
            roomNameComboBox.addItem(roomName);
        }
    }
}
