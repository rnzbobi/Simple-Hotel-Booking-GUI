import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HotelReservationSystemGUI {
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField inputField;
    private JButton createHotelButton, viewHotelButton, manageHotelButton, simulateBookingButton, exitButton;

    public HotelReservationSystemGUI() {
        frame = new JFrame("Hotel Reservation System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(60, 179, 113));
        JLabel headerLabel = new JLabel("Hotel Reservation System");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        inputPanel.add(inputField, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));

        createHotelButton = new JButton("Create Hotel");
        viewHotelButton = new JButton("View Hotel");
        manageHotelButton = new JButton("Manage Hotel");
        simulateBookingButton = new JButton("Simulate Booking");
        exitButton = new JButton("Exit");

        styleButton(createHotelButton);
        styleButton(viewHotelButton);
        styleButton(manageHotelButton);
        styleButton(simulateBookingButton);
        styleButton(exitButton);

        buttonPanel.add(createHotelButton);
        buttonPanel.add(viewHotelButton);
        buttonPanel.add(manageHotelButton);
        buttonPanel.add(simulateBookingButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(60, 179, 113));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    public void createAndShowGUI() {
        frame.setVisible(true);
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInputField() {
        inputField.setText("");
    }

    public void addCreateHotelListener(ActionListener listener) {
        createHotelButton.addActionListener(listener);
    }

    public void addViewHotelListener(ActionListener listener) {
        viewHotelButton.addActionListener(listener);
    }

    public void addManageHotelListener(ActionListener listener) {
        manageHotelButton.addActionListener(listener);
    }

    public void addSimulateBookingListener(ActionListener listener) {
        simulateBookingButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}
