package Items;

import game.GamePanel;

import java.awt.image.BufferedImage;

public class RoteRocket extends Rocket
{
  BufferedImage[] gezündetrot;
  BufferedImage[] tausch;
  boolean gezündet;
  private static final long serialVersionUID = 1L;
  
  double actualYSpeed;

  public RoteRocket(BufferedImage[] i, BufferedImage[] gezündetrot, long delay, GamePanel p)
  {
    super(i, delay, p);
    this.gezündetrot = gezündetrot;
    this.tausch = i;
    this.gezündet = false;
    this.actualYSpeed=parent.constants.defaultRocketYSpeed;

    this.parent.setRoteRockets(this.parent.getRoteRockets() + 1);
  }

  public void doLogic(long delta)
  {
    super.doLogic(delta);

    if (this.locked) {
      addxSpeed(4.0D);
      

      if (!this.gezündet) {
        this.gezündet = true;
        this.parent.lib.sound.playSound("rstart");
        if (this.locked) {
          this.tausch = this.getPics();
          this.setPics(this.gezündetrot);
        }

      }

      if (getY() < this.targetp.getY()) {
        setVerticalSpeed(actualYSpeed);  
      }
      if (getY() > this.targetp.getY() + this.parent.copter.getHeight() / 2.0D) {
        setVerticalSpeed(-actualYSpeed);
      }

      if(intersects(targetp))
      if(actualYSpeed>0){
    	  actualYSpeed-=0.25;
      }
      


      if ((getHorizontalSpeed() > 0.0D) && (getX() > this.parent.getWidth())) {
        setRemove(true);
      }

      if ((getHorizontalSpeed() < 0.0D) && (getX() + getWidth() < 0.0D))
        setRemove(true);
    }
  }
  
 
  public void tausche()
  {
    if (!this.gezündet) {
      this.gezündet = true;
      this.parent.lib.sound.playSound("rstart");
      if (this.locked) {
        this.tausch = this.getPics();
        this.setPics(this.gezündetrot);
      }
      else {
        this.gezündet = false;
        this.setPics(this.tausch);
      }
    }
  }
}