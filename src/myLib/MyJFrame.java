package myLib;

import game.GamePanel;
import javax.swing.JFrame;

public class MyJFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  GamePanel p;

  public MyJFrame(String s, GamePanel p)
  {
    super(s);
    this.p = p;
  }

  public void dispose()
  {
    if (this.p.isStarted()) {
      this.p.shutdown();
    }
    this.p.dispose();
    super.dispose();
  }
}