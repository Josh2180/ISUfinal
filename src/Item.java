
public class Item {
    
    //declaring variables for an item
    protected String name;
    protected int attack;
    protected int defense;
    protected int cost;
    
    //default constructor has nothing because the parent class is not actually a
    //usable item itself
    public Item(){
        defense = 0;
    }
    
    //code if you want to add your own stats to items
    public Item(String n, int a, int d, int c){
        name = n;
        attack = a;
        defense = d;
        cost = c;
    }
    
    //getters
    public String getItemName(){
        return name;
    }
    
    public int getItemAttack(){
        return attack;
    }
    
    public int getItemDefense(){
        return defense;
    }     
    
    public int getCost(){
        return cost;
    }
    
}
