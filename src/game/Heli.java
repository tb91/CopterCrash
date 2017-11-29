package game;

import java.awt.image.BufferedImage;
import java.util.Vector;


public class Heli extends Sprite
{
  private static final long serialVersionUID = 1L;
  public int leuchtkugelanz;
  public Vector<Leuchtkugel> kugeln;
  int xspeed;
  int yspeed;
  private long shieldactive;
  public boolean shieldactivated;
  

  public void activateShield()
  {
    this.shieldactive = this.parent.constants.ShieldDuration*1000;
    
  }

  public long getShieldactive() {
    return this.shieldactive;
  }

  public void deactivateShield() {
    this.shieldactive = 0;
  }

  public Heli(BufferedImage[] i, double x, double y, long delay, GamePanel p)
  {
    super(i, x, y, delay, p);

    this.kugeln = new Vector<Leuchtkugel>();
    this.leuchtkugelanz = this.parent.constants.leuchtkugelCount;

    this.shieldactivated = false;

    this.xspeed = this.parent.constants.heliSpeed;
    this.yspeed = this.parent.constants.heliSpeed;
    this.shieldactive = 0;
    
  }

  public int getXspeed()
  {
    return this.xspeed;
  }

  public void setXspeed(int xspeed)
  {
    this.xspeed = xspeed;
  }

  public int getYspeed()
  {
    return this.yspeed;
  }

  public void setYspeed(int yspeed)
  {
    this.yspeed = yspeed;
  }

  public void throwKugel()
  {
    if (this.leuchtkugelanz > 0)
    {
      Leuchtkugel leucht = new Leuchtkugel(this.parent.lib.leuchtkugel, 70L, this.parent.copter, this.parent);
     
      this.kugeln.add(leucht);
      this.leuchtkugelanz -= 1;
    }
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);
    if ((this instanceof Leuchtkugel))
    {
      return;
    }
    if (getX() < 0.0D) {
      setX(0.0D);
      this.x = 0.0D;
    }

    if (getX() + getWidth() > this.parent.getWidth()) {
      setX(0.0D);
      this.x = (this.parent.getWidth() - getWidth());
    }

    if (getY() < 0.0D) {
      setVerticalSpeed(0.0D);
      setY(0.0D);
    }

    if (getY() + getHeight() > this.parent.getHeight()) {
      setVerticalSpeed(0.0D);
      setY(this.parent.getHeight() - getHeight());
    }
    shieldactive-=100000000.0/delta;
  }

  public boolean collidedWith(Sprite s)
  {
    return false;
  }
}