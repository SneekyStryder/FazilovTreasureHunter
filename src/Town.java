/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean treasureHunted = false;     // Ticket 1

    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            printMessage += fightAnimation();                       // Ticket 4
            double chance = 0;
            int goldDiff = 0;
            if (TreasureHunter.isEasyMode()) {                      // Ticket 2
                goldDiff = ((int) (Math.random() * 10) + 1) + 7;    // Ticket 2
                chance = Math.random() + 0.4;                       // Ticket 2
            }
            else if (TreasureHunter.isCheatMode()) {                // Ticket 5
                goldDiff = 100;                                     // Ticket 5
                chance = 100;                                       // Ticket 5
            }
            else {
                goldDiff = (int) (Math.random() * 10) + 1;
                chance = Math.random();
            }
            if (chance > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
            }
            if (hunter.getGold() <= 0) {                            // Ticket 1
                hunter.lose = true;                                 // Ticket 1
            }
        }
    }

    public String fightAnimation() {                                // Ticket 4
        int random = (int) ((Math.random() * 4) + 1);               // Ticket 4
        if (random == 1) {                                          // Ticket 4
            return "(ノಠ益ಠ)ノ彡┻━┻    ლ(ಠ益ಠლ)\n";                // Ticket 4
        }                                                           // Ticket 4
        else if (random == 2) {                                     // Ticket 4
            return "(ง'̀-'́)ง     ᕦ(ò_óˇ)ᕤ\n";                       // Ticket 4
        }                                                           // Ticket 4
        else if (random == 3) {                                     // Ticket 4
            return "ಠ_ಠ     ლ(▀̿Ĺ̯̿▀̿ლ)\n";                       // Ticket 4
        }                                                           // Ticket 4
        else if (random == 4) {                                     // Ticket 4
            return "(◣_◢)     ━╤デ╦︻(▀̿Ĺ̯̿▀̿)\n";                  // Ticket 4
        }                                                           // Ticket 4
        return "0";                                                 // Ticket 4
    }

    public void goTreasureHunting() {                               // Ticket 1
        int random = (int) ((Math.random() * 4) + 1);               // Ticket 1
        if (random == 1) {                                          // Ticket 1
            System.out.println("You found a Sapphire!");            // Ticket 1
            hunter.addTreasure(1);                      // Ticket 1
        }
        else if (random == 2) {                                     // Ticket 1
            System.out.println("You found an Emerald!");            // Ticket 1
            hunter.addTreasure(2);                      // Ticket 1
        }
        else if (random == 3) {                                     // Ticket 1
            System.out.println("You found a Diamond!");             // Ticket 1
            hunter.addTreasure(3);                      // Ticket 1
        }
        else if (random == 4) {                                     // Ticket 1
            System.out.println("You found nothing!");               // Ticket 1
        }
        treasureHunted = true;                                      // Ticket 1
    }

    public boolean isTreasureHunted() {
        return treasureHunted;
    }    // Ticket 1

    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else if (rnd < .9)                                      // Ticket 8
        {
            return new Terrain("Canyon", "Picks");  // Ticket 8
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }
}