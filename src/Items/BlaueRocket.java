package Items;

import game.GamePanel;

import java.awt.image.BufferedImage;

public class BlaueRocket extends Rocket
{
  private static final long serialVersionUID = 1L;
  int verticalspeed;

  public BlaueRocket(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, delay, p);
    this.targetp = this.parent.copter;
    this.verticalspeed = this.parent.constants.defaultRocketYSpeed;
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if (getHorizontalSpeed() > 0.0D) {
      if ((getX() > this.targetp.getX()) && (this.locked)) {
        this.locked = false;
      }

    }
    else if ((getX() < this.targetp.getX()) && (this.locked)) {
      this.locked = false;
    }

    if (this.locked) {
      if (getY() < this.targetp.getY()) {
        setVerticalSpeed(this.parent.constants.defaultRocketYSpeed);
      }
      if (getY() > this.targetp.getY() + this.targetp.getHeight() / 2.0D) {
        setVerticalSpeed(-this.parent.constants.defaultRocketYSpeed);
      }
      if (this.targetp.intersects(this.target))
        addySpeed(-1.0D);
    }
  }
}