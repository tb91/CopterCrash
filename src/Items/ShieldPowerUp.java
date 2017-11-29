package Items;

import game.GamePanel;
import game.PowerUp;
import game.Sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ShieldPowerUp extends PowerUp
{
  private static final long serialVersionUID = 1L;

  public ShieldPowerUp(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, delay, p);
    this.parent.incAktuOtherPowerUp();
  }

  public void doPowerUp(Sprite s)
  {
    this.parent.copter.activateShield();

    setRemove(true);
  }

  public void drawObjects(Graphics g)
  {
    super.drawObjects(g);
  }
  
  @Override
  public void setRemove(boolean t){
	  this.parent.decAktuOtherPowerUp();
	  super.setRemove(t);
  }
}