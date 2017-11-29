package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class PowerUp extends Sprite {
	long timer;
	private static final long serialVersionUID = 1L;
	long remainingTime;

	private boolean colliding;
	

	public PowerUp(BufferedImage[] i, long delay, GamePanel p) {
		super(i, 0.0D, 0.0D, delay, p);
		setVerticalSpeed(0.0D);
		setHorizontalSpeed(0.0D);
		

		remainingTime=parent.constants.PowerUpDuration;
		this.timer = System.currentTimeMillis();
		double helihor = parent.copter.getX();
		double heliver = parent.copter.getY();
		int hor = 0;
		int ver = 0;
		colliding=false;
		
		do {
			hor = (int) (Math.random() * (this.parent.getWidth() - getWidth()));
			ver = (int) (Math.random() * (this.parent.getHeight() - getHeight()));
		} while (distance(hor, ver, helihor, heliver) < 100);  //XXX: besser lösen

		setX(hor);
		setY(ver);
		
		
	}

	public void drawObjects(Graphics g) {
		super.drawObjects(g);

		int t = (int) (remainingTime / 1000L);
		g.setColor(Color.YELLOW);
		if(this.getY()<5){
			g.drawString(Integer.toString(t), (int) this.x, (int) (this.y+getHeight()+10));
		}else{
			g.drawString(Integer.toString(t), (int) this.x, (int) this.y);
		}
		
	}

	public void doLogic(long delta) {
		super.doLogic(delta);
		if(!parent.paused){
			remainingTime-=100000000.0/delta;
		}
		
		if (remainingTime <= 0){
			setRemove(true);
			
		}
		
		if(!colliding){
			if(dx>0){
				dx-=1;
			}
			
		}
		
		colliding=false;
	}

	public boolean collidedWith(Sprite s) {
		if (getRemove()) {
			return false;
		}

		if (checkOpaqueColorcollisions(s)) {
			if (((s instanceof Heli)) && (!(s instanceof Leuchtkugel)) && (!(s instanceof Shield))) {
				doPowerUp(s);
				return true;
			} else if (s instanceof PowerUp && this instanceof PowerUp) {
				colliding=true;
				
				dx+=1;
				
				return true;
			}

		}

		return false;
	}

	protected void computeAnimation() {
	}

	public abstract void doPowerUp(Sprite paramSprite);
}