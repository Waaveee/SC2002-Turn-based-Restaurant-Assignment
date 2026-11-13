package stage2;

/** A menu entry exposes its price in cents and its preparation cost. */
public abstract class MenuItem {
    protected final String name;

    protected MenuItem(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public abstract int getPriceCents();
    public abstract int getPrepUnits();
}
