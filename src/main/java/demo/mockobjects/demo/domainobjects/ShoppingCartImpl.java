package demo.mockobjects.demo.domainobjects;

public class ShoppingCartImpl implements ShoppingCart {

    private float totalAmount;
    private long[] itemNumbers;

    public ShoppingCartImpl(float totalAmount, long[] itemNumbers) {
        this.totalAmount = totalAmount;
        this.itemNumbers = itemNumbers;
    }

    public long[] getItemNumbers() {
        return itemNumbers;
    }

    public float getTotalAmount() {
        return totalAmount;
    }
}
