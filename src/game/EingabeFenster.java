package game;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EingabeFenster extends JFrame
  implements ActionListener
{
  GamePanel parent;
  TextField eingabe;
  private static final long serialVersionUID = 1L;

  public EingabeFenster(GamePanel parent)
  {
    this.parent = parent;

    setTitle("CC - Highscore");
    setResizable(false);
    setLayout(new BorderLayout());
    setLocation(parent.getWidth() / 2, parent.getHeight() / 2);
    setDefaultCloseOperation(2);

    Label entername = new Label("Please enter your name:");

    entername.setVisible(true);

    add(entername, "First");

    this.eingabe = new TextField();

    this.eingabe.setVisible(true);
    this.eingabe.setColumns(1);
    this.eingabe.addKeyListener(new KeyListener()
    {
      public void keyTyped(KeyEvent arg0)
      {
      }

      public void keyReleased(KeyEvent e)
      {
        if (e.getKeyCode() == 10)
          EingabeFenster.this.actionPerformed(null);
      }

      public void keyPressed(KeyEvent e)
      {
      }
    });
    add(this.eingabe, "Center");

    JButton finished = new JButton("send");
    finished.setLocation(0, 55);
    finished.setSize(250, 20);
    finished.setVisible(true);
    finished.addActionListener(this);

    add(finished, "Last");

    pack();
    setVisible(true);
    this.eingabe.requestFocus();
  }

  public void actionPerformed(ActionEvent arg0)
  {
    if (!this.eingabe.getText().isEmpty()) {
      setVisible(false);
      this.parent.submitScore(this.eingabe.getText() + "//" + (int)this.parent.score);

      dispose();
    } else {
      JOptionPane.showMessageDialog(null, "Name darf nicht leer sein.", "Fehlermeldung", 0);
    }
  }
}