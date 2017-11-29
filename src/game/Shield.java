package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shield extends Heli
{
  private static final long serialVersionUID = 1L;

  public Shield(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, -130.0D, 0.0D, delay, p);

    setVerticalSpeed(0.0D);
    setHorizontalSpeed(0.0D);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);
    setX(this.parent.copter.getX() - 25.0D);
    setY(this.parent.copter.getY() - 22.0D);
    if (this.parent.copter.getShieldactive() <= 0) {	//XXX: set copter as owner
      this.parent.copter.shieldactivated = false;
      setRemove(true);
    }
  }

  public void drawObjects(Graphics g)
  {
    super.drawObjects(g);
    int t = (int)(parent.copter.getShieldactive() / 1000L);
    g.setColor(Color.YELLOW);
    g.drawString(Integer.toString(t), (int)this.x, (int)this.y);
  }
}