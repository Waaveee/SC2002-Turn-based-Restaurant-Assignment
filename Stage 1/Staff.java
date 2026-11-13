package stage1;

public abstract class Staff {
    protected final String staffName;
    protected boolean busyThisTurn;

    protected Staff(String staffName){ 
        this.staffName = staffName; 
    }
    public String getStaffName(){ 
        return staffName; 
    }
    public void resetTurn(){ 
        busyThisTurn = false; 
    }
    public boolean isAvailable(){ 
        return !busyThisTurn; 
    }
}
