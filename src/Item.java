
public abstract class Item {
    
    protected String name;
    protected int attack;
    protected int defense;
    protected int cost;
    
    public Item(){
        
    }
    
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
