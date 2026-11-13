package stage3;

// A combo contains two existing menu items.
public class ComboMeal extends MenuItem {
    private MenuItem[] components = new MenuItem[2];

    public ComboMeal(String name, MenuItem first, MenuItem second){
        super(name);
        components[0] = first;
        components[1] = second;
    }

    public MenuItem[] getComponents(){
        return components.clone();
    }

    public int getPriceCents(){
        int combined = components[0].getPriceCents() + components[1].getPriceCents();
        // Keep 90%. Adding 50 before dividing rounds to the nearest cent.
        return (combined * 90 + 50) / 100;
    }

    public int getPrepUnits(){
        return components[0].getPrepUnits() + components[1].getPrepUnits();
    }
}
