
import javax.swing.ImageIcon;

public class BlackKnight extends Enemy {

    //all the stats for the enemy
    public BlackKnight() {
        super();
        image = new ImageIcon(getClass().getResource("BlackKnight.gif"));
        name = "Black Knight";
        expdrop = (int)(Math.random() * (30 - 21) + 21);
        gpdrop =  (int)(Math.random() * (17 - 5) + 5);
        health = 15;
        curhealth = health;
        attack = 10;
        defense = 17;
    }
    
    @Override
    public void reduceDamage() {
        if (defense % p.getPlayerAttack() < defense * 0.01);
        defense = defense/(defense / p.getPlayerAttack());
    }
}
