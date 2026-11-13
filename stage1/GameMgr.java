package stage1;

/** Coordinates turn phases; customer-specific behaviour stays in Customer. */
public class GameMgr {
    public static final int MAX_TURNS = 18;
    private int currentTurn = 1;
    private final Restaurant restaurant;
    private final RestaurantUI ui;
    private final ArrivalSchedule schedule;
    private final IOrderChoiceStrategy choiceStrategy;

    public GameMgr(Restaurant restaurant, RestaurantUI ui, ArrivalSchedule schedule, IOrderChoiceStrategy choiceStrategy){
        this.restaurant = restaurant;
        this.ui = ui;
        this.schedule = schedule;
        this.choiceStrategy = choiceStrategy;
    }

    public void runGame(){
        for(; currentTurn <= MAX_TURNS; currentTurn++){
            executeTurn(currentTurn);
        }
        ui.displayFinalResult(checkVictory(), restaurant);
    }

    private void executeTurn(int turn){
        restaurant.startTurn(turn);
        processArrivals(turn);
        ui.displayStartOfTurn(restaurant, turn);
        processManagerAndStaffActions(turn);
        processEatingAndPayment(turn);
        processWaitingAndAbandonment();
        ui.displayEndOfTurn(restaurant, turn);
    }

    private void processArrivals(int turn){
        Customer arrival = schedule.getScheduledCustomer(turn);
        if(arrival != null){
            restaurant.addCustomerToQueue(arrival);
            ui.displayMessage("Arrival: customer " + arrival.getCustomerId() + " (" + arrival.getTypeName() + ").");
        }
    }

    private void processManagerAndStaffActions(int turn){
        // Complete all Waiter decisions BEFORE the Chef can change an order to READY.
        while(true){
            int task = ui.promptWaiterTask(restaurant);
            if(task == 0){
                break;
            }
            if(task == 1){
                if(restaurant.getWaitingQueue().length == 0 || restaurant.getFreeTable() == null){
                    ui.displayError("Seating needs a queued customer and a free table.");
                    continue;
                }
                int id = ui.promptCustomerId(restaurant.getWaitingQueue());
                if(id == 0){
                    continue;
                }
                Customer customer = restaurant.getCustomer(id);
                if(!restaurant.isInWaitingQueue(customer)){
                    ui.displayError("Customer is not in the waiting queue.");
                    continue;
                }
                int tableId = ui.promptTableId(restaurant);
                if(tableId == 0){
                    continue;
                }
                if(!restaurant.getWaiter().seatCustomer(customer, restaurant.getTable(tableId))){
                    ui.displayError("Select a free table.");
                    continue;
                }
                restaurant.removeSeatedCustomerFromQueue(customer);
                ui.displayMessage("Waiter seated customer " + id + " at table " + tableId + ".");
                break;
            } else if (task == 2){
                if(restaurant.getCustomersInState(CustomerState.SEATED).length == 0){
                    ui.displayError("No seated customer is awaiting an order.");
                    continue;
                }
                int id = ui.promptCustomerId(restaurant.getCustomersInState(CustomerState.SEATED));
                if(id == 0){
                    continue;
                }
                Customer customer = restaurant.getCustomer(id);
                if(customer == null || customer.getState() != CustomerState.SEATED){
                    ui.displayError("Only a seated customer without an order can order.");
                    continue;
                }
                MenuItem item = customer.chooseFood(restaurant.getMenu(), choiceStrategy);
                Order order = restaurant.getWaiter().takeOrder(customer, item);
                if(order == null){
                    ui.displayError("The order could not be taken. Choose again.");
                    continue;
                }
                restaurant.addOrder(order);
                ui.displayMessage("Customer " + id + " chose " + item.getName()
                        + "; locked bill " + RestaurantUI.money(order.getPriceCents()) + ".");
                break;
            } else if (task == 3){
                if(restaurant.getOrdersWithStatus(OrderStatus.READY).length == 0){
                    ui.displayError("No order is READY. A dish completed by the Chef must wait until next turn.");
                    continue;
                }
                int id = ui.promptOrderId(restaurant.getOrdersWithStatus(OrderStatus.READY));
                if(id == 0){
                    continue;
                }
                if(!restaurant.getWaiter().serveOrder(restaurant.getOrder(id), turn)){
                    ui.displayError("Selected order is not READY.");
                    continue;
                }
                ui.displayMessage("Order " + id + " SERVED.");
                break;
            } else {
                ui.displayError("Choose a Waiter task from 0 to 3.");
            }
        }
        while(true){
            ui.displayMessage("Chef assignment:");
            int id = ui.promptChefTask(restaurant);
            if(id == 0){
                break;
            }
            Order order = restaurant.getOrder(id);
            if(!restaurant.getChef().prepareOrder(order)){
                ui.displayError("Choose an IN_PROGRESS order. No preparation unit was used.");
                continue;
            }
            ui.displayMessage("Chef prepared order " + id + ": " + order.getStatus()
                    + " (" + order.getRemainingPrepUnits() + " units left).");
            break;
        }
    }

    private void processEatingAndPayment(int turn){
        // Pay only customers ready BEFORE this turn, then finish this turn's eating.
        Customer paying = restaurant.getOldestReadyToPayCustomer();
        if(restaurant.getCashier().processPayment(restaurant)){
            ui.displayMessage("Customer " + paying.getCustomerId() + " PAID "
                    + RestaurantUI.money(paying.getCurrentOrder().getPriceCents()) + "; table freed.");
        }
        restaurant.transitionEatenCustomers(turn);
    }

    private void processWaitingAndAbandonment(){
        int previous = restaurant.getUnhappyDepartures();
        restaurant.applyDecayAndCleanupAbandoned();
        if(restaurant.getUnhappyDepartures() > previous){
            ui.displayMessage((restaurant.getUnhappyDepartures() - previous)
                    + " customer(s) left unhappy; their unpaid orders were cancelled.");
        }
    }

    private boolean checkVictory(){
        return restaurant.evaluateVictoryTargets();
    }
}
