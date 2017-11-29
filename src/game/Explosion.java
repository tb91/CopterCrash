package game;

import java.awt.image.BufferedImage;


public class Explosion extends Sprite
{
  private static final long serialVersionUID = 1L;
  int old_pic = 0;

  public Explosion(BufferedImage[] i, double x, double y, long delay, GamePanel p)
  {
    super(i, x, y, delay, p);
  }

  public void doLogic(long delta)
  {
    this.old_pic = this.currentpic;
    super.doLogic(delta);
    if ((this.currentpic == 0) && (this.old_pic != 0))
      setRemove(true);
  }

  public boolean collidedWith(Sprite s)
  {
    return false;
  }
}