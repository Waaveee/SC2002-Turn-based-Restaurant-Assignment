package stage1;

/** Exactly six arrivals, one on each listed turn. IDs preserve arrival order. */
public class ArrivalSchedule {
    private final int[] arrivalTurns = {1, 4, 7, 10, 13, 16};

    public Customer getScheduledCustomer(int turn){
        for(int i = 0; i < arrivalTurns.length; i++){
            if(arrivalTurns[i] == turn){
                return createCustomer(i + 1);
            }
        }
        return null;
    }

    private Customer createCustomer(int id){
        return new RegularCustomer(id);
    }
}
