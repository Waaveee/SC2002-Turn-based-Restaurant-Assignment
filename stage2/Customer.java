package stage2;

// Stores one customer's information and checks valid state changes.
public abstract class Customer {
    protected int customerId;
    private int satisfaction = 100;
    private CustomerState state = CustomerState.WAITING;
    private Order currentOrder;
    private int readyToPayTurn = -1;

    public Customer(int customerId){
        this.customerId = customerId;
    }

    public int getCustomerId(){
        return customerId;
    }
    public int getSatisfaction(){
        return satisfaction;
    }
    public CustomerState getState(){
        return state;
    }
    public Order getCurrentOrder(){
        return currentOrder;
    }
    public int getReadyToPayTurn(){
        return readyToPayTurn;
    }
    public abstract String getTypeName();
    protected abstract int getWaitingDecay();

    public MenuItem chooseFood(MenuItem[] menu, IOrderChoiceStrategy strategy){
        return strategy.selectMenuItem(menu, this);
    }

    public int applyCustomerDiscount(int baseCents){
        return baseCents;
    }

    public boolean isWaitingForService(){
        return state == CustomerState.WAITING || state == CustomerState.SEATED
                || state == CustomerState.ORDERED;
    }

    public void applyEndOfTurnDecay(){
        if(isWaitingForService()){
            satisfaction = satisfaction - getWaitingDecay();
            if(satisfaction < 0){
                satisfaction = 0;
            }
        }
    }

    public boolean hasAbandoned(){
        return state == CustomerState.LEFT || (isWaitingForService() && satisfaction == 0);
    }

    // true means the action succeeded; false means it was not allowed.
    public boolean markSeated(){
        if(state != CustomerState.WAITING){
            return false;
        }
        state = CustomerState.SEATED;
        return true;
    }

    public boolean placeOrder(Order order){
        if(state != CustomerState.SEATED || order == null || currentOrder != null){
            return false;
        }
        if(order.getCustomer() != this){
            return false;
        }
        currentOrder = order;
        state = CustomerState.ORDERED;
        return true;
    }

    public boolean markServed(){
        if(state != CustomerState.ORDERED || currentOrder == null){
            return false;
        }
        if(currentOrder.getStatus() != OrderStatus.SERVED){
            return false;
        }
        state = CustomerState.SERVED;
        return true;
    }

    public void finishEating(int currentTurn){
        if(state == CustomerState.SERVED && currentOrder.hasFinishedEating(currentTurn)){
            state = CustomerState.READY_TO_PAY;
            readyToPayTurn = currentTurn;
        }
    }

    public boolean markPaid(){
        if(state != CustomerState.READY_TO_PAY || currentOrder == null){
            return false;
        }
        if(currentOrder.markPaid() == false){
            return false;
        }
        state = CustomerState.PAID;
        return true;
    }

    public boolean leave(){
        if(state == CustomerState.LEFT || hasAbandoned() == false){
            return false;
        }
        if(currentOrder != null){
            if(currentOrder.cancel() == false){
                return false;
            }
        }
        state = CustomerState.LEFT;
        return true;
    }
}
