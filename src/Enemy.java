
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Enemy {

    protected String name;
    protected int expdrop;
    protected int gpdrop;
    protected int health;
    protected int curhealth;
    protected int attack;
    protected int defense;
    protected ImageIcon image;

    public Enemy() {

    }
    
    public Icon getImage() {
        return image;
    }

    public String getEnemyName() {
        return name;
    }

    public int getEnemyExpdrop() {
        return expdrop;
    }

    public int getEnemyGpdrop() {
        return gpdrop;
    }

    public int getEnemyHealth() {
        return health;
    }

    public int getEnemyAttack() {
        return attack;
    }

    public int getEnemyDefense() {
        return defense;
    }

    public int getCurhealth() {
        return curhealth;
    }

    public void setCurhealth(int curhealth) {
        this.curhealth -= curhealth;
    }
}
