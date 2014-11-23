package demo.mockobjects.demo.domainobjects;

public class CustomerImpl implements Customer {

    private long number;
    private String name;
    private String email;
    private String address;

    public CustomerImpl(long number, String name, String email, String address) {
        this.number = number;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public long getNumber() {
        return number;
    }

}
