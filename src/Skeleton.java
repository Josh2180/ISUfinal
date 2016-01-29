
import javax.swing.ImageIcon;

public class Skeleton extends Enemy {

    //all the stats for the enemy
    public Skeleton() {
        super();
        image = new ImageIcon(getClass().getResource("Skeleton.gif"));
        name = "Skeleton";
        expdrop = (int)(Math.random() * (8 - 4) + 4);
        gpdrop =  (int)(Math.random() * (11 - 5) + 5);
        health = 5;
        curhealth = health;
        attack = 3;
        defense = 1;
    }
    
    @Override
    public void reduceDamage() {
        if (defense % p.getPlayerAttack() < defense * 0.01);
        defense *= p.getPlayerAttack()/defense;
    }
    
}
