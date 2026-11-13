package stage2;

public class Chef extends Staff {
    public Chef(){
        super("Chef");
    }

    public boolean prepareOrder(Order order){
        if(!isAvailable() || order == null || order.getStatus() != OrderStatus.IN_PROGRESS){
            return false;
        }
        if(order.advancePreparation() == false){
            return false;
        }
        busyThisTurn = true;
        return true;
    }
}
