
import javax.swing.ImageIcon;

public class IceGolem extends Enemy {

    public IceGolem() {
        image = new ImageIcon(getClass().getResource("IceGolem.gif"));
        name = "Ice Golem";
        expdrop = (int)(Math.random() * (25 - 10) + 10);
        gpdrop =  (int)(Math.random() * (50 - 15) + 15);
        health = 20;
        curhealth = health;
        attack = 10;
        defense = 8;
    }
    
    
}
