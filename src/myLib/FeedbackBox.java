package myLib;

import game.ClientScore;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class FeedbackBox extends JFrame
  implements ActionListener
{
  private static final long serialVersionUID = 1L;
  JButton send = new JButton("Send");
  TextField name = new TextField();
  JLabel labelName = new JLabel("Your name:");
  JLabel labelMessage = new JLabel("Your message:");
  JTextArea message = new JTextArea();
  JScrollPane scroll;

  public FeedbackBox()
  {
    setTitle("CC - Feedback");

    SpringLayout layout = new SpringLayout();

    this.scroll = new JScrollPane(this.message);
    this.scroll.setVerticalScrollBarPolicy(22);

    JPanel components = new JPanel();
    components.setLayout(layout);
    components.add(this.labelName);
    components.add(this.name);
    components.add(this.labelMessage);
    components.add(this.scroll);
    components.add(this.send);

    this.send.addActionListener(this);

    this.setLocation(250,300);
    this.name.setPreferredSize(new Dimension(376, 23));
    this.send.setPreferredSize(new Dimension(440, 30));
    this.scroll.setPreferredSize(new Dimension(440, 170));

    this.message.setLineWrap(true);
    this.message.setWrapStyleWord(true);

    layout.putConstraint("North", this.labelName, 5, "North", components);
    layout.putConstraint("West", this.labelName, 0, "West", components);

    layout.putConstraint("North", this.name, 0, "North", this.labelName);
    layout.putConstraint("West", this.name, 0, "East", this.labelName);

    layout.putConstraint("North", this.labelMessage, 5, "South", this.name);
    layout.putConstraint("West", this.labelMessage, 0, "West", this.labelName);

    layout.putConstraint("North", this.scroll, 0, "South", this.labelMessage);
    layout.putConstraint("West", this.scroll, 0, "West", this.labelMessage);

    layout.putConstraint("North", this.send, 5, "South", this.scroll);
    layout.putConstraint("West", this.send, 0, "West", this.labelMessage);

    layout.putConstraint("South", components, 0, "South", this.send);
    layout.putConstraint("East", components, 0, "East", this.scroll);

    add(components);

    pack();

    setVisible(true);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource().equals(this.send)) {
      if (this.name.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "You must enter a name.", "Fehlermeldung", 0);
        return;
      }
      if (this.message.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "You must send something.", "Fehlermeldung", 0);
        return;
      }
      try {
        sendFeedback();
      }
      catch (Throwable e1) {
        e1.printStackTrace();
      }
    }
  }

  private void sendFeedback() throws Throwable
  {
    ClientScore clienttest = new ClientScore();
    String s = "";
    s = clienttest.sendFeedback(this.name.getText(), this.message.getText());
    if (s.equals("true"))
    {
      JOptionPane.showMessageDialog(null, "Feedback successfully submitted", "submitted", 1);
      dispose();
    }
    else if (s.equals("unknown")) {
      JOptionPane.showMessageDialog(null, "Hostname not found.\nPlease contact admin.", "Fehlermeldung", 2);
    } else if (s.equals("io")) {
      JOptionPane.showMessageDialog(null, "IO-Exception. Try again please.\nIf this Error occurs more than once, contact admin please.", "Fehlermeldung", 2);
    } else {
      JOptionPane.showMessageDialog(null, "Server is not online.\nPlease contact admin.", "Fehlermeldung", 2);
    }
  }
}