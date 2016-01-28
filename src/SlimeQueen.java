
import javax.swing.ImageIcon;

public class SlimeQueen extends Enemy {

    public SlimeQueen() {
        image = new ImageIcon(getClass().getResource("SlimeQueen.gif"));
        name = "Slime Queen";
        expdrop = (int)(Math.random() * (20 - 10) + 10);
        gpdrop =  (int)(Math.random() * (50 - 10) + 10);
        health = 10;
        curhealth = health;
        attack = 20;
        defense = 5;
    }
    
    
}
