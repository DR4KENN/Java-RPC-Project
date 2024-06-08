import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantServiceImpl extends UnicastRemoteObject implements RestaurantService {
    private List<String> orders;
    private Map<Integer, String> orderStatus;

    protected RestaurantServiceImpl() throws RemoteException {
        super();
        orders = new ArrayList<>();
        orderStatus = new HashMap<>();
    }

    @Override
    public void createOrder(String orderDetails) throws RemoteException {
        orders.add(orderDetails);
        orderStatus.put(orders.size() - 1, "Pending");
    }

    @Override
    public List<String> listOrders() throws RemoteException {
        return orders;
    }

    @Override
    public String getOrderStatus(int orderId) throws RemoteException {
        return orderStatus.get(orderId);
    }

    @Override
    public void updateOrderStatus(int orderId, String status) throws RemoteException {
        orderStatus.put(orderId, status);
    }

    public static void main(String[] args) {
        try {
            RestaurantServiceImpl service = new RestaurantServiceImpl();
            java.rmi.registry.LocateRegistry.createRegistry(1099).rebind("RestaurantService", service);
            System.out.println("Restaurant Service RMI Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

