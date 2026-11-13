package stage3;

/** Food is chosen by the customer, never by the manager. */
public interface IOrderChoiceStrategy {
    MenuItem selectMenuItem(MenuItem[] menu, Customer customer);
}
