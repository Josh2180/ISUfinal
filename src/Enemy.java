
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class Enemy {

    //various declarations
    static String name;
    protected int expdrop;
    protected int gpdrop;
    protected int health;
    protected int curhealth;
    protected int attack;
    protected int defense;
    protected ImageIcon image;
    final Player p;

    //empty because the parent class isn't actually an enemy
    public Enemy() {
        p = new Player();
    }
    
    //for making your own enemy
    public Enemy(ImageIcon i, String n, int e, int g, int h, int a, int d){
        p = new Player();
        image = i;
        name = n;
        expdrop = e;
        gpdrop = g;
        health = h;
        curhealth = health;
        attack = a;
        defense = d;
    }
    
    //getters and setters
    public final Icon getImage() {
        return image;
    }

    public final static String getEnemyName() {
        return name;
    }

    public final int getEnemyExpdrop() {
        return expdrop;
    }

    public final int getEnemyGpdrop() {
        return gpdrop;
    }

    public final int getEnemyHealth() {
        return health;
    }

    public final int getEnemyAttack() {
        return attack;
    }

    public final int getEnemyDefense() {
        return defense;
    }

    public final int getCurhealth() {
        return curhealth;
    }

    public void setCurhealth(int curhealth) {
        this.curhealth -= curhealth;
    }
    
    //abstract method
    public abstract void reduceDamage();
}
