package stage3;

public class Waiter extends Staff {
    public Waiter(){
        super("Waiter");
    }

    public boolean seatCustomer(Customer customer, Table table){
        if(!isAvailable() || customer == null || table == null || !table.isFree()
                || customer.getState() != CustomerState.WAITING){
            return false;
        }
        if(table.seatCustomer(customer) == false){
            return false;
        }
        busyThisTurn = true;
        return true;
    }

    public Order takeOrder(Customer customer, MenuItem item, HappyHourPolicy happyHour){
        if(!isAvailable() || customer == null || item == null
                || customer.getState() != CustomerState.SEATED){
            return null;
        }
        // VIP discount first, then Happy Hour. The final amount is locked in Order.
        int customerPrice = customer.applyCustomerDiscount(item.getPriceCents());
        int lockedPrice = happyHour.calculateDiscountedPrice(customerPrice);
        Order order = new Order(customer, item, lockedPrice);
        if(customer.placeOrder(order) == false){
            return null;
        }
        busyThisTurn = true;
        return order;
    }

    public boolean serveOrder(Order order, int currentTurn){
        if(!isAvailable() || order == null || !order.isReady()
                || order.getCustomer().getState() != CustomerState.ORDERED){
            return false;
        }
        if(order.markServed(currentTurn) == false){
            return false;
        }
        if(order.getCustomer().markServed() == false){
            return false;
        }
        busyThisTurn = true;
        return true;
    }
}
