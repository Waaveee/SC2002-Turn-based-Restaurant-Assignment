package stage2;

/** An order's bill is fixed at creation; only its lifecycle can change. */
public class Order {
    private final int orderId;
    private final Customer customer;
    private final MenuItem dish;
    private final int lockedPriceCents;
    private int remainingPrepUnits;
    private int servedTurn = -1;
    private OrderStatus status = OrderStatus.IN_PROGRESS;

    public Order(Customer customer, MenuItem dish, int lockedPriceCents){
        this.customer = customer;
        this.orderId = customer.getCustomerId(); // One order per customer.
        this.dish = dish;
        this.lockedPriceCents = lockedPriceCents;
        this.remainingPrepUnits = dish.getPrepUnits();
    }

    public int getOrderId(){
        return orderId;
    }
    public Customer getCustomer(){
        return customer;
    }
    public MenuItem getDish(){
        return dish;
    }
    public int getLockedPriceCents(){
        return lockedPriceCents;
    }
    public int getRemainingPrepUnits(){
        return remainingPrepUnits;
    }
    public int getServedTurn(){
        return servedTurn;
    }
    public OrderStatus getStatus(){
        return status;
    }
    public boolean isReady(){
        return status == OrderStatus.READY;
    }

    public boolean advancePreparation(){
        if(status != OrderStatus.IN_PROGRESS){
            return false;
        }
        remainingPrepUnits--;
        if(remainingPrepUnits == 0){
            status = OrderStatus.READY;
        }
        return true;
    }

    public boolean markServed(int currentTurn){
        if(!isReady() || currentTurn < 1){
            return false;
        }
        status = OrderStatus.SERVED;
        servedTurn = currentTurn;
        return true;
    }

    public boolean hasFinishedEating(int currentTurn){
        return status == OrderStatus.SERVED && currentTurn > servedTurn;
    }

    public boolean markPaid(){
        if(status != OrderStatus.SERVED || customer.getState() != CustomerState.READY_TO_PAY){
            return false;
        }
        status = OrderStatus.PAID;
        return true;
    }

    public boolean cancel(){
        if(status != OrderStatus.IN_PROGRESS && status != OrderStatus.READY){
            return false;
        }
        status = OrderStatus.CANCELLED;
        return true;
    }
}
