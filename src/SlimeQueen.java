
import javax.swing.ImageIcon;

public class SlimeQueen extends Enemy {

    //all the stats for the enemy
    public SlimeQueen() {
        super();
        image = new ImageIcon(getClass().getResource("SlimeQueen.gif"));
        name = "Slime Queen";
        expdrop = (int)(Math.random() * (20 - 10) + 10);
        gpdrop =  (int)(Math.random() * (50 - 10) + 10);
        health = 10;
        curhealth = health;
        attack = 20;
        defense = 5;
    }

    @Override
    public void reduceDamage() {
        if (defense % p.getPlayerAttack() < defense * 0.01);
        defense = defense/(defense / p.getPlayerAttack());
    }
    
    
}
