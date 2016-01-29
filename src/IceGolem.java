
import javax.swing.ImageIcon;

public class IceGolem extends Enemy {

    //all the stats for the enemy
    public IceGolem() {
        super();
        image = new ImageIcon(getClass().getResource("IceGolem.gif"));
        name = "Ice Golem";
        expdrop = (int)(Math.random() * (25 - 10) + 10);
        gpdrop =  (int)(Math.random() * (50 - 15) + 15);
        health = 20;
        curhealth = health;
        attack = 10;
        defense = 8;
    }
    
    @Override
    public void reduceDamage() {
        if (defense % p.getPlayerAttack() < defense * 0.01);
        defense = defense/(defense / p.getPlayerAttack());
    }
}
