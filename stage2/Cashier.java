package stage2;

public class Cashier extends Staff {
    public Cashier(){
        super("Cashier");
    }

    public boolean processPayment(Restaurant restaurant){
        if(!isAvailable()){
            return false;
        }
        Customer customer = restaurant.getOldestReadyToPayCustomer();
        if(customer == null){
            return false;
        }
        Order order = customer.getCurrentOrder();
        if(customer.markPaid() == false){
            return false;
        }
        restaurant.recordPayment(order.getLockedPriceCents(), customer.getSatisfaction());
        restaurant.releaseCustomer(customer);
        busyThisTurn = true;
        return true;
    }
}
