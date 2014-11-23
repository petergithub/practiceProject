package demo.mockobjects.demo.services;

public interface OrderDispatcher {

    void dispatchItem(long itemNumber,
                      String name,
                      String address);

}
