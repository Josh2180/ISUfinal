
public class Potion extends Item {
    
    int heal;
    
    public Potion(String n, int h, int c){
        heal = h;
        name = n;
        cost = c;
    }


    @Override
    public int getItemAttack(){
        return heal;  
    }
}
