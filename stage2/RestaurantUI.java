package stage2;

import java.util.Scanner;

/** Boundary only: renders state and collects decisions; it does not apply rules. */
public class RestaurantUI {
    private Scanner sc;
    private boolean endOfInputReported;

    public RestaurantUI(Scanner sc){
        this.sc = sc;
    }

    public void displayMessage(String message){
        System.out.println(message);
    }
    public void displayError(String reason){
        System.out.println("Invalid choice: " + reason);
    }
    // Store money in cents; add a leading zero for amounts such as $9.05.
    public static String money(int cents){
        int dollars = cents / 100;
        int remainder = cents % 100;
        if(remainder < 10){
            return "$" + dollars + ".0" + remainder;
        }
        return "$" + dollars + "." + remainder;
    }

    private String average(Restaurant restaurant){
        if(restaurant.getPaidCustomerCount() == 0){
            return "N/A";
        }
        // Round to two decimal places for display only.
        int hundredths = (int) (restaurant.getAveragePaidSatisfaction() * 100 + 0.5);
        int whole = hundredths / 100;
        int fraction = hundredths % 100;
        if(fraction < 10){
            return whole + ".0" + fraction;
        }
        return whole + "." + fraction;
    }

    public void displayStartOfTurn(Restaurant restaurant, int turn){
        System.out.println("\n========== TURN " + turn + " / 18 ==========");
        System.out.println("Revenue: " + money(restaurant.getRevenueCents())
                + " | Paid: " + restaurant.getPaidCustomerCount()
                + " | Average paid satisfaction: " + average(restaurant));
        System.out.println("Waiting queue:");
        if(restaurant.getWaitingQueue().length == 0){
            System.out.println("  (empty)");
        }
        for(Customer customer : restaurant.getWaitingQueue()){
            displayCustomer(customer);
        }
        System.out.println("Tables:");
        for(Table table : restaurant.getTables()){
            System.out.print("  Table " + table.getTableId() + ": ");
            if(table.isFree()){
                System.out.println("FREE");
            }
            else{
                displayCustomer(table.getOccupant());
            }
        }
        System.out.println("Menu:");
        for(MenuItem item : restaurant.getMenu()){
            System.out.println("  " + item.getName() + " " + money(item.getPriceCents())
                    + " (" + item.getPrepUnits() + " prep units)");
        }
        System.out.println("Waiter: seat / take order / serve READY order / wait (one task).");
        System.out.println("Chef: prepare one unit / wait. Cashier: one automatic payment.");
    }

    private void displayCustomer(Customer customer){
        System.out.print("  Customer " + customer.getCustomerId() + " " + customer.getTypeName()
                + " | satisfaction " + customer.getSatisfaction() + " | " + customer.getState());
        Order order = customer.getCurrentOrder();
        if(order != null){
            System.out.print(" | Order " + order.getOrderId() + ": " + order.getDish().getName()
                    + " " + order.getStatus() + " | " + order.getRemainingPrepUnits()
                    + " units left | bill " + money(order.getLockedPriceCents()));
        }
        System.out.println();
    }

    public int promptWaiterTask(Restaurant restaurant){
        return readInt("Waiter [0 Wait, 1 Seat, 2 Take order, 3 Serve]: ");
    }

    public int promptChefTask(Restaurant restaurant){
        return promptOrderId(restaurant.getOrdersWithStatus(OrderStatus.IN_PROGRESS));
    }

    public int promptCustomerId(Customer[] candidates){
        System.out.print("Eligible customer IDs:");
        for(Customer customer : candidates){
            System.out.print(" " + customer.getCustomerId());
        }
        System.out.println();
        return readInt("Customer ID (0 cancels): ");
    }

    public int promptTableId(Restaurant restaurant){
        System.out.print("Free table IDs:");
        for(Table table : restaurant.getTables()){
            if(table.isFree()){
                System.out.print(" " + table.getTableId());
            }
        }
        System.out.println();
        return readInt("Table ID (0 cancels): ");
    }

    public int promptOrderId(Order[] candidates){
        System.out.print("Eligible order IDs:");
        for(Order order : candidates){
            System.out.print(" " + order.getOrderId() + "(" + order.getDish().getName() + ")");
        }
        System.out.println();
        return readInt("Order ID (0 waits/cancels): ");
    }

    protected int readInt(String prompt){
        while(true){
            System.out.print(prompt);
            // hasNext() also handles input ending during a replay.
            if(sc.hasNext() == false){
                if(endOfInputReported == false){
                    System.out.println("End of input: remaining decisions default to Wait.");
                    endOfInputReported = true;
                }
                return 0;
            }
            // Check before nextInt(), so words and oversized numbers do not crash the game.
            if(sc.hasNextInt()){
                int choice = sc.nextInt();
                return choice;
            }
            else{
                sc.next(); // Discard the invalid input before asking again.
                displayError("Please enter a whole number within the integer range.");
            }
        }
    }

    public void displayEndOfTurn(Restaurant restaurant, int turn){
        System.out.println("End of turn " + turn + ": Served=" + restaurant.getServedCustomerCount()
                + ", Paid=" + restaurant.getPaidCustomerCount()
                + ", Unhappy departures=" + restaurant.getUnhappyDepartures()
                + ", Revenue=" + money(restaurant.getRevenueCents())
                + ", Average paid satisfaction=" + average(restaurant)
                + ", Turns used=" + turn + "/18");
    }

    public void displayFinalResult(boolean victory, Restaurant restaurant){
        if(victory){
            System.out.println("\nVICTORY");
        }
        else{
            System.out.println("\nDEFEAT");
        }
        System.out.println("Targets: at least 4 paid, revenue at least $45.00, average satisfaction at least 60.");
        displayEndOfTurn(restaurant, 18);
    }
}
