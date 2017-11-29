package Items;

import game.GamePanel;
import game.Sprite;

import java.awt.image.BufferedImage;

public class Cloud extends Sprite
{
  private static final long serialVersionUID = 1L;
  final int SPEED = 20;

  public Cloud(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
    super(i, x, y, delay, p);

    if ((int)(Math.random() * 2.0D) < 1)
      setHorizontalSpeed(-20.0D);
    else
      setHorizontalSpeed(20.0D);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if ((getHorizontalSpeed() > 0.0D) && (getX() > this.parent.getWidth())) {
      this.x = (-getWidth());
    }

    if ((getHorizontalSpeed() < 0.0D) && (getX() + getWidth() < 0.0D))
      this.x = this.parent.getWidth();
  }

  public boolean collidedWith(Sprite s)
  {
    return false;
  }
}