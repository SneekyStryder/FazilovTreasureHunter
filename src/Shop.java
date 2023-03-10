/**
 * The Shop class controls the cost of the items in the Treasure Hunt game.<p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method.<p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
import java.util.Scanner;

public class Shop
{
    // constants
    private static int WATER_COST = 2;
    private static int ROPE_COST = 4;
    private static int MACHETE_COST = 6;
    private static int HORSE_COST = 12;
    private static int BOAT_COST = 20;
    private static int PICKS_COST = 10;   // Ticket 8

    // instance variables
    private double markdown;
    private Hunter customer;

    //Constructor
    public Shop(double markdown)
    {
        this.markdown = markdown;
        customer = null;
    }

    public static void setToCheatMode() {       // Ticket 5
        WATER_COST = 1;                         // Ticket 5
        ROPE_COST = 1;                          // Ticket 5
        MACHETE_COST = 1;                       // Ticket 5
        HORSE_COST = 1;                         // Ticket 5
        BOAT_COST = 1;                          // Ticket 5
        PICKS_COST = 1;                         // Ticket 5
    }

    /** method for entering the shop
     * @param hunter  the Hunter entering the shop
     * @param buyOrSell  String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell)
    {
        customer = hunter;

        Scanner scanner = new Scanner(System.in);
        if (buyOrSell.equals("B") || buyOrSell.equals("b"))
        {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String userResponse = scanner.nextLine();
            String firstLetter = userResponse.toUpperCase();    // Ticket 3
            String item = firstLetter.substring(0, 1) + userResponse.substring(1);  // Ticket 3
            int cost = checkMarketPrice(item, true);
            if (cost == 0)
            {
                System.out.println("We ain't got none of those.");
            }
            else
            {
                System.out.print("It'll cost you " + cost + " gold. Buy it (y/n)? ");
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    buyItem(item);
                }
            }
        }
        else
        {
            System.out.println("What're you lookin' to sell? ");
            System.out.print("You currently have the following items: " + customer.getInventory());
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, false);
            if (cost == 0)
            {
                System.out.println("We don't want none of those.");
            }
            else
            {
                System.out.print("It'll get you " + cost + " gold. Sell it (y/n)? ");
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    sellItem(item);
                }
            }
        }
    }

    /** A method that returns a string showing the items available in the shop (all shops sell the same items)
     *
     * @return  the string representing the shop's items available for purchase and their prices
     */
    public String inventory()
    {
        String str = "Water: " + WATER_COST + " gold\n";
        str += "Rope: " + ROPE_COST + " gold\n";
        str += "Machete: " + MACHETE_COST + " gold\n";
        str += "Picks: " + PICKS_COST + " gold\n";  // Ticket 8
        str += "Horse: " + HORSE_COST + " gold\n";
        str += "Boat: " + BOAT_COST + " gold\n";

        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     * @param item The item being bought.
     */
    public void buyItem(String item)
    {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem))
        {
            System.out.println("Ye' got yerself a " + item + ". Come again soon.");
        }
        else
        {
            System.out.println("Hmm, either you don't have enough gold or you've already got one of those!");
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     * @param item The item being sold.
     */
    public void sellItem(String item)
    {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice))
        {
            System.out.println("Pleasure doin' business with you.");
        }
        else
        {
            System.out.println("Stop stringin' me along!");
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying)
    {
        if (isBuying)
        {
            return getCostOfItem(item);
        }
        else
        {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item)
    {
        if (item.equals("Water") || item.equals("water"))
        {
            return WATER_COST;
        }
        else if (item.equals("Rope") || item.equals("rope"))
        {
            return ROPE_COST;
        }
        else if (item.equals("Machete") || item.equals("machete"))
        {
            return MACHETE_COST;
        }
        else if (item.equals("Picks") || item.equals("picks"))  // Ticket 8
        {
            return PICKS_COST;  // Ticket 8
        }
        else if (item.equals("Horse") || item.equals("horse"))
        {
            return HORSE_COST;
        }
        else if (item.equals("Boat") || item.equals("boat"))
        {
            return BOAT_COST;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item)
    {
        int cost = (int)(getCostOfItem(item) * markdown);
        return cost;
    }
}