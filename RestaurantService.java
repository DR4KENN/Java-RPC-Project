import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RestaurantService extends Remote {
    void createOrder(String orderDetails) throws RemoteException;
    List<String> listOrders() throws RemoteException;
    String getOrderStatus(int orderId) throws RemoteException;
    void updateOrderStatus(int orderId, String status) throws RemoteException;
}
