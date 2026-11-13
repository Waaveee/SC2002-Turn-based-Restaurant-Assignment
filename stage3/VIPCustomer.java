package stage3;

public class VIPCustomer extends Customer {
    private static final int DECAY_RATE = 5;

    public VIPCustomer(int customerId){
        super(customerId);
    }
    public String getTypeName(){
        return "VIP";
    }
    protected int getWaitingDecay(){
        return DECAY_RATE;
    }

    /** Retain 90%, rounding half a cent up using integer arithmetic. */
    public int applyCustomerDiscount(int baseCents){
        return (int) ((baseCents * 90L + 50) / 100);
    }
}
