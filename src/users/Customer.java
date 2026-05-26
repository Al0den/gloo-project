package users;

import discount.DiscountPlan;

public class Customer extends User {  
    private DiscountPlan discountPlan;
    private String address;

    public Customer(String username, String firstName, String surname, String password, String address, DiscountPlan discountPlan) {
        super(username, firstName, surname, password);
        this.discountPlan = discountPlan;
        this.address = address;
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
}
