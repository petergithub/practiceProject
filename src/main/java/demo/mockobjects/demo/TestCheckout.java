package demo.mockobjects.demo;
import junit.framework.TestCase;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;

import demo.mockobjects.demo.domainobjects.Customer;
import demo.mockobjects.demo.domainobjects.CustomerImpl;
import demo.mockobjects.demo.domainobjects.ShoppingCart;
import demo.mockobjects.demo.domainobjects.ShoppingCartImpl;
import demo.mockobjects.demo.services.Invoicer;
import demo.mockobjects.demo.services.InvoicerImpl;
import demo.mockobjects.demo.services.OrderDispatcher;
import demo.mockobjects.demo.services.OrderDispatcherImpl;


public class TestCheckout extends TestCase {

    public void testDispatchAndInvoice() {

        // create a Checkout to test
        Checkout checkout = new Checkout();

        // Create some mock dependencies
        Mock orderDispatcher = new Mock(OrderDispatcher.class);
        Mock invoicer = new Mock(Invoicer.class);

        // Create some values to pass in
        Customer customer = new CustomerImpl(123, "Duke",
                "duke@ilovejava.com", "101 Java Dr., San Jose, CA 95126");
        ShoppingCart cart = new ShoppingCartImpl(100.50f, new long[]{1, 2, 3});

        // Substitute the dependencies of the Checkout with the mocks
        checkout.setOrderDispatcher((OrderDispatcher) orderDispatcher.proxy());
        checkout.setInvoicer((Invoicer) invoicer.proxy());

        // Setup expectations of how the Customer should interact with its dependencies
        invoicer.expect("invoiceCustomer",
                C.eq(new Long(123), "duke@ilovejava.com", new Float(100.50)));
        orderDispatcher.expect("dispatchItem",
                C.eq(new Long(1), "Duke", "101 Java Dr., San Jose, CA 95126"));
        orderDispatcher.expect("dispatchItem",
                C.eq(new Long(2), "Duke", "101 Java Dr., San Jose, CA 95126"));
        orderDispatcher.expect("dispatchItem",
                C.eq(new Long(3), "Duke", "101 Java Dr., San Jose, CA 95126"));

        // Execute the code under test
        checkout.process(cart, customer);

        // Verify the expectations were met
        invoicer.verify();
        orderDispatcher.verify();
    }
    
  //will throw exception if call the real method
    public void testDispatchAndInvoiceReal() {
        // create a Checkout to test
        Checkout checkout = new Checkout();

        // Create some mock dependencies
        OrderDispatcherImpl orderDispatcher = new OrderDispatcherImpl();
        InvoicerImpl invoicer = new InvoicerImpl();

        // Create some values to pass in
        Customer customer = new CustomerImpl(123, "Duke",
                "duke@ilovejava.com", "101 Java Dr., San Jose, CA 95126");
        ShoppingCart cart = new ShoppingCartImpl(100.50f, new long[]{1, 2, 3});

        // Substitute the dependencies of the Checkout with the mocks
        checkout.setOrderDispatcher((OrderDispatcher) orderDispatcher);
        checkout.setInvoicer((Invoicer) invoicer);

//will throw exception if call the real method
        // Execute the code under test
        try {
        	checkout.process(cart, customer);
        } catch (UnsupportedOperationException e) {
        	e.printStackTrace();
        }
    }
}
