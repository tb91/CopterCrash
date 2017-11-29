package game;


import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Items.GelbeRocket;
import Items.RoteRocket;
import Items.SchwarzeRocket;


public abstract class Sprite extends Rectangle2D.Double
  implements Movable, Drawable
{
  private static final long serialVersionUID = 1L;
  long delay;
  long animation = 0L;
  protected GamePanel parent;
  private BufferedImage[] pics;
  protected int currentpic = 0;
  protected double dx;
  protected double dy;
  int loop_from;
  int loop_to;
  private boolean remove = false;

  public Sprite(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
    this.setPics(i);
    this.x = x;
    this.y = y;
    this.delay = delay;
    this.width = this.getPics()[0].getWidth();
    this.height = this.getPics()[0].getHeight();
    this.parent = p;
    this.loop_from = 0;
    this.loop_to = (this.getPics().length - 1);
  }

  public boolean getRemove() {
    return this.remove;
  }

  public void setRemove(boolean t) {
    if (t) {
      if ((this instanceof RoteRocket))
        this.parent.setRoteRockets(this.parent.getRoteRockets() - 1);
      else if ((this instanceof GelbeRocket))
        this.parent.setGelbeRockets(this.parent.getGelbeRockets() - 1);
      else if ((this instanceof SchwarzeRocket)) {
        this.parent
				.setSchwarzeRockets(this.parent.getSchwarzeRockets() - 1);
      }
    }

    this.remove = t;
  }

  public boolean checkOpaqueColorcollisions(Sprite s)
  {
    Rectangle2D.Double cut = (Rectangle2D.Double)createIntersection(s);

    if ((cut.width < 1.0D) || (cut.height < 1.0D)) {
      return false;
    }

    Rectangle2D.Double sub_me = getSubRec(this, cut);
    Rectangle2D.Double sub_him = getSubRec(s, cut);

    BufferedImage img_me = this.getPics()[this.currentpic].getSubimage((int)sub_me.x, (int)sub_me.y, 
      (int)sub_me.width, (int)sub_me.height);

    BufferedImage img_him = s.getPics()[s.currentpic].getSubimage((int)sub_him.x, (int)sub_him.y, 
      (int)sub_him.width, (int)sub_him.height);

    for (int i = 0; i < img_me.getWidth(); i++) {
      for (int n = 0; n < img_him.getHeight(); n++)
      {
        int rgb1 = img_me.getRGB(i, n);
        int rgb2 = img_him.getRGB(i, n);

        if ((isOpaque(rgb1)) && (isOpaque(rgb2))) {
          return true;
        }
      }

    }

    return false;
  }
  
  /**
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @return distance between (x1,y1) and (x2,y2)
 */
public double distance(double x1, double y1, double x2, double y2){
	  return Math.sqrt((Math.abs(Math.pow((x1-x2), 2)-Math.pow(y1-y2, 2))));
  }

/**
* @param x1
* @param y1
* @param x2
* @param y2
* @return distance between this object and a point (x2,y2)
*/
public double distance(double x2, double y2){
	  return Math.sqrt((Math.abs(Math.pow((getX()-x2), 2)-Math.pow(getY()-y2, 2))));
}

  private boolean isOpaque(int rgb)
  {
    int alpha = rgb >> 24 & 0xFF;

    if (alpha == 0) {
      return false;
    }
    return true;
    
  }

  protected Rectangle2D.Double getSubRec(Rectangle2D.Double source, Rectangle2D.Double part)
  {
    Rectangle2D.Double sub = new Rectangle2D.Double();

    if (source.x > part.x)
      sub.x = 0.0D;
    else {
      part.x -= source.x;

    }

    if (source.y > part.y)
      sub.y = 0.0D;
    else {
      part.y -= source.y;
    }

    sub.width = part.width;
    sub.height = part.height;

    return sub;
  }

  public double getHorizontalSpeed()
  {
    return this.dx;
  }

  public void setHorizontalSpeed(double dx)
  {
    this.dx = dx;
  }

  public double getVerticalSpeed()
  {
    return this.dy;
  }

  public void setVerticalSpeed(double dy)
  {
    this.dy = dy;
  }

  public abstract boolean collidedWith(Sprite paramSprite);

  public void setX(double d) {
    this.x = d;
  }
  public void setY(double d) {
    this.y = d;
  }

  public void drawObjects(Graphics g)
  {
    g.drawImage(this.getPics()[this.currentpic], (int)this.x, (int)this.y, null);
  }

  public void doLogic(long delta)
  {
    if (this.remove) {
      return;
    }
    this.animation += delta / 1000000L;
    if (this.animation > this.delay) {
      this.animation = 0L;
      computeAnimation();
    }
  }

  protected void computeAnimation()
  {
    this.currentpic += 1;

    if (this.currentpic > this.loop_to)
      this.currentpic = this.loop_from;
  }

  public void setLoop(int from, int to)
  {
    this.loop_from = from;
    this.loop_to = to;
    this.currentpic = from;
  }

  public void move(long delta)
  {
    if (this.dx != 0.0D) {
      this.x += this.dx * (delta / 1000000000.0D);
    }

    if (this.dy != 0.0D)
      this.y += this.dy * (delta / 1000000000.0D);
  }

public BufferedImage[] getPics() {
	return pics;
}

public void setPics(BufferedImage[] pics) {
	this.pics = pics;
}
}