
public class Player{

    String name;//player's name
    int experience;//experience the player has
    int gold;
    int level;//player starts at level 1
    int maxhealth;
    int curhealth;
    int attack;//player starts with one attack
    int defense;//player starts with one defense
    int itemcount;
    Item i[] = new Item[10];

    public Player() {
        
    }

    public Player(String n, int l, int e) {
        name = n;
        level = l;
        experience = e;
        maxhealth = 20;
        curhealth = maxhealth;
        attack = 1;//player starts with one attack
        defense = 1;//player starts with one defense
        itemcount = 0;//player starts with no items
        gold = 100;//gold the player has
        i = new Item[10];
    }

    //getter for player name
    public String getPlayerName() {
        return name;
    }

    //updates the player when they level up
    public void PlayerUpdate() {
        //function which makes it so it requires more experience to level up each time
        level = (int) ((1 / Math.sqrt(10) * Math.sqrt(experience)) + 1);
        //function which makes it so you gain more attack and defense per level as you level up
        attack = (int) (0.4 * ((level * level) - 1) + 1);
        defense = (int) (0.2 * ((level * level) - 1) + 1);
        //health is always fifteen times your level
        maxhealth = (int) (((level - 1) * (level - 1)) + 20);
    }

    //used to give player experience after defeating an enemy
    public void addPlayerExperience(int e) {
        experience += e;
    }

    public void addPlayerGold(int g) {
        gold += g;
    }

    public int getPlayerGold() {
        return gold;
    }

    //getteer for player level
    public int getPlayerLevel() {
        return level;
    }

    //getter for player attack
    public int getPlayerAttack() {
        return attack;
    }

    //getter for player defense
    public int getPlayerDefense() {
        return defense;
    }

    //getter for player experience
    public int getPlayerExperience() {
        return experience;
    }

    //getter for player health
    public int getPlayerHealth() {
        return maxhealth;
    }

    public int getPlayerCurrentHealth() {
        return curhealth;
    }

    public int getNextLevel() {
        return (int) ((level * Math.sqrt(10)) * (level * Math.sqrt(10)));
    }

    public int getPreviousLevel() {
        return (int) (((level - 1) * Math.sqrt(10)) * ((level - 1) * Math.sqrt(10)));
    }

    public int getPreviousAttack() {
        return (int) (0.5 * (((level - 1) * (level - 1)) - 1) + 1);
    }

    public void addPlayerHealth(int h) {
        curhealth += h;
    }

    public void setCurrentPlayerHealth() {
        curhealth = maxhealth;
    }

    public void addItem(Item item) {
        i[itemcount] = item;
        itemcount++;
    }


    public void removeItem(Item item) {
        i[itemcount] = item;
        itemcount--;
    }


}
