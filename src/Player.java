
import java.util.HashMap;

public class Player {
//The player has a LOT of variables
    String name;
    int experience;
    int gold;
    int level;
    int maxhealth;
    int curhealth;
    static int attack;
    int defense;
    int itemcount;
    int count;
    HashMap<Integer, Item> map;

    //empty constructor because you can't make a player out of nothing
    public Player() {

    }

    //this is much nicer, here we can set various aspects of the player
    public Player(String n, int l, int e) {
        name = n;
        level = l;
        experience = e;//
        maxhealth = 20;//player starts with 20 health
        curhealth = maxhealth;//player starts with max health
        attack = 1;//player starts with one attack
        defense = 1;//player starts with one defense
        itemcount = 0;//player starts with no items
        gold = 100;//gold the player has
        count = 0;
        map = new HashMap<Integer, Item>();
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
        //health increases exponentially as well
        maxhealth = (int) (((level - 1) * (level - 1)) + 20);
    }

    //used to give player experience after defeating an enemy
    public void addPlayerExperience(int e) {
        experience += e;
    }

    //getters and setters
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
    public static int getPlayerAttack() {
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

    //current health of the player
    public int getPlayerCurrentHealth() {
        return curhealth;
    }

    //gets the experience needed for the next level
    public int getNextLevel() {
        return (int) ((level * Math.sqrt(10)) * (level * Math.sqrt(10)));
    }

    //gets the experience needed for the previous level
    public int getPreviousLevel() {
        return (int) (((level - 1) * Math.sqrt(10)) * ((level - 1) * Math.sqrt(10)));
    }

    //gets the player attack last level
    public int getPreviousAttack() {
        return (int) (0.5 * (((level - 1) * (level - 1)) - 1) + 1);
    }

    //used for modifying the players current health, healing and taking damage
    public void addPlayerHealth(int h) {
        curhealth += h;
    }

    //used to fully heal the player
    public void setCurrentPlayerHealth() {
        curhealth = maxhealth;
    }

    //adds a bought item to the player's hashmap
    public void addItem(Item item) {
        map.put(count, item);
        count++;
    }

    //removes a sold item from the players hashmap
    public void removeItem(int i) {
        map.remove(i);
        count--;
    }

    //gets the players hashmap
    public HashMap getHashMap() {
        return map;
    }

}
