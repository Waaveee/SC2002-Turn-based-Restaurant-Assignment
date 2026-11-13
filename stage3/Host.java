package stage3;

/** Independent seating task: using the Host does not consume the Waiter's task. */
public class Host extends Staff {
    public Host(){
        super("Host");
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
}
