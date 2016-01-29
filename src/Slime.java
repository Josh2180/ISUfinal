
import javax.swing.ImageIcon;

public class Slime extends Enemy {

    //all the stats for the enemy
    public Slime() {
        super();
        image = new ImageIcon(getClass().getResource("Slime.gif"));
        name = "Slime";
        expdrop = (int)(Math.random() * (5 - 2)  + 2);
        gpdrop =  (int)(Math.random() * (7 - 3) + 3);
        health = 3;
        curhealth = health;
        attack = 2;
        defense = 0;
    }

    @Override
    public void reduceDamage() {
        if (defense % p.getPlayerAttack() < defense * 0.01);
        defense = defense/(defense / p.getPlayerAttack());
    }
    
    
}
