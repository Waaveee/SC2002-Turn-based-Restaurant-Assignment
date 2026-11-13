package stage1;

public class RegularCustomer extends Customer {
    private static final int DECAY_RATE = 8;

    public RegularCustomer(int customerId){
        super(customerId);
    }
    public String getTypeName(){
        return "Regular";
    }
    protected int getWaitingDecay(){
        return DECAY_RATE;
    }
}
