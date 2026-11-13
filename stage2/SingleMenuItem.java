package stage2;

public class SingleMenuItem extends MenuItem {
    private final int priceCents;
    private final int prepUnits;

    public SingleMenuItem(String name, int priceCents, int prepUnits){
        super(name);
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    public int getPriceCents(){
        return priceCents;
    }
    public int getPrepUnits(){
        return prepUnits;
    }
}
