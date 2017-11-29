package Items;

import game.GamePanel;
import game.Heli;
import game.PowerUp;
import game.Sprite;

import java.awt.image.BufferedImage;

public class LeuchtkugelPowerUp extends PowerUp
{
  private static final long serialVersionUID = 1L;

  public LeuchtkugelPowerUp(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, delay, p);
    this.parent.incAktuOtherPowerUp();
  }

  public void doPowerUp(Sprite s)
  {
    if (((Heli)s).leuchtkugelanz < 3) {
      ((Heli)s).leuchtkugelanz += 1;
    }
    setRemove(true);
  }
  
  @Override
  public void setRemove(boolean t){
	  this.parent.decAktuOtherPowerUp();
	  super.setRemove(t);
  }
}