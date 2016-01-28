
import javax.swing.ImageIcon;

public class Slime extends Enemy {

    public Slime() {
        image = new ImageIcon(getClass().getResource("Slime.gif"));
        name = "Slime";
        expdrop = (int)(Math.random() * (5 - 2)  + 2);
        gpdrop =  (int)(Math.random() * (7 - 3) + 3);
        health = 3;
        curhealth = health;
        attack = 2;
        defense = 0;
    }
    
    
}
