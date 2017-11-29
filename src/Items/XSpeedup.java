package Items;

import game.GamePanel;
import game.Heli;
import game.PowerUp;
import game.Sprite;

import java.awt.image.BufferedImage;

public class XSpeedup extends PowerUp {
	private static final long serialVersionUID = 1L;

	public XSpeedup(BufferedImage[] i, long delay, GamePanel p) {
		super(i, delay, p);
		this.parent.incAktuSpeedUp();
	}

	public void doPowerUp(Sprite s) {
		if (((Heli) s).getXspeed() < this.parent.constants.maxHeliSpeed)
			((Heli) s).setXspeed(((Heli) s).getXspeed() + 10);
		else {
			this.parent.score += 5.0F;
		}

		
		setRemove(true);
	}

	@Override
	public void setRemove(boolean t) {
		this.parent.decAktuSpeedUp();
		super.setRemove(t);
	}
}