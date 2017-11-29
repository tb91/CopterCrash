package Items;

import game.GamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class GelbeRocket extends Rocket
{
  private static final long serialVersionUID = 1L;

  public GelbeRocket(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, delay, p);
    this.locked = true;

    this.targetp = this.parent.copter;

    this.parent.setGelbeRockets(this.parent.getGelbeRockets() + 1);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if (this.beschleunigt) {
      if ((getX() < this.targetp.getX()) && (getHorizontalSpeed() <= 100.0D)) {
        setHorizontalSpeed(getHorizontalSpeed() + 0.7D);
      }

      if ((getX() > this.targetp.getX()) && (getHorizontalSpeed() >= -100.0D)) {
        setHorizontalSpeed(getHorizontalSpeed() - 0.7D);
      }

      if ((getY() < this.targetp.getY()) && (getVerticalSpeed() <= 70.0D)) {
        setVerticalSpeed(getVerticalSpeed() + 0.3D);
      }
      if ((getY() > this.targetp.getY()) && (getVerticalSpeed() >= -70.0D)) {
        setVerticalSpeed(getVerticalSpeed() - 0.3D);
      }

    }

    if (this.treibstoff == 0) {
      this.beschleunigt = false;
      if ((getHorizontalSpeed() > 0.0D) && (getX() > this.parent.getWidth())) {
        setRemove(true);
      }

      if ((getHorizontalSpeed() < 0.0D) && (getX() + getWidth() < 0.0D)) {
        setRemove(true);
      }
      if ((getVerticalSpeed() > 0.0D) && (getY() > this.parent.getHeight())) {
        setRemove(true);
      }
      if ((getVerticalSpeed() < 0.0D) && (getY() + getHeight() - this.getPics()[0].getHeight() < 0.0D)) {
        setRemove(true);
      }
      if ((getHorizontalSpeed() < 20.0D) && (getHorizontalSpeed() > -20.0D))
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
    if (getX() < 0.0D) {
      g.setColor(Color.YELLOW);

      g.drawImage(this.parent.lib.markerGelb, 0, (int)(this.y + getHeight() / 2.0D), null);
    } else if (getX() > this.parent.getWidth()) {
      g.setColor(Color.YELLOW);

      g.drawImage(this.parent.lib.markerGelb, this.parent.getWidth() - this.parent.lib.markerGelb.getWidth(), (int)(this.y + getHeight() / 2.0D), null);
    }
    if (getY() < 0.0D)
      g.drawImage(this.parent.lib.markerGelb, (int)this.x, 0, null);
    else if (getY() > this.parent.getHeight())
      g.drawImage(this.parent.lib.markerGelb, (int)this.x, this.parent.getHeight() - this.parent.lib.markerSchwarz.getHeight(), null);
  }
}