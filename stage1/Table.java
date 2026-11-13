package stage1;

public class Table {
    private final int tableId;
    private Customer occupant;

    public Table(int tableId){
        this.tableId = tableId;
    }

    public int getTableId(){
        return tableId;
    }
    public Customer getOccupant(){
        return occupant;
    }
    public boolean isFree(){
        return occupant == null;
    }

    public boolean seatCustomer(Customer customer){
        if(!isFree() || customer == null || customer.getState() != CustomerState.WAITING){
            return false;
        }
        if(customer.markSeated() == false){
            return false;
        }
        occupant = customer;
        return true;
    }

    public boolean freeTable(){
        if(occupant != null && occupant.getState() != CustomerState.PAID
                && occupant.getState() != CustomerState.LEFT){
            return false;
        }
        occupant = null;
        return true;
    }
}
