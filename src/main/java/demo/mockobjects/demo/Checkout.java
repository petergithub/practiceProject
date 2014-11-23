package demo.mockobjects.demo;
import demo.mockobjects.demo.domainobjects.Customer;
import demo.mockobjects.demo.domainobjects.ShoppingCart;
import demo.mockobjects.demo.services.Invoicer;
import demo.mockobjects.demo.services.InvoicerImpl;
import demo.mockobjects.demo.services.OrderDispatcher;
import demo.mockobjects.demo.services.OrderDispatcherImpl;

public class Checkout {

    private OrderDispatcher orderDispatcher = new OrderDispatcherImpl();
    private Invoicer invoicer = new InvoicerImpl();

    public void setOrderDispatcher(OrderDispatcher orderDispatcher) {
        this.orderDispatcher = orderDispatcher;
    }

    public void setInvoicer(Invoicer invoicer) {
        this.invoicer = invoicer;
    }

    public void process(ShoppingCart shoppingCart, Customer customer) {
        // send invoice
        invoicer.invoiceCustomer(customer.getNumber(),
                customer.getEmail(),
                shoppingCart.getTotalAmount());
        // dispatch items
        long[] itemNumbers = shoppingCart.getItemNumbers();
        for (int i = 0; i < itemNumbers.length; i++) {
            orderDispatcher.dispatchItem(itemNumbers[i],
                    customer.getName(),
                    customer.getAddress());
        }
    }

}
