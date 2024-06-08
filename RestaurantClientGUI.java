import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.rmi.Naming;
import java.util.List;
import javax.swing.*;

public class RestaurantClientGUI extends JFrame {
    private RestaurantService service;
    private JTextArea displayArea;
    private JTextField orderDetailsField;
    private JTextField orderIdField;
    private JTextField statusField;

    public RestaurantClientGUI(String host) {
        try {
            service = (RestaurantService) Naming.lookup("rmi://" + host + ":1099/RestaurantService");
            initUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Client exception: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initUI() {
        setTitle("Restaurant Client");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        orderDetailsField = new JTextField();
        orderIdField = new JTextField();
        statusField = new JTextField();

        inputPanel.add(new JLabel("Order Details:"));
        inputPanel.add(orderDetailsField);
        inputPanel.add(new JLabel("Order ID:"));
        inputPanel.add(orderIdField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        JButton createOrderButton = new JButton("Create Order");
        createOrderButton.addActionListener(e -> createOrder());
        JButton listOrdersButton = new JButton("List Orders");
        listOrdersButton.addActionListener(e -> listOrders());
        JButton getOrderStatusButton = new JButton("Get Order Status");
        getOrderStatusButton.addActionListener(e -> getOrderStatus());
        JButton updateOrderStatusButton = new JButton("Update Order Status");
        updateOrderStatusButton.addActionListener(e -> updateOrderStatus());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(createOrderButton);
        buttonPanel.add(listOrdersButton);
        buttonPanel.add(getOrderStatusButton);
        buttonPanel.add(updateOrderStatusButton);
        buttonPanel.add(exitButton);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void createOrder() {
        displayArea.setText("");  // Clear the screen
        String orderDetails = orderDetailsField.getText();
        if (orderDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Order details cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            service.createOrder(orderDetails);
            displayArea.append("Order created successfully.\n");
            orderDetailsField.setText("");
        } catch (Exception e) {
            showError("Error creating order: " + e.toString());
        }
    }

    private void listOrders() {
        displayArea.setText("");  // Clear the screen
        try {
            List<String> orders = service.listOrders();
            displayArea.append("Orders:\n");
            for (int i = 0; i < orders.size(); i++) {
                displayArea.append((i + 1) + ". " + orders.get(i) + "\n");
            }
            displayArea.append("--------------------------------------\n");
        } catch (Exception e) {
            showError("Error listing orders: " + e.toString());
        }
    }

    private void getOrderStatus() {
        displayArea.setText("");  // Clear the screen
        try {
            int orderId = Integer.parseInt(orderIdField.getText());
            String status = service.getOrderStatus(orderId - 1);
            displayArea.append("Order Status: " + status + "\n");
        } catch (NumberFormatException e) {
            showError("Invalid order ID.");
        } catch (Exception e) {
            showError("Error getting order status: " + e.toString());
        }
    }

    private void updateOrderStatus() {
        displayArea.setText("");  // Clear the screen
        try {
            int orderId = Integer.parseInt(orderIdField.getText());
            String newStatus = statusField.getText();
            service.updateOrderStatus(orderId - 1, newStatus);
            displayArea.append("Order status updated successfully.\n");
            orderIdField.setText("");
            statusField.setText("");
        } catch (NumberFormatException e) {
            showError("Invalid order ID.");
        } catch (Exception e) {
            showError("Error updating order status: " + e.toString());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java RestaurantClientGUI <server-host>");
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> {
            RestaurantClientGUI client = new RestaurantClientGUI(args[0]);
            client.setVisible(true);
        });
    }
}

