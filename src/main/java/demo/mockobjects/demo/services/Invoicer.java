package demo.mockobjects.demo.services;

public interface Invoicer {

    void invoiceCustomer(long customerNumber,
                         String customerEmail,
                         float amount);

}
