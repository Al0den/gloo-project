package users;

import discount.DiscountPlan;

public class Customer extends User {  
    private DiscountPlan discountPlan;
    private String address;
    private boolean hasRequestedDelivery = false;

    public Customer(String username, String firstName, String surname, String address, String password, DiscountPlan discountPlan) {
        super(username, firstName, surname, password);
        this.address = address;
        this.discountPlan = discountPlan;
    } 

    public void setDiscountPlan(DiscountPlan discountPlan) {
        this.discountPlan = discountPlan;
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public DiscountPlan getDiscountPlan() {
        return discountPlan;
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
    }

    
}
