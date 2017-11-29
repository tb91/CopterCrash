package Items;

import game.GamePanel;
import game.Heli;
import game.Leuchtkugel;
import game.Shield;
import game.Sprite;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

/**
 * @author Tim
 *
 */
public abstract class Rocket extends Sprite
{
  private static final long serialVersionUID = 1L;
  int verticalspeed;
  Rectangle2D.Double target;
  Heli targetp;
  protected boolean locked;
  boolean beschleunigt;
  int treibstoff;

  public Rocket(BufferedImage[] i, long delay, GamePanel p)
  {
    super(i, 0.0D, 0.0D, delay, p);

    this.beschleunigt = true;
    this.treibstoff = ((int)(Math.random() * 500.0D) + this.parent.constants.minFuel);
    this.locked = false;
    initializeSpeed();
  }

  private void initializeSpeed()
  {
    int x = 0;
    int y = (int)(Math.random() * this.parent.getHeight());

    if (Math.abs(this.parent.copter.getY() + this.parent.copter.getHeight() / 2.0D - y) < 40.0D)
    {
      y = (int)(Math.random() * 2.0D);
      if (y == 0) {
        y = (int)(this.parent.copter.getY() - 2.0D * this.parent.copter.getHeight());
      }
      else {
        y = (int)(this.parent.copter.getY() + 2.0D * this.parent.copter.getHeight());
      }
    }

    int xspeed = this.parent.constants.defaultRocketXSpeed;
    if ((int)Math.random() * 2 == 0)
      xspeed += (int)Math.random() * 10;
    else {
      xspeed -= (int)Math.random() * 10;
    }

    int yspeed = this.parent.constants.defaultRocketYSpeed;
    if ((int)Math.random() * 2 == 0)
      yspeed += (int)Math.random() * 5;
    else {
      yspeed -= (int)Math.random() * 5;
    }

    int hori = (int)(Math.random() * 2.0D);

    if (hori == 0)
      x = -30;
    else {
      x = this.parent.getWidth() + 30;
    }
    if (x < 0)
      setHorizontalSpeed(xspeed);
    else {
      setHorizontalSpeed(-xspeed);
    }
    setX(x);
    setY(y);

    if (getY() < this.parent.getHeight() / 2)
      setVerticalSpeed(yspeed);
    else
      setVerticalSpeed(-yspeed);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if (getHorizontalSpeed() > 0.0D) {
      this.target = new Rectangle2D.Double(getX() + getWidth(), getY(), this.parent.getWidth() - getX(), getHeight());
    }
    else {
      this.target = new Rectangle2D.Double(0.0D, getY(), getX(), getHeight());
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
      if ((getVerticalSpeed() < 0.0D) && (getY() + getHeight() < 0.0D))
        setRemove(true);
    }
    else {
      this.treibstoff -= 1;
    }

    if ((!this.locked) && (this.parent.copter.intersects(this.target))) {
      this.locked = true;
      this.targetp = this.parent.copter;
    }

    for (ListIterator<Leuchtkugel> it = this.parent.copter.kugeln.listIterator(); it.hasNext(); ) {
      Heli kugel = (Heli)it.next();
      if (kugel.intersects(this.target)) {
        this.locked = true;
        this.targetp = kugel;
        break;
      }
    }
  }
  
  /**
 * @param t
 * creates explosion and removes the rocket
 */
public void destroyRocket(boolean t){
	  this.parent.createExplosion((int)getX(), (int)getY());
	  super.setRemove(t);
  }

  public boolean collidedWith(Sprite s)
  {
    if (getRemove()) {
      return false;
    }

    if (checkOpaqueColorcollisions(s))
    {
      if (((s instanceof Heli)) && (!(s instanceof Leuchtkugel)) && (!(s instanceof Shield))) {
        
    	destroyRocket(true); 
        if (!this.parent.copter.shieldactivated)
        {
          
          this.parent.createExplosion((int)s.getX(), (int)s.getY());
          s.setRemove(true);
          return true;
        }

        this.parent.copter.deactivateShield();
      }

      if ((s instanceof Rocket)) {
        this.parent.createExplosion((int)getX(), (int)getY());
        this.parent.createExplosion((int)s.getX(), (int)s.getY());
        setRemove(true);
        s.setRemove(true);
        if (!this.parent.copter.getRemove()) {
          this.parent.setScore(this.parent.getScore() + 10.0F);
        }
        return true;
      }
    }

    return false;
  }

  public void addxSpeed(double d)
  {
    if (getHorizontalSpeed() < 0.0D) {
      super.setHorizontalSpeed(getHorizontalSpeed() - d);
    }
    else
      super.setHorizontalSpeed(getHorizontalSpeed() + d);
  }

  public void addySpeed(double d)
  {
    if (getVerticalSpeed() < 0.0D)
      setVerticalSpeed(getVerticalSpeed() - d);
    else
      setVerticalSpeed(getVerticalSpeed() + d);
  }

  public void setHorizontalSpeed(double d)
  {
    super.setHorizontalSpeed(d);

    if (getHorizontalSpeed() > 0.0D)
      setLoop(4, 7);
    else
      setLoop(0, 3);
  }
}