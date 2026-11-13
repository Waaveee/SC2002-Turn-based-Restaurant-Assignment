package stage3;

// Arrays hold the six customers, two tables and their orders.
public class Restaurant {
    private Customer[] customers = new Customer[6];
    private Customer[] waitingQueue = new Customer[6];
    private Order[] activeOrders = new Order[6];
    private int customerCount = 0;
    private int waitingCount = 0;
    private int orderCount = 0;
    private Table[] tables = new Table[2];
    private MenuItem[] menu;
    private Waiter waiter = new Waiter();
    private Chef chef = new Chef();
    private Cashier cashier = new Cashier();
    private Host host = new Host();
    private int revenueCents = 0;
    private int paidCustomerCount = 0;
    private int totalPaidSatisfaction = 0;
    private int unhappyDepartures = 0;
    private int currentTurn;

    public Restaurant(){
        for(int i=0; i<tables.length; i++){
            tables[i] = new Table(i+1);
        }
        menu = new MenuItem[4];
        menu[0] = new SingleMenuItem("Salad", 900, 1);
        menu[1] = new SingleMenuItem("Burger", 1500, 1);
        menu[2] = new SingleMenuItem("Pasta", 1800, 2);
        menu[3] = new ComboMeal("Salad + Pasta", menu[0], menu[2]);
    }

    public void startTurn(int turn){
        currentTurn = turn;
        waiter.resetTurn();
        chef.resetTurn();
        cashier.resetTurn();
        host.resetTurn();
    }

    public int getRevenueCents(){
        return revenueCents;
    }
    public int getPaidCustomerCount(){
        return paidCustomerCount;
    }
    public int getUnhappyDepartures(){
        return unhappyDepartures;
    }
    public Waiter getWaiter(){
        return waiter;
    }
    public Chef getChef(){
        return chef;
    }
    public Cashier getCashier(){
        return cashier;
    }
    public Host getHost(){
        return host;
    }
    // Return copies, like the copied seat array in the Plane lab.
    public MenuItem[] getMenu(){
        return menu.clone();
    }
    public Table[] getTables(){
        return tables.clone();
    }

    public Customer[] getCustomers(){
        Customer[] copy = new Customer[customerCount];
        for(int i=0; i<customerCount; i++){
            copy[i] = customers[i];
        }
        return copy;
    }

    public Customer[] getWaitingQueue(){
        Customer[] copy = new Customer[waitingCount];
        for(int i=0; i<waitingCount; i++){
            copy[i] = waitingQueue[i];
        }
        return copy;
    }

    public Order[] getActiveOrders(){
        Order[] copy = new Order[orderCount];
        for(int i=0; i<orderCount; i++){
            copy[i] = activeOrders[i];
        }
        return copy;
    }

    public boolean isInWaitingQueue(Customer customer){
        for(int i=0; i<waitingCount; i++){
            if(waitingQueue[i] == customer){
                return true;
            }
        }
        return false;
    }

    public boolean addCustomerToQueue(Customer customer){
        if(customer == null || customerCount == customers.length){
            return false;
        }
        if(customer.getState() != CustomerState.WAITING || getCustomer(customer.getCustomerId()) != null){
            return false;
        }
        customers[customerCount] = customer;
        customerCount++;
        waitingQueue[waitingCount] = customer;
        waitingCount++;
        return true;
    }

    public Customer getCustomer(int id){
        for(int i=0; i<customerCount; i++){
            if(customers[i].getCustomerId() == id){
                return customers[i];
            }
        }
        return null;
    }

    public Table getTable(int id){
        for(int i=0; i<tables.length; i++){
            if(tables[i].getTableId() == id){
                return tables[i];
            }
        }
        return null;
    }

    public Table getFreeTable(){
        for(int i=0; i<tables.length; i++){
            if(tables[i].isFree()){
                return tables[i];
            }
        }
        return null;
    }

    public Order getOrder(int id){
        for(int i=0; i<orderCount; i++){
            if(activeOrders[i].getOrderId() == id){
                return activeOrders[i];
            }
        }
        return null;
    }

    public Customer[] getCustomersInState(CustomerState state){
        int count = 0;
        for(int i=0; i<customerCount; i++){
            if(customers[i].getState() == state){
                count++;
            }
        }
        Customer[] result = new Customer[count];
        int index = 0;
        for(int i=0; i<customerCount; i++){
            if(customers[i].getState() == state){
                result[index] = customers[i];
                index++;
            }
        }
        return result;
    }

    public Order[] getOrdersWithStatus(OrderStatus status){
        int count = 0;
        for(int i=0; i<orderCount; i++){
            if(activeOrders[i].getStatus() == status){
                count++;
            }
        }
        Order[] result = new Order[count];
        int index = 0;
        for(int i=0; i<orderCount; i++){
            if(activeOrders[i].getStatus() == status){
                result[index] = activeOrders[i];
                index++;
            }
        }
        return result;
    }

    public void removeSeatedCustomerFromQueue(Customer customer){
        if(customer != null && customer.getState() == CustomerState.SEATED){
            removeFromQueue(customer);
        }
    }

    private void removeFromQueue(Customer customer){
        for(int i=0; i<waitingCount; i++){
            if(waitingQueue[i] == customer){
                // Shift the remaining customers left to keep their queue order.
                for(int j=i; j<waitingCount-1; j++){
                    waitingQueue[j] = waitingQueue[j+1];
                }
                waitingCount--;
                waitingQueue[waitingCount] = null;
                return;
            }
        }
    }

    public boolean addOrder(Order order){
        if(order == null || orderCount == activeOrders.length){
            return false;
        }
        Customer customer = order.getCustomer();
        if(getCustomer(customer.getCustomerId()) != customer || customer.getCurrentOrder() != order
                || getOrder(order.getOrderId()) != null){
            return false;
        }
        activeOrders[orderCount] = order;
        orderCount++;
        return true;
    }

    // Pay the earliest READY_TO_PAY customer; use arrival ID to break ties.
    public Customer getOldestReadyToPayCustomer(){
        Customer oldest = null;
        for(int i=0; i<customerCount; i++){
            Customer customer = customers[i];
            if(customer.getState() == CustomerState.READY_TO_PAY
                    && customer.getReadyToPayTurn() < currentTurn){
                if(oldest == null || customer.getReadyToPayTurn() < oldest.getReadyToPayTurn()){
                    oldest = customer;
                }
                else if(customer.getReadyToPayTurn() == oldest.getReadyToPayTurn()
                        && customer.getCustomerId() < oldest.getCustomerId()){
                    oldest = customer;
                }
            }
        }
        return oldest;
    }

    public void transitionEatenCustomers(int turn){
        for(int i=0; i<customerCount; i++){
            customers[i].finishEating(turn);
        }
    }

    public void applyDecayAndCleanupAbandoned(){
        for(int i=0; i<customerCount; i++){
            Customer customer = customers[i];
            customer.applyEndOfTurnDecay();
            if(customer.getState() != CustomerState.LEFT && customer.hasAbandoned()){
                if(customer.leave()){
                    unhappyDepartures++;
                    releaseCustomer(customer);
                }
            }
        }
    }

    public void releaseCustomer(Customer customer){
        if(customer == null){
            return;
        }
        if(customer.getState() != CustomerState.PAID && customer.getState() != CustomerState.LEFT){
            return;
        }
        removeFromQueue(customer);
        for(int i=0; i<orderCount; i++){
            if(activeOrders[i] == customer.getCurrentOrder()){
                for(int j=i; j<orderCount-1; j++){
                    activeOrders[j] = activeOrders[j+1];
                }
                orderCount--;
                activeOrders[orderCount] = null;
                break;
            }
        }
        for(int i=0; i<tables.length; i++){
            if(tables[i].getOccupant() == customer){
                tables[i].freeTable();
            }
        }
    }

    public void recordPayment(int amountCents, int satisfaction){
        if(amountCents < 0 || satisfaction < 0 || satisfaction > 100){
            return;
        }
        revenueCents = revenueCents + amountCents;
        paidCustomerCount++;
        totalPaidSatisfaction = totalPaidSatisfaction + satisfaction;
    }

    public double getAveragePaidSatisfaction(){
        if(paidCustomerCount == 0){
            return 0; // The UI displays N/A when nobody has paid.
        }
        return (double) totalPaidSatisfaction / paidCustomerCount;
    }

    public int getServedCustomerCount(){
        int count = 0;
        for(int i=0; i<customerCount; i++){
            CustomerState state = customers[i].getState();
            if(state == CustomerState.SERVED || state == CustomerState.READY_TO_PAY || state == CustomerState.PAID){
                count++;
            }
        }
        return count;
    }

    public boolean evaluateVictoryTargets(){
        return paidCustomerCount >= 4 && revenueCents >= 4500 && getAveragePaidSatisfaction() >= 60;
    }
}
