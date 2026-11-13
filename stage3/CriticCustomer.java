package stage3;

/** Documented preference: a Critic always orders Pasta, ignoring the cycle. */
public class CriticCustomer extends Customer {
    private static final int DECAY_RATE = 12;

    public CriticCustomer(int customerId){
        super(customerId);
    }
    public String getTypeName(){
        return "Critic";
    }
    protected int getWaitingDecay(){
        return DECAY_RATE;
    }

    public MenuItem chooseFood(MenuItem[] menu, IOrderChoiceStrategy strategy){
        for(MenuItem item : menu){
            if(item.getName().equals("Pasta")){
                return item;
            }
        }
        return null;
    }
}
