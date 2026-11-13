package stage2;

import java.util.Scanner;

// Optional automatic manager decisions for the two reproducible demo runs.
// Inheritance is used in the same way as the shape subclasses in the labs.
public class DemoUI extends RestaurantUI {
    private boolean playToWin;

    public DemoUI(Scanner sc, boolean playToWin){
        super(sc);
        this.playToWin = playToWin;
    }

    public int promptWaiterTask(Restaurant restaurant){
        int task = 0;
        if(playToWin){
            if(restaurant.getOrdersWithStatus(OrderStatus.READY).length > 0){
                task = 3;
            }
            else if(restaurant.getCustomersInState(CustomerState.SEATED).length > 0){
                task = 2;
            }
            else if(restaurant.getWaitingQueue().length > 0 && restaurant.getFreeTable() != null){
                task = 1;
            }
        }
        displayMessage("Demo Waiter decision: " + task);
        return task;
    }

    public int promptChefTask(Restaurant restaurant){
        Order[] orders = restaurant.getOrdersWithStatus(OrderStatus.IN_PROGRESS);
        int id = 0;
        if(playToWin && orders.length > 0){
            id = orders[0].getOrderId();
        }
        displayMessage("Demo Chef order: " + id);
        return id;
    }

    public int promptCustomerId(Customer[] candidates){
        if(candidates.length == 0){
            return 0;
        }
        return candidates[0].getCustomerId();
    }

    public int promptOrderId(Order[] candidates){
        if(candidates.length == 0){
            return 0;
        }
        return candidates[0].getOrderId();
    }

    public int promptTableId(Restaurant restaurant){
        Table table = restaurant.getFreeTable();
        if(table == null){
            return 0;
        }
        return table.getTableId();
    }
}
