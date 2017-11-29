package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.TabExpander;

import myLib.FeedbackBox;

public class Menu extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private GamePanel game;
	private JButton start;
	private JButton training;
	private JButton highscores;
	private JButton help;
	private JButton end;
	private JButton multiplayer;
	private JButton easy;
	private JButton medium;
	private JButton hard;
	private JButton feedback;
	private JButton changelog;
	private JLabel pfeil;
	private JPanel components;
	private JPanel viewHighscore;
	private JPanel viewChangelog;

	private JLabel splash;
	private JLabel splashSW;

	private JTable highscoreTable; //currently used for highscore only
	private JScrollPane scrollpane;
	 
	private DefaultTableModel tablemodel;
	
	private JTextArea workingList;

	private ClientScore client;

	public Menu(int a, int b, ClientScore client) {
		this.client = client;
		this.game = new GamePanel(800, 600, 2, false, client);
		this.start = new JButton("Story");
		this.training = new JButton("Training");
		this.highscores = new JButton("view Highscores");
		this.help = new JButton("Help");
		this.end = new JButton("Quit");
		this.changelog = new JButton("view Changelog");
		changelog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showChangelog();
				
			}
		});

		this.easy = new JButton("Easy");
		this.medium = new JButton("Medium");
		this.hard = new JButton("Hard");

		this.multiplayer = new JButton("Multiplayer");

		this.feedback = new JButton("Feedback");

		this.pfeil = new JLabel(">>");

		this.start.addActionListener(this);
		this.training.addActionListener(this);
		this.highscores.addActionListener(this);
		this.help.addActionListener(this);
		this.end.addActionListener(this);
		
		
		this.easy.addActionListener(this);
		this.medium.addActionListener(this);
		this.hard.addActionListener(this);
		this.multiplayer.addActionListener(this);
		this.feedback.addActionListener(this);

		SpringLayout layout = new SpringLayout();

		components = new JPanel();
		components.setLayout(layout);
		components.add(this.training);
		components.add(this.start);
		components.add(this.end);
		components.add(this.help);
		components.add(this.highscores);
		components.add(this.easy);
		components.add(this.medium);
		components.add(this.hard);
		components.add(this.changelog);

		components.add(this.multiplayer);
		components.add(this.feedback);

		components.add(this.pfeil);
		splash = new JLabel("");
		splash.setIcon(new ImageIcon(this.game.lib.splashscreen));
		
		splashSW=new JLabel("");
		splashSW.setIcon(new ImageIcon(this.game.lib.splashscreenSW));

		components.add(splash);

		add(components);
		setPreferredSize(new Dimension(this.game.lib.splashscreen.getWidth(), this.game.lib.splashscreen.getHeight()));
		setTitle("Copter - Crash" + " @ " + client.version);
		setResizable(false);
		setIconImage(this.game.lib.heli[0]);
		pack();

		layout.putConstraint("North", this.start, getHeight() / 2, "North", components);
		layout.putConstraint("West", this.start, getWidth() / 2 - this.start.getWidth() / 2 - 150, "West", components);

		layout.putConstraint("North", this.training, 5, "South", this.start);
		layout.putConstraint("West", this.training, -(this.training.getWidth() - this.start.getWidth()) / 2, "West", this.start);

		layout.putConstraint("North", this.multiplayer, 5, "South", this.training);
		layout.putConstraint("West", this.multiplayer, -(this.multiplayer.getWidth() - this.training.getWidth()) / 2, "West", this.training);

		layout.putConstraint("North", this.highscores, 5, "South", this.multiplayer);
		layout.putConstraint("West", this.highscores, -(this.highscores.getWidth() - this.multiplayer.getWidth()) / 2, "West", this.multiplayer);
		
		layout.putConstraint("North", this.changelog, 5, "South", this.highscores);
		layout.putConstraint("West", this.changelog, -(this.changelog.getWidth() - this.highscores.getWidth()) / 2, "West", this.highscores);

		layout.putConstraint("North", this.help, 5, "South", this.changelog);
		layout.putConstraint("West", this.help, -(this.help.getWidth() - this.changelog.getWidth()) / 2, "West", this.changelog);

		layout.putConstraint("North", this.feedback, 5, "South", this.help);
		layout.putConstraint("West", this.feedback, -(this.feedback.getWidth() - this.help.getWidth()) / 2, "West", this.help);

		layout.putConstraint("North", this.end, 5, "South", this.feedback);
		layout.putConstraint("West", this.end, -(this.end.getWidth() - this.feedback.getWidth()) / 2, "West", this.feedback);

		layout.putConstraint("North", this.easy, 0, "North", this.start);
		layout.putConstraint("West", this.easy, 15, "East", this.highscores);

		layout.putConstraint("North", this.medium, 0, "North", this.training);
		layout.putConstraint("West", this.medium, -(this.medium.getWidth() - this.easy.getWidth()) / 2, "West", this.easy);

		layout.putConstraint("North", this.hard, 0, "North", this.multiplayer);
		layout.putConstraint("West", this.hard, 0, "West", this.easy);

		layout.putConstraint("North", this.pfeil, 5, "North", this.training);
		layout.putConstraint("West", this.pfeil, 3, "East", this.training);

		setLocation(400, 200);
		setVisible(true);
		setDefaultCloseOperation(3);

		this.easy.setVisible(false);
		this.medium.setVisible(false);
		this.hard.setVisible(false);
		this.pfeil.setVisible(false);

		this.multiplayer.setEnabled(false);
		this.start.setEnabled(false);
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	public static void main(String[] arg) throws Throwable {
		
			
		
//		ClientScore clienttest = new ClientScore();
//				String s = clienttest.testVersion();
//
//		if (s.equals("right Version")) {

			new Menu(400, 400, new ClientScore());
//		} else if (s.equals("wrong Version")) {
//			if (JOptionPane.showConfirmDialog(null, "Client is not up to date.\nWould you like to download the newest one?", "Fehlermeldung", 0) == 0) {
//				clienttest.update();
//			}
//		} else {
//			JOptionPane.showMessageDialog(null, "Server is not online.\nPlease contact admin.", "Fehlermeldung", 2);
//		}


}
	
		
	

	private void switchdifficulties() {
		this.easy.setVisible(!this.easy.isVisible());
		this.medium.setVisible(!this.medium.isVisible());
		this.hard.setVisible(!this.hard.isVisible());
		this.pfeil.setVisible(!this.pfeil.isVisible());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.start)) {
			System.out.println("GO");
		} else if (e.getSource().equals(this.training)) {
			switchdifficulties();
		} else if (e.getSource().equals(this.multiplayer)) {
			System.out.println("Multiplayer");
		} else if (e.getSource().equals(this.highscores)) {
			showHighscores();
			
		} else if (e.getSource().equals(this.help)) {
			this.game.öffneHilfe();
			
		} else if (e.getSource().equals(this.easy)) {
			if (this.game != null) {
				this.game.dispose();
			}
			this.game = new GamePanel(800, 600, 0, true, client);
			switchdifficulties();
		} else if (e.getSource().equals(this.medium)) {
			if (this.game != null) {
				this.game.dispose();
			}
			this.game = new GamePanel(800, 600, 1, true, client);
			switchdifficulties();
		} else if (e.getSource().equals(this.hard)) {
			if (this.game != null) {
				this.game.dispose();
			}

			this.game = new GamePanel(800, 600, 2, true, client);
			switchdifficulties();
		} else if (e.getSource().equals(this.feedback)) {
			new FeedbackBox();
		} else if (e.getSource().equals(this.end)) {

			System.exit(0);
		}
	}
	
	private void showChangelog(){
		components.setVisible(false);
		if (viewChangelog == null) {
			initializeChangelogScreen();
		}

		viewChangelog.add(splashSW);// damit splashscreen nicht verschwindet in einem der JPanels
		viewChangelog.setVisible(true);
		workingList.setText("");
		workingList.setFont(new Font("Calibri", Font.BOLD, 20));
		workingList.setForeground(Color.red);

		workingList.setLineWrap(true);
		workingList.setWrapStyleWord(true);
		
		
	
		try {
			
			
			for (String entry : client.requestChangelog()) {

				
				
				addText("-> " + entry);

				
			}
			if(workingList.getText().isEmpty()){
				addText("No changes made yet");
			}

		} catch (UnknownHostException e) {

			addText(e.getStackTrace().toString() + "\nPlease contact admin.");

			e.printStackTrace();
		} catch (IOException e) {
			addText("Server is not online.\nPlease contact admin.");
			e.printStackTrace();
		}
	}

	private void addText(String string) {
		
		workingList.setText(workingList.getText()+ '\n' + string);
		
		
	}

	private void initializeHighscoreScreen() {
		SpringLayout layout = new SpringLayout();
		viewHighscore = new JPanel(layout);
		viewHighscore.setVisible(true);

		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				viewHighscore.setVisible(false);
				components.add(splash);
				components.setVisible(true);
			}
		});

		
		tablemodel=new DefaultTableModel();
		highscoreTable = new JTable(tablemodel);
		scrollpane=new JScrollPane(highscoreTable);
		
		
		
		highscoreTable.setFont(new Font("Arial", 0, 25));
		highscoreTable.setForeground(Color.red);

	
		//highscoreTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );//TODO: move
		

		tablemodel.addColumn("Place");
		tablemodel.addColumn("Name");
		tablemodel.addColumn("Points");
		
		highscoreTable.setModel(tablemodel);						//TODO: move
		highscoreTable.setRowHeight(25);
		
		
		

		viewHighscore.add(back);

		
		
		layout.putConstraint("North", splash, 0, "North", viewHighscore);
		layout.putConstraint("West", splash, 0, "West", viewHighscore);
		
		
		
		layout.putConstraint("North", scrollpane, (int) (this.getSize().getHeight() / 2 - scrollpane.getPreferredSize().getWidth() / 2)+25, "North", viewHighscore);
		layout.putConstraint("West", scrollpane, (int) (this.getSize().getWidth() / 2 - scrollpane.getPreferredSize().getWidth() / 2)+15, "West", viewHighscore);
		layout.putConstraint("East", scrollpane, -(int) (this.getSize().getWidth() / 2 - scrollpane.getPreferredSize().getWidth() / 2), "East", viewHighscore);
		layout.putConstraint("South", scrollpane, (int) (-5 - back.getPreferredSize().getHeight()), "South", viewHighscore);

		
		
		
		/*layout.putConstraint("North", scrollpane, 0, "North", viewHighscore);
		layout.putConstraint("West", scrollpane,0, "West", viewHighscore);
		layout.putConstraint("East", scrollpane,0, "East", viewHighscore);
		layout.putConstraint("South", scrollpane,-back.getPreferredSize().height-5, "South", viewHighscore);*/
		layout.putConstraint("North", back, 5, "South", scrollpane);
		layout.putConstraint("East", back, 0, "East", scrollpane);

		
		TableColumn col=null;
		highscoreTable.setPreferredSize(new Dimension(400,200));
		for(int i=0; i<3;i++){
			col=highscoreTable.getColumnModel().getColumn(i);
			
			if(i==0){
				col.setPreferredWidth(35);
				
				
			
			}else{
				col.setPreferredWidth((int)(scrollpane.getPreferredSize().getWidth()-35)/2);
			}
		}
		

		viewHighscore.add(scrollpane);
		viewHighscore.add(splash);
		//highscoreTable.setOpaque(false);
		
		
		//System.out.println("HERHEv);
		this.add(viewHighscore);
		pack();

	}
	
	
	private void showHighscores() {
		components.setVisible(false);
		
		if(highscoreTable==null){
			initializeHighscoreScreen();
		}
		
		tablemodel.setRowCount(0);
		
				
		viewHighscore.add(splash);// damit splashscreen nicht verschwindet in einem der JPanels
		viewHighscore.setVisible(true);
		
		highscoreTable.setFont(new Font("Calibri", Font.BOLD, 20));
		
		
		try {
			client.requestScore();
			int place = 1;
			
			
			
			
			
			

			
	
			
			
			for (String entry : client.highscores) {

				String name = this.client.getName(entry);
				String score = this.client.getScore(entry);
				String diff = this.client.getdifficulty(entry);

				String[] result={""+place,name,score + " " + diff};
				tablemodel.addRow(result);
				

				place++;
			}
			if(client.highscores.isEmpty()){
				//TODO: if empty output
			}
			highscoreTable.tableChanged(new TableModelEvent(tablemodel));
		} catch (UnknownHostException e) {

			addText(e.getStackTrace().toString() + "\nPlease contact admin.");

			e.printStackTrace();
		} catch (IOException e) {
			addText("Server is not online.\nPlease contact admin.");
			e.printStackTrace();
		}
		
	} // TODO:ändere Buttons, dass sie anonyme klassen nutzen

	private String buildString(int place, String name, String score, String diff) {
		String result = "";

		
		
		
		result=place + ".\t" + name + "\t\t\t"+ score + " " + diff; 
		
		

		return result;
	}
	
	private void initializeChangelogScreen(){
		SpringLayout layout = new SpringLayout();
		viewChangelog = new JPanel(layout);
		viewChangelog.setVisible(true);
		
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				viewChangelog.setVisible(false);
				components.add(splash);
				components.setVisible(true);
			}
		});

		workingList = new JTextArea();
		workingList.setEditable(false);
	
		JScrollPane scrollPane = new JScrollPane(workingList);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		

		viewChangelog.add(scrollPane);

		viewChangelog.add(back);
	

		layout.putConstraint("North", splash, 0, "North", viewChangelog);
		layout.putConstraint("West", splash, 0, "West", viewChangelog);
		
		layout.putConstraint("North", scrollPane,0, "North", viewChangelog);
		layout.putConstraint("West", scrollPane,0, "West", viewChangelog);
		layout.putConstraint("East", scrollPane,0, "East", viewChangelog);
		layout.putConstraint("South", scrollPane,(int) (-5 - back.getPreferredSize().getHeight()), "South", viewChangelog);

		layout.putConstraint("North", back, 5, "South", scrollPane);
		layout.putConstraint("East", back, 0, "East", scrollPane);


		workingList.setOpaque(false);
		scrollPane.getViewport().add(workingList);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);// XXX: is that okay?
		
		this.add(viewChangelog);
		pack();
	}
	

	
}