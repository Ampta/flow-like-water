package ArrayProblem.compair;

public class Address implements Comparable<Address> {
    private String street;
    private int pincode;

    public Address() {
    }
    public Address(String street, int pincode) {
        this.street = street;
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", pincode=" + pincode +
                '}';
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    @Override
    public int compareTo(Address o) {
        return this.pincode - o.getPincode();
    }
}
