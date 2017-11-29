package Items;

import game.GamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class SchwarzeRocket extends Rocket
{
  Point reff;
  private static final long serialVersionUID = 1L;

  public SchwarzeRocket(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, delay, p);
    this.targetp = this.parent.copter;
    this.reff = new Point(0, 0);
    this.locked = true;

    this.parent
			.setSchwarzeRockets(this.parent.getSchwarzeRockets() + 1);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if (this.locked)
    {
      this.reff = new Point((int)(this.targetp.getX() + this.targetp.getPics()[0].getWidth() / 2 + this.targetp.getHorizontalSpeed()), (int)(this.targetp.getY() + this.targetp.getPics()[0].getHeight() / 2 + this.targetp.getVerticalSpeed()));

      if (this.beschleunigt) {
        if ((getX() < this.targetp.getX()) && 
          (getX() < this.reff.getX()) && (getHorizontalSpeed() <= 100.0D) && 
          (getHorizontalSpeed() - 40.0D < this.targetp.getHorizontalSpeed())) {
          setHorizontalSpeed(getHorizontalSpeed() + 1.0D);
        }

        if ((getX() > this.targetp.getX()) && 
          (getX() > this.reff.getX()) && (getHorizontalSpeed() >= -100.0D) && 
          (getHorizontalSpeed() + 40.0D > this.targetp.getHorizontalSpeed())) {
          setHorizontalSpeed(getHorizontalSpeed() - 1.0D);
        }

        if ((getY() < this.targetp.getY()) && 
          (getY() < this.reff.getY()) && (getVerticalSpeed() <= 70.0D) && 
          (getVerticalSpeed() - 40.0D < this.targetp.getVerticalSpeed())) {
          setVerticalSpeed(getVerticalSpeed() + 0.6D);
        }

        if ((getY() > this.targetp.getY()) && 
          (getY() > this.reff.getY()) && (getVerticalSpeed() >= -70.0D) && 
          (getVerticalSpeed() + 40.0D > this.targetp.getVerticalSpeed())) {
          setVerticalSpeed(getVerticalSpeed() - 0.6D);
        }

      }
      else if ((getHorizontalSpeed() < 20.0D) && (getHorizontalSpeed() > -20.0D))
      {
        if ((getVerticalSpeed() >= 0.0D) && (getVerticalSpeed() < 80.0D)) {
          setVerticalSpeed(getVerticalSpeed() + 1.0D);
        }
        else if ((getVerticalSpeed() > -80.0D) && (getVerticalSpeed() < 0.0D))
          setVerticalSpeed(getVerticalSpeed() - 1.0D);
      }
    }
  }

  public void drawObjects(Graphics g)
  {
    super.drawObjects(g);
    super.drawObjects(g);
    if (getX() < 0.0D) {
      g.setColor(Color.YELLOW);

      g.drawImage(this.parent.lib.markerSchwarz, 0, (int)(this.y - 7.0D), null);
    } else if (getX() > this.parent.getWidth()) {
      g.setColor(Color.YELLOW);

      g.drawImage(this.parent.lib.markerSchwarz, this.parent.getWidth() - this.parent.lib.markerSchwarz.getWidth(), (int)this.y, null);
    }
    if (getY() < 0.0D)
      g.drawImage(this.parent.lib.markerSchwarz, (int)this.x, 0, null);
    else if (getY() > this.parent.getHeight())
      g.drawImage(this.parent.lib.markerSchwarz, (int)this.x, this.parent.getHeight() - this.parent.lib.markerSchwarz.getHeight(), null);
  }
}