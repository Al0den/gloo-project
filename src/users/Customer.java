package users;

import discount.DiscountPolicy;
import delivery.DeliveryRequest;

public class Customer extends User {  
    private DiscountPolicy discountPolicy;
    private String address;
    private boolean hasRequestedDelivery = false;
    private DeliveryRequest deliveryRequest;

    public Customer(String username, String firstName, String surname, String address, String password, DiscountPolicy discountPolicy) {
        super(username, firstName, surname, password);
        this.address = address;
        this.discountPolicy = discountPolicy;
    } 

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean hasRequestedDelivery() {
        return hasRequestedDelivery;
    }   

    public void requestDelivery() {
        this.hasRequestedDelivery = true;

        deliveryRequest = new DeliveryRequest(address, 10.0);
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }
}
