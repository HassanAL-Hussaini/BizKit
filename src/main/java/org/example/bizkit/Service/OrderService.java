package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Client;
import org.example.bizkit.Model.Invoice;
import org.example.bizkit.Model.OrderItem;
import org.example.bizkit.Model.Orders;
import org.example.bizkit.Model.Product;
import org.example.bizkit.Model.Provider;
import org.example.bizkit.Repository.InvoiceRepository;
import org.example.bizkit.Repository.OrderItemRepository;
import org.example.bizkit.Repository.OrderRepository;
import org.example.bizkit.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ProviderService providerService;
    private final ProductRepository productRepository;
    private final ClientService clientService;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemService orderItemService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;



    //============ REQUEST ORDER ===================
    public void requestOrder(Integer clientId, Integer productId, Integer quantity) {
        // validate client
        Client client = clientService.getClientByIdAndCheckIfExist(clientId);
        if (!"active".equalsIgnoreCase(client.getStatus())) {
            throw new ApiException("Client is not active");
        }

        // validate product + provider
        Product product = productService.getProductByIdAndCheckIfExist(productId);
        Provider provider = providerService.getProviderByIdAndCheckIfExist(product.getProviderId());
        if (Boolean.FALSE.equals(provider.getIsActive())) {
            throw new ApiException("Provider is not active");
        }

        // create order (pending)
        Orders orders = new Orders();
        orders.setClientId(client.getId());
        orders.setProviderId(product.getProviderId());
        orders.setProductId(productId);
        orders.setTotalPrice(0.0);
        orders = orderRepository.save(orders);

        // pending + notify provider
        setStatusAndNotify(orders, "pending");

        boolean stockDecremented = false;

            // stock check
            if (quantity <= 0 || quantity > product.getStockQuantity()) {
                throw new ApiException("Quantity must be > 0 and <= available stock");
            }

            // create order item (snapshot price)
            //Done separate it in orderItem Service
            orderItemService.addOrderItem(orders.getId(),productId,quantity);
            OrderItem orderItem = orderItemRepository.findOrderItemByOrderId(orders.getId());

            // decrement stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
            stockDecremented = true;

            // compute total = price * qty
            Double total = product.getPrice() * quantity;

            // update order totals
            orders.setTotalPrice(total);
            orderRepository.save(orders);

            // create & save invoice
            //Done make the invoice come from the invoice service
            invoiceService.addInvoice(orders.getId(),total);
            Invoice invoice = invoiceRepository.findInvoiceByOrderId(orders.getId());


            // accepted + notify client
            setStatusAndNotify(orders, "accepted");
    }

    //============ CANCEL ORDER By Client ===================
    public void cancelOrder(Integer clientId, Integer orderId) {
        // validate client
        Client client = clientService.getClientByIdAndCheckIfExist(clientId);
        if (!"active".equalsIgnoreCase(client.getStatus())) {
            throw new ApiException("Client is not active");
        }

        // validate order
        Orders orders = getOrderByIdAndCheckIfExist(orderId);

        if (!orders.getClientId().equals(client.getId())) {
            throw new ApiException("Client is not owner of this order");
        }
        if ("completed".equalsIgnoreCase(orders.getStatus())
                || "rejected".equalsIgnoreCase(orders.getStatus())
                || "canceled".equalsIgnoreCase(orders.getStatus())) {
            throw new ApiException("Order already finalized");
        }

        // load items + invoice
        OrderItem orderItem = orderItemRepository.findOrderItemByOrderId(orders.getId());
        Invoice invoice = invoiceRepository.findInvoiceByOrderId(orders.getId());

        // restore stock (single-item order per your comment)
        if (orderItem != null) {
            Product p = productService.getProductByIdAndCheckIfExist(orderItem.getProductId());
            p.setStockQuantity(p.getStockQuantity() + orderItem.getQuantity());
            productRepository.save(p);
        }

        // delete invoice (if exists)
        if (invoice != null && invoice.getId() != null) {
            invoiceRepository.deleteById(invoice.getId());
        }

        // mark order canceled
        orders.setStatus("canceled");
        orders.setTotalPrice(0.0);
        orderRepository.save(orders);

        // notify provider
        setStatusAndNotify(orders, "canceled");
    }

    //============ COMPLETE REQUEST ORDER BY PROVIDER ===================
    public void completeOrder(Integer providerId, Integer orderId) {
        Provider provider = providerService.getProviderByIdAndCheckIfExist(providerId);
        if (Boolean.FALSE.equals(provider.getIsActive())) {
            throw new ApiException("Provider is not active");
        }

        Orders orders = getOrderByIdAndCheckIfExist(orderId);
        if (!orders.getProviderId().equals(provider.getId())) {
            throw new ApiException("Provider is not owner of this order");
        }
        if (!"accepted".equalsIgnoreCase(orders.getStatus())) {
            throw new ApiException("Only accepted orders can be completed");
        }

        // completed + notify client
        setStatusAndNotify(orders, "completed");
    }

    //============ REJECT ORDER BY PROVIDER ===================
    public void rejectOrderByProvider(Integer providerId, Integer orderId) {
        // validate provider
        Provider provider = providerService.getProviderByIdAndCheckIfExist(providerId);
        if (Boolean.FALSE.equals(provider.getIsActive())) {
            throw new ApiException("Provider is not active");
        }

        // validate order
        Orders orders = getOrderByIdAndCheckIfExist(orderId);

        // ownership check
        if (!orders.getProviderId().equals(provider.getId())) {
            throw new ApiException("Provider is not owner of this order");
        }

        // only non-finalized can be rejected
        if ("completed".equalsIgnoreCase(orders.getStatus())
                || "rejected".equalsIgnoreCase(orders.getStatus())
                || "canceled".equalsIgnoreCase(orders.getStatus())) {
            throw new ApiException("Order already finalized");
        }

        // load order item (single item per order)
        OrderItem orderItem = orderItemRepository.findOrderItemByOrderId(orders.getId());
        if (orderItem == null) {
            throw new ApiException("Order item not found for this order");
        }

        // load invoice
        Invoice invoice = invoiceRepository.findInvoiceByOrderId(orders.getId());

        // compensation: restore stock + remove invoice + mark rejected
        Product product = productService.getProductByIdAndCheckIfExist(orderItem.getProductId());
        product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
        productRepository.save(product);

        if (invoice != null && invoice.getId() != null) {
            invoiceRepository.deleteById(invoice.getId());
        }

        orders.setTotalPrice(0.0);
        orderRepository.save(orders);

        // rejected + notify client
        setStatusAndNotify(orders, "rejected");
    }

    //========================== UPDATE QUANTITY ===============================
    public void updateOrderItemQuantity(Integer orderItemId, Integer delta) {
        if (delta == null || delta == 0) {
            throw new ApiException("Quantity change must be non-zero");
        }

        // load OrderItem
        OrderItem orderItem = orderItemService.getOrderItemByIdAndCheckIfExist(orderItemId);

        // load order
        Orders order = orderRepository.findOrdersById(orderItem.getOrderId());
        if (order == null) {
            throw new ApiException("Order not found");
        }
        if ("completed".equalsIgnoreCase(order.getStatus())
                || "rejected".equalsIgnoreCase(order.getStatus())
                || "canceled".equalsIgnoreCase(order.getStatus())) {
            throw new ApiException("Order already finalized");
        }

        // load product
        Product product = productService.getProductByIdAndCheckIfExist(orderItem.getProductId());

        if (delta > 0) {
            // increase
            if (product.getStockQuantity() < delta) {
                throw new ApiException("Not enough stock for the increase");
            }
            product.setStockQuantity(product.getStockQuantity() - delta);
            productRepository.save(product);
        } else {
            // decrease
            int decrease = -delta;
            if (orderItem.getQuantity() < decrease) {
                throw new ApiException("Quantity cannot go below zero");
            }
            product.setStockQuantity(product.getStockQuantity() + decrease);
            productRepository.save(product);
        }

        // Update Quantity
        int newQty = orderItem.getQuantity() + delta;
        if (newQty <= 0) {
            throw new ApiException("Resulting quantity must be > 0");
        }
        orderItem.setQuantity(newQty);
        orderItemRepository.save(orderItem);

        // update Amount (single item per order per تصميمك)
        double total = orderItem.getPrice() * orderItem.getQuantity();
        order.setTotalPrice(total);
        orderRepository.save(order);

        Invoice invoice = invoiceRepository.findInvoiceByOrderId(order.getId());
        if (invoice != null) {
            invoice.setAmount(total);
            invoiceRepository.save(invoice);
        }
    }

    // ================== Get Services ==================
    public Orders getOrderById(Integer orderId) {
        Orders order = orderRepository.findOrdersById(orderId);
        if (order == null) throw new ApiException("Order not found");
        return order;
    }

    public List<Orders> getOrdersByClient(Integer clientId) {
        clientService.getClientByIdAndCheckIfExist(clientId);
        return orderRepository.findOrdersByClientId(clientId);
    }

    public List<Orders> getOrdersByProvider(Integer providerId) {
        providerService.getProviderByIdAndCheckIfExist(providerId);
        return orderRepository.findOrdersByProviderId(providerId);
    }

    public Orders getOrderByIdAndCheckIfExist(Integer id){
        Orders order = orderRepository.findOrdersById(id);
        if(order == null){
            throw new ApiException("Order not found");
        }
        return order;
    }

    // ========= helper: set status + notify (HTML) =========
    private void setStatusAndNotify(Orders orders, String newStatus) {
        orders.setStatus(newStatus);
        orderRepository.save(orders);

        Client client = clientService.getClientByIdAndCheckIfExist(orders.getClientId());
        Provider provider = providerService.getProviderByIdAndCheckIfExist(orders.getProviderId());

        String orderUrl = "https://your-frontend/orders/" + orders.getId();

        switch (newStatus) {
            case "pending" -> {
                if (provider.getEmail() != null) {
                    String html = notificationService.buildOrderHtml(
                            "New Order #" + orders.getId() + " Pending",
                            "Hello Provider : " + (provider.getName() != null ? provider.getName() : "Provider") + ",",
                            "You have a new order <b>#"+orders.getId()+"</b> from client <b>#"+client.getId()+"</b>. Please review it.",
                            "View Order",
                            orderUrl,
                            "This is an automated notification from BizKit."
                    );
                    notificationService.sendHtmlEmail(
                            provider.getEmail(),
                            "New order #" + orders.getId() + " pending",
                            html
                    );
                }
            }
            case "accepted" -> {
                if (client.getEmail() != null) {
                    String html = notificationService.buildOrderHtml(
                            "Order #" + orders.getId() + " Accepted",
                            "Hello Client : " + (client.getName() != null ? client.getName() : "Client") + ",",
                            "Your order <b>#"+orders.getId()+"</b> has been <b>accepted</b>. Total amount: <b>"+orders.getTotalPrice()+"</b>.",
                            "View Order",
                            orderUrl,
                            "If you have any questions, just reply to this email."
                    );
                    notificationService.sendHtmlEmail(
                            client.getEmail(),
                            "Your order #" + orders.getId() + " accepted",
                            html
                    );
                }
            }
            case "rejected" -> {
                if (client.getEmail() != null) {
                    String html = notificationService.buildOrderHtml(
                            "Order #" + orders.getId() + " Rejected",
                            "Hello Client : " + (client.getName() != null ? client.getName() : "Client") + ",",
                            "Unfortunately, your order <b>#"+orders.getId()+"</b> has been <b>rejected</b>. You can place a new order or contact support.",
                            "View Order",
                            orderUrl,
                            "We’re here to help if you need anything."
                    );
                    notificationService.sendHtmlEmail(
                            client.getEmail(),
                            "Your order #" + orders.getId() + " rejected",
                            html
                    );
                }
            }
            case "canceled" -> {
                if (provider.getEmail() != null) {
                    String html = notificationService.buildOrderHtml(
                            "Order #" + orders.getId() + " Canceled",
                            "Hello Provider : " + (provider.getName() != null ? provider.getName() : "Provider") + ",",
                            "Order <b>#"+orders.getId()+"</b> was <b>canceled</b> by client <b>#"+client.getId()+"</b>.",
                            "View Order",
                            orderUrl,
                            "This is an automated notification from BizKit."
                    );
                    notificationService.sendHtmlEmail(
                            provider.getEmail(),
                            "Order #" + orders.getId() + " canceled",
                            html
                    );
                }
            }
            case "completed" -> {
                if (client.getEmail() != null) {
                    String html = notificationService.buildOrderHtml(
                            "Order #" + orders.getId() + " Completed",
                            "Hello Client : " + (client.getName() != null ? client.getName() : "Client") + ",",
                            "Your order <b>#"+orders.getId()+"</b> has been <b>completed</b>. Thank you for your business!",
                            "View Order",
                            orderUrl,
                            "We appreciate your trust in BizKit."
                    );
                    notificationService.sendHtmlEmail(
                            client.getEmail(),
                            "Your order #" + orders.getId() + " completed",
                            html
                    );
                }
            }
        }
    }
}
