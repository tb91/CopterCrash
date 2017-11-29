package Items;

import game.GamePanel;
import game.Heli;
import game.PowerUp;
import game.Sprite;

import java.awt.image.BufferedImage;

public class XSpeeddown extends PowerUp {
	private static final long serialVersionUID = 1L;

	public XSpeeddown(BufferedImage[] i, long delay, GamePanel p) {
		super(i, delay, p);
		this.parent.incAktuSpeedDowns();
	}

	public void doPowerUp(Sprite s) {
		if (((Heli) s).getXspeed() > this.parent.constants.minHeliSpeed) {
			((Heli) s).setXspeed(((Heli) s).getXspeed() - 10);
			this.parent.score += 10.0F;
		}

		setRemove(true);
	}

	@Override
	public void setRemove(boolean t) {
		this.parent.decAktuSpeedDowns();
		super.setRemove(t);
	}
}