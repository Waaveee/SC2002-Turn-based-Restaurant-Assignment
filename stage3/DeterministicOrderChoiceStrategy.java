package stage3;

/**
 * Documented deterministic rule: arrival IDs 1, 2, 3 choose Pasta, Burger,
 * Salad respectively; the cycle then repeats for IDs 4, 5, 6.
 * Stage 3: every fourth arrival using this strategy chooses the fourth menu
 * entry, Salad + Pasta. Critics override this strategy and still choose Pasta.
 * The base menu is ordered Salad, Burger, Pasta. No randomness is used.
 */
public class DeterministicOrderChoiceStrategy implements IOrderChoiceStrategy {
    public MenuItem selectMenuItem(MenuItem[] menu, Customer customer){
        if(menu == null || menu.length < 3 || customer == null){
            return null;
        }
        if(customer.getCustomerId() % 4 == 0 && menu.length >= 4){
            return menu[3];
        }
        return menu[2 - (customer.getCustomerId() - 1) % 3];
    }
}
