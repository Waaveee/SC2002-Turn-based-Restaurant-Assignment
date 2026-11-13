package stage3;

// Can be activated once, for the current turn and the next turn.
public class HappyHourPolicy {
    private boolean activatedOnce = false;
    private int remainingTurns = 0;

    public boolean canActivate(){
        return activatedOnce == false;
    }

    public boolean isActive(){
        return remainingTurns > 0;
    }

    public int getRemainingTurns(){
        return remainingTurns;
    }

    public boolean activate(){
        if(activatedOnce){
            return false;
        }
        activatedOnce = true;
        remainingTurns = 2;
        return true;
    }

    public void applyTurnStartRecovery(Customer[] customers){
        if(isActive()){
            for(int i=0; i<customers.length; i++){
                // The customer checks whether they are still waiting for service.
                customers[i].recoverSatisfaction(15);
            }
        }
    }

    public int calculateDiscountedPrice(int priceCents){
        if(isActive()){
            return (priceCents * 80 + 50) / 100;
        }
        return priceCents;
    }

    public void decrementTurn(){
        if(remainingTurns > 0){
            remainingTurns--;
        }
    }
}
