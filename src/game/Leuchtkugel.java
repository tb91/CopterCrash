package game;

import java.awt.image.BufferedImage;


public class Leuchtkugel extends Heli
{
  Heli owner;
  private static final long serialVersionUID = 1L;

  public Leuchtkugel(BufferedImage[] i, long delay, Heli owner, GamePanel p)
  {
    super(i, 0.0D, 0.0D, delay, p);

    this.owner = owner;

    setX(owner.getX() - owner.getWidth());
    setY(owner.getY());

    setVerticalSpeed(0.0D);
    setHorizontalSpeed(0.0D);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if (getVerticalSpeed() < 120.0D) {
      setVerticalSpeed(getVerticalSpeed() + 1.0D);
    }
    if (getY() > this.parent.getHeight())
      setRemove(true);
  }

  public boolean collidedWith(Sprite s)
  {
    return false;
  }
}