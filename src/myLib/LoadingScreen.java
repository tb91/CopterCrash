package myLib;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class LoadingScreen extends JFrame
  implements ActionListener
{
  private static final long serialVersionUID = 1L;
  public JProgressBar JBar;
  public JLabel result;
  public Timer timer;

  public LoadingScreen(String title, int x, int y, int max)
  {
    super(title);
    setLayout(new BorderLayout());
    setLocation(x, y);

    this.JBar = new JProgressBar(0, max);
    this.JBar.setValue(0);
    this.JBar.setStringPainted(true);
    this.JBar.setVisible(true);

    this.result = new JLabel();

    this.timer = new Timer(3000, this);

    add(this.JBar, "First");
    add(this.result, "Last");
    pack();
    setVisible(true);
  }

  public void success()
  {
    this.result.setText("File successfully transmitted");
    this.result.setVisible(true);
    pack();
    this.timer.start();
  }
  public void fail() {
    this.result.setText("Filetransmission not successful");
    this.result.setVisible(true);
    pack();
    this.timer.start();
  }

  public void actionPerformed(ActionEvent arg0)
  {
    setVisible(false);
    dispose();
  }
}