package vn.edu.tdtu.springecommerce.service;

import jakarta.servlet.http.HttpSession;
import vn.edu.tdtu.springecommerce.model.Customer;
import vn.edu.tdtu.springecommerce.model.Orders;

import java.util.List;

public interface OrdersService {
    void addOrder(Orders order, Customer currentCustomer, HttpSession session);
    Orders addOrder(Orders order, int customerId);
    void updateOrder(int orderId, Orders order);
    Orders findById(int ordersId);
    List<Orders> findAllOrderPlaced();
    List<Orders> findOrderPlacedOfCustomer(Customer currentCustomer);
    void updateStatus(int orderId, int status);
}
