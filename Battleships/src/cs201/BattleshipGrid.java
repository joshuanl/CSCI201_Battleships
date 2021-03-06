package cs201;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;


@SuppressWarnings("serial")
public class BattleshipGrid extends JPanel {
	private MyButton playerBG[][];
	private MyButton compBG[][];
	private JLabel playerNameLabel = new JLabel();
	private JLabel playerNameLabelTop = new JLabel();
	private JLabel computerNameLabel = new JLabel();
	private String computerName;
	private String playerName;
	private JLabel clockLabel;
	private JButton startButton;
	private JButton sendButton;
	static private JTextArea console1;
	static private JTextArea console2;
	static private JTextArea console3;
	static private JTextArea console4;
	static private JPanel consolePanel;
	static private JPanel console1CP;
	static private JPanel console2CP;
	static private JPanel console3CP;
	static private JPanel console4CP;
	private JPanel innerPanel;
	static private JCheckBox filterChat = new JCheckBox("Chat");
	static private JCheckBox filterEvents = new JCheckBox("Events");
	private CardLayout cardlayout = new CardLayout();
	private String coordGuess;
	private boolean editMode;
	private boolean readyStatus = false;
	private boolean oppReadyStatus = false;
	private ArrayList<Battleship> compShips;
	private ArrayList<Battleship> playerShips;
	private int numOf_AC;
	private int numOf_BS;
	private int numOf_C;
	private int numOf_D;
	private int roundCount;
	static private int turnTime;
	private int pvpTurnTime;
	private static int placementGrid[][];
	private static boolean playerTurnTaken;
	private static boolean compTurnTaken;
	private static boolean oppTurnTaken;
	private boolean gameRunning;
	private boolean compGuessed[][];
	private boolean playerGuessed[][];
	
	
	private boolean updatedTime = false;
	private boolean wantsRematch = false;
	private boolean gameFinished = false;
	private boolean isHost;
	private boolean isSinglePlayer;
	private boolean mapLoaded = false;
	private boolean oppLoadedMap = false;
	private String ip;
	private int port;
	private int playersConnected = 0;
	private Vector<String> mapContentsVector;
	private Vector<ChatThread> ctVector = new Vector<ChatThread>();
	private ChatThread ct;
	private ServerSocket ss;
	private Socket s;
	private BufferedReader br;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Object obj;	
	private boolean clientConnected = false;
	private BattleshipFrame bsf;
	
	public BattleshipGrid(BattleshipFrame bsf, boolean b, boolean isSinglePlayer, String ip, String port, Vector<String> mapContentsVector, String pName) {
		this.bsf = bsf;
		
		numOf_AC = 1;
		numOf_BS = 1;
		numOf_C = 1;
		numOf_D = 2;
		playerTurnTaken = false;
		compTurnTaken = false;
		oppTurnTaken = false;
		playerGuessed = new boolean[10][10];
		compGuessed = new boolean[10][10];
		turnTime = 15;
		pvpTurnTime = 15;
		roundCount = 1;
		coordGuess = "";
		isHost = b;
		this.isSinglePlayer = isSinglePlayer;
		this.ip = ip;
		this.port = Integer.parseInt(port);
		this.mapContentsVector = mapContentsVector;
		this.playerName = pName;
		
		bsf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				if(!gameFinished){
					rageQuit();
				}	
			}
		});
				
		setLayout(new BorderLayout());
		
		placementGrid = new int[10][10];
		JPanel jp1 = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jpCase = new JPanel();
		JPanel jpbottom = new JPanel();
		jp1.setLayout(new GridLayout(11,11));
		jp1.setOpaque(false);
		jp2.setLayout(new GridLayout(11,11));
		jp2.setOpaque(false);
		jpCase.setOpaque(false);
		jpbottom.setOpaque(false);
		playerBG = new MyButton[10][10];
		compBG = new MyButton[10][10];
		compShips = new ArrayList<Battleship>();
		playerShips = new ArrayList<Battleship>();
		MyButton temp_button;
//====================================================================SET NORTH LABELS
		playerNameLabel.setText(pName);
		playerNameLabelTop.setText(pName);
		computerNameLabel.setText("Computer");
		clockLabel = new JLabel("Time - 0:"+turnTime);
		JPanel northPanel = new JPanel();
		JPanel jp = new JPanel();
		northPanel.setOpaque(false);
		northPanel.setLayout(new GridLayout(1,3));
		jp.add(playerNameLabelTop);
		northPanel.add(jp);
		jp = new JPanel();
		jp.add(clockLabel);
		northPanel.add(jp);
		jp = new JPanel();
		jp.add(computerNameLabel);
		northPanel.add(jp);
		add(northPanel, BorderLayout.NORTH);
//==================================================================== CREATE BOARD		
		for(int i = 0; i < 10; i++) {
			jp1.add(new JLabel(Character.toString((char)(0x41+i)), SwingConstants.CENTER));//0x41 is 'A' increment by i to go down the alphabet
			for(int j = 0; j < 10; j++) {
				temp_button = new MyButton();
				temp_button.setPreferredSize(new Dimension(45, 45));
				PlaceShipsAdapter psa = new PlaceShipsAdapter(i, j);
				temp_button.addActionListener(psa);
				playerBG[i][j] = temp_button;
				jp1.add(playerBG[i][j]);
			}// end of inner for 
		}//end of outer for
		
		jp1.add(new JLabel("")); //fill in the bottom left corner
		
		for(int i = 0; i < 9; i++) {
			jp1.add(new JLabel("  "+Character.toString((char)(0x31+i)), SwingConstants.CENTER));//0x31 is '1' increment by i to increase value
		}
		jp1.add(new JLabel("  "+Character.toString((char)(0x31))+Character.toString((char)(0x30)), SwingConstants.CENTER));// 0x30,0x31 = '1''0'
//======================================================================================== CREATING GRID 2		
		for(int i = 0; i < 10; i++) {
			jp2.add(new JLabel(Character.toString((char)(0x41+i)), SwingConstants.CENTER));//0x41 is 'A' increment by i to go down the alphabet
			for(int j = 0; j < 10; j++) {
				temp_button = new MyButton();
				temp_button.setPreferredSize(new Dimension(45, 45));
				AttackShipListener asl = new AttackShipListener(i,j);
				temp_button.addActionListener(asl);
				
				compBG[i][j] = temp_button;
				jp2.add(compBG[i][j]);
				compBG[i][j].setEnabled(false);
			}// end of inner for 
		}//end of outer for
		
		jp2.add(new JLabel("")); //fill in the bottom left corner
		
		for(int i = 0; i < 9; i++) {
			jp2.add(new JLabel("  "+Character.toString((char)(0x31+i)), SwingConstants.CENTER));//0x31 is '1' increment by i to increase value
		}
		jp2.add(new JLabel("  "+Character.toString((char)(0x31))+Character.toString((char)(0x30)), SwingConstants.CENTER));// 0x30,0x31 = '1''0'
		jpCase.add(jp1);
		jpCase.add(jp2);
		add(jpCase, BorderLayout.CENTER);
//==================================================================================== creating bottom panel
		JPanel bottomA = new JPanel();	
		JPanel logPanel = new JPanel();

		bottomA.setLayout(new BorderLayout());
		jpbottom.setLayout(new BoxLayout(jpbottom, BoxLayout.Y_AXIS));
		startButton = new JButton("START");
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(numOf_AC == 0 && numOf_BS == 0 && numOf_C == 0 && numOf_D == 0){
					startButton.setEnabled(false);
					readyStatus = true;
					if(isHost){
						if(oppReadyStatus && readyStatus){
							Vector<String> mapContents;
							mapContents = getMap(placementGrid);
							sendMessageToClients(true);
							sendMessageToClients(mapContents);
						}//end of if both players are ready
						else{
							sendMessageToClients(true);
						}	
					}//end of if host
					else if(isSinglePlayer){
						playGame();
					}
					else if (!isHost && !isSinglePlayer){
						try {
							if(oppReadyStatus && readyStatus){
								Vector<String> mapContents;
								mapContents = getMap(placementGrid);
								oos.writeObject(true);
								oos.flush();
								oos.writeObject(mapContents);
								oos.flush();
							}//end of if both players are ready
							else{
								oos.writeObject(true);
								oos.flush();
							}	
						} catch (IOException e) {
							System.out.println("IOE while sending ready status");
						}
						
					}//end of else client
				
				}
			}
		});
		
		createConsole();
		bottomA.add(consolePanel, BorderLayout.CENTER);
		console4.setText("You are in edit mode.  Click button on your grid to place your ships\n");
		console4.append("When you are finished placing your ships, load .battle file for the computer's grid\n");
		console4.append("After you've finished placing your ships and loading a .battle file, press Start to begin the game");
		cardlayout.show(innerPanel, "fourth");
		
		JLabel logLabel = new JLabel("Log                                                                                                          "
				+ "                                                                                                                                ");
		jp = new JPanel(); 
		jp.setAlignmentX(LEFT_ALIGNMENT);
		jp.add(logLabel);
		jpbottom.add(jp, Box.LEFT_ALIGNMENT);


		jpbottom.add(logLabel);
		jpbottom.add(bottomA);

		add(jpbottom, BorderLayout.SOUTH);
		
		if(isSinglePlayer){
			loadMap(mapContentsVector);
			bsf.setVisible(true);
		}
		else{
			
			new CreateConnections().start();
		}
		
	}//=================================================================================end of constructor
	
	public void createConsole(){
		sendButton = new JButton("Send");
		innerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		innerPanel.setLayout(cardlayout);
		consolePanel = new JPanel();
		consolePanel.setLayout(new BorderLayout());
		console1CP = new JPanel();
		console2CP = new JPanel();
		console3CP = new JPanel();
		console4CP = new JPanel();
		console1CP.setLayout(new BorderLayout());
		console2CP.setLayout(new BorderLayout());
		console3CP.setLayout(new BorderLayout());
		console4CP.setLayout(new BorderLayout());
		JLabel filterLabel = new JLabel("Filter:");
		JTextField chatTextField = new JTextField(20);
		editMode = true;
		
		console1 = new JTextArea(7,50);
		JScrollPane scroll = new JScrollPane(console1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret)console1.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		console1.setLineWrap(true);
		console1.setWrapStyleWord(true);
		console1.setText("Chat Only\n");
		console1.setEditable(false);
		console1CP.add(scroll);
		
		console2 = new JTextArea(7,50);
		scroll = new JScrollPane(console2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		caret = (DefaultCaret)console2.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		console2.setLineWrap(true);
		console2.setWrapStyleWord(true);
		console2.setText("Events Only\n");
		console2.setEditable(false);
		console2CP.add(scroll);
		
		console3 = new JTextArea(7,50);
		scroll = new JScrollPane(console3, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		caret = (DefaultCaret)console3.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		console3.setLineWrap(true);
		console3.setWrapStyleWord(true);
		console3.setText("Chat and Events\n");
		console3.setEditable(false);
		console3CP.add(scroll);
		
		console4 = new JTextArea(7,50);
		scroll = new JScrollPane(console4, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		caret = (DefaultCaret)console4.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		console4.setLineWrap(true);
		console4.setWrapStyleWord(true);
		console4.setText("");
		console4.setEditable(false);
		console4CP.add(scroll);
		
		innerPanel.add(console1CP, "first");
		innerPanel.add(console2CP, "second");
		innerPanel.add(console3CP, "third");
		innerPanel.add(console4CP, "fourth");
		
		filterChat.addActionListener(new CheckBoxClicked());
		filterEvents.addActionListener(new CheckBoxClicked());
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String temp = chatTextField.getText();
				chatTextField.setText("");
				if(temp.length() != 0){
					if(isHost){
						console1.append("\n"+playerName + ": "+temp);
						console3.append("\n"+playerName + ": "+temp);
						sendMessageToClients(new ChatMessageObject("\n"+playerName + ": "+temp, 1));
					}//end of if host
					else{
						console1.append("\n"+playerName + ": "+temp);
						console3.append("\n"+playerName + ": "+temp);
						try {
							oos.writeObject(new ChatMessageObject("\n"+playerName + ": "+temp, 1));
							oos.flush();
						} catch (IOException e) {
							System.out.println("IOE from sendbutton action listener: " + e.getMessage());
						}
					}//end of else client	
					
				}
			}
		});
		
		
		playerNameLabel.setText("  "+playerNameLabel.getText()+"  ");
		southPanel.add(playerNameLabel);
		southPanel.add(chatTextField);
		southPanel.add(sendButton);
		southPanel.add(startButton);
		eastPanel.add(filterLabel);
		eastPanel.add(filterChat);
		eastPanel.add(filterEvents);
		
		consolePanel.add(innerPanel, BorderLayout.CENTER);
		consolePanel.add(southPanel, BorderLayout.SOUTH);
		consolePanel.add(eastPanel, BorderLayout.EAST);
		
	}//end of creating log/console
	
	class CheckBoxClicked implements ActionListener{
		public CheckBoxClicked(){}
		public void actionPerformed(ActionEvent ae){
			if(filterChat.isSelected() && !filterEvents.isSelected()){
				//CardLayout cl = (CardLayout)console1CPCase.getLayout();
				cardlayout.show(innerPanel, "first");
			}//end of if show first1
			else if(!filterChat.isSelected() && filterEvents.isSelected()){
				//CardLayout cl = (CardLayout)console2CPCase.getLayout();
				cardlayout.show(innerPanel, "second");
			}//end of if show first1
			else if(filterChat.isSelected() && filterEvents.isSelected()){
				//CardLayout cl = (CardLayout)console3CPCase.getLayout();
				cardlayout.show(innerPanel, "third");
			}//end of if show first1
			else if(!filterChat.isSelected() && !filterEvents.isSelected()){
				//CardLayout cl = (CardLayout)console4CPCase.getLayout();
				cardlayout.show(innerPanel, "fourth");
			}//end of if show first1
		}
	}//end of custom action listener	
	//=========================================PLAY GAME
	public boolean playGame(){
//===================== DISABLE/ENDABLE BUTTONS
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				playerBG[i][j].setMSVisible(false);
				compBG[i][j].setMSVisible(false);
			}
		}
		if(isSinglePlayer){
			new TurnThread().start();
			System.out.println("single player turn thread started");
		}
		else if (isHost){
			new PVPTurnThread().start();
		}
		boolean isOver = false;
		editMode = false;
		enableGrid(false, playerBG);
		enableGrid(true, compBG);
		return true;
	}//end of playgame
	
//=============================================== ATTACK SHIP LISTENER	
	class AttackShipListener implements ActionListener{
		private int coordX;
		private int coordY;
		private char c;
		private boolean guessed;
		
		AttackShipListener(int x, int y){
			coordX = x+1;
			coordY = y;
			guessed = false;
		}// end of constructor

		public synchronized void actionPerformed(ActionEvent ae){
			c = getLetter(coordY);
			coordGuess = ""+c+coordX;
			if(playerGuessed[coordX-1][coordY]){
				JOptionPane.showConfirmDialog(BattleshipGrid.this, "Already guessed this spot!", "Battleships", JOptionPane.PLAIN_MESSAGE);
				return;   //if true then player already has guessed that spot
			}
			playerGuessed[coordX-1][coordY] = true;
			if(isHost){
				sendMessageToClients(new AttackCoord(coordGuess));
			}
			else if(!isHost && !isSinglePlayer){
				try {
					oos.writeObject(new AttackCoord(coordGuess));
					oos.flush();
				} catch (IOException e) {
					System.out.println("IOE in attackshiplistener, sending over coord: "+e.getMessage());
				}
			}
			if (hitCoord(coordGuess, 2)){
				//=============================================END OF GAME
				//=============================================END OF GAME
				if(getNumSunk(compShips)==5){
					gameFinished = true;
					if(isHost){
						console2.append("\nYou won!");
						console3.append("\nYou won!");
						sendMessageToClients(new ChatMessageObject("\nYou lost!", 2));
					}
					else if(!isHost && !isSinglePlayer){
						console2.append("\nYou won!");
						console3.append("\nYou won!");
						try {
							oos.writeObject("\nYou lost!");
							oos.flush();
						} catch (IOException e) {
							System.out.println("IOE in writing you lost to server: "+e.getMessage());
						}
					}
					for(int i=0; i < 10; i++){
						for(int j=0; j < 10; j++){
							playerBG[i][j].endGame();
							compBG[i][j].endGame();
						}//end of inner for
					}//end of outer for
					enableGrid(false, compBG);
					enableGrid(false, playerBG);
					gameRunning = false;
					Thread t = new Thread();
					t.start();
					for(int i=0; i < 50; i++){
						try {
							t.sleep(i);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "You Won!", "Game Over", JOptionPane.PLAIN_MESSAGE);
					int n = JOptionPane.showConfirmDialog(BattleshipGrid.this, "Rematch?", "Game Over", JOptionPane.YES_NO_OPTION);
					switch(n){
					case JOptionPane.YES_OPTION:
						if(isHost){
							while(!wantsRematch){}
							bsf.dispose();
							bsf = new BattleshipFrame();
							BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, isSinglePlayer, ip, new String(""+ port) , mapContentsVector, playerName);
							bsf.add(bsg);
							sendMessageToClients(-5);
						}
						else if(!isHost && !isSinglePlayer){
							try {
								oos.writeObject(-5);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							while(!wantsRematch){}
							bsf.dispose();
							bsf = new BattleshipFrame();
							BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, isSinglePlayer, ip, new String(""+ port) , mapContentsVector, playerName);
							bsf.add(bsg);
						}
						break;
					case JOptionPane.NO_OPTION:
						break;
					}//end of switch
				}//end of if end of game
				playerTurnTaken = true;

			}//end of if hit
			enableGrid(false, compBG);
		}//end of actionevent	
	}//end of attackshiplistener class
	
	public char getLetter(int n){
		switch(n){
			case 0:
				return 'A';
			case 1:
				return 'B';
			case 2:
				return 'C';
			case 3:
				return 'D';
			case 4:
				return 'E';
			case 5:
				return 'F';
			case 6:
				return 'G';
			case 7:
				return 'H';
			case 8:
				return 'I';
			case 9:
				return 'J';
		}//end of switch
		return 'Z';
	}//end of getLetter equiv

//============================== PLACE SHIPS LISTENER	
	class PlaceShipsAdapter implements ActionListener{
		private int startX; private int startY;
		private int orientation;
		private ButtonGroup jbg;
		private JButton placeShipButton;

		PlaceShipsAdapter(int x, int y){
			startX = x;
			startY = y;
		}
		public void actionPerformed(ActionEvent e) {
			if(editMode){
				if(placementGrid[startX][startY] != 0){
					String shipType = "";
					JFrame tempFrame = new JFrame();
					switch(placementGrid[startX][startY]){
						case 5:
							shipType = "Hellion";
							break;
						case 4:
							shipType = "Gouf";
							break;
						case 3:
							shipType = "Leo";
							break;
						case 2:
						case 1:
							shipType = "Dom Trooper";
							break;
					}//end of switch
					String msg = "Do you really want to delete these Mobile Suits? \n\""+shipType+"\"";
					int choice = JOptionPane.showConfirmDialog(tempFrame, msg, "Confirmation", JOptionPane.OK_CANCEL_OPTION);
					if(choice == 0){
						removeShip(startX, startY);
						console2.append("\n Removed Mobile Suits: "+shipType);
						console3.append("\n Removed Mobile Suits: "+shipType);
					}
					return;
				}
				JFrame PSW = new JFrame();
				PSW.setTitle("Place Mobile Suits");
				PSW.setLocation(300,300);
				PSW.setSize(275,150);
				PSW.getContentPane().setLayout(new BoxLayout(PSW.getContentPane(), BoxLayout.Y_AXIS));
			
				
				JPanel jp1 = new JPanel();
				jp1.setLayout(new BoxLayout(jp1, BoxLayout.X_AXIS));
				JLabel jl = new JLabel("Select MS   ");
				jp1.add(jl);
				Vector<String> listofships = new Vector<String>();
				listofships.add("Select MS");
				for(int i=0; i < numOf_AC; i++){
					listofships.add("Hellion");
				}//end of for
				for(int i=0; i < numOf_BS; i++){
					listofships.add("Gouf");
				}//end of for
				for(int i=0; i < numOf_C; i++){
					listofships.add("Leo");
				}//end of for
				for(int i=0; i < numOf_D; i++){
					listofships.add("Dom Trooper");
				}//end of for
				JComboBox<String> jcb = new JComboBox<String>(listofships);
				jcb.setPreferredSize(new Dimension(10,10));
				jp1.add(jcb);
				PSW.add(jp1);
				
				jbg = new ButtonGroup();
				JRadioButton northRB = new JRadioButton("Face North");
				northRB.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						placeShipButton.setEnabled(true);
						orientation = 1;
					}
				});
				JRadioButton eastRB = new JRadioButton("Face East");
				eastRB.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						placeShipButton.setEnabled(true);
						orientation = 2;
					}
				});
				JRadioButton southRB = new JRadioButton("Face South");
				southRB.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						placeShipButton.setEnabled(true);
						orientation = 3;
					}
				});
				JRadioButton westRB = new JRadioButton("Face West");
				westRB.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						placeShipButton.setEnabled(true);
						orientation = 4;
					}
				});
				jbg.add(northRB);
				jbg.add(eastRB);
				jbg.add(southRB);
				jbg.add(westRB);
				
				JPanel jp2 = new JPanel();
				jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));
				jp2.add(northRB);
				jp2.add(eastRB);
				PSW.add(jp2);
				
				JPanel jp3 = new JPanel();
				jp3.setLayout(new BoxLayout(jp3, BoxLayout.X_AXIS));
				jp3.add(southRB);
				jp3.add(westRB);
				PSW.add(jp3);
				
				
				JPanel bottomPanel = new JPanel();
				placeShipButton = new JButton("Place MobileSuit");
				placeShipButton.setEnabled(false);
				placeShipButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						int index = 0;
						if(jcb.getSelectedItem().toString() == "Hellion"){
							index = 1;
						}
						else if(jcb.getSelectedItem().toString() == "Gouf"){
							index = 2;
						}
						else if(jcb.getSelectedItem().toString() == "Leo"){
							index = 3;
						}
						else if(jcb.getSelectedItem().toString() == "Dom Trooper"){
							index = 4;
						}
						if(index == 0){
							console2.append("\nNo ship selected");
							console3.append("\nNo ship selected");
							return;
						}
						//=============fix index
						
						if(validPlace(startX, startY, index, orientation)){
							Point startPoint = new Point(startX, startY);
							Point endPoint = null;
							int length = 0;
							ImageIcon msIcon = new ImageIcon();
							char tag = 'F';
							switch(index){
								case 1:
									length = 5;
									tag = 'A';
									msIcon = new ImageIcon("A_resized.png");
									numOf_AC--;
									break;
								case 2:
									length = 4;
									tag = 'B';
									msIcon = new ImageIcon("B_resized.png");
									numOf_BS--;
									break;
								case 3:
									length = 3;
									tag = 'C';
									msIcon = new ImageIcon("C_resized.png");
									numOf_C--;
									break;
								case 4:
									length = 2;
									tag = 'D';
									msIcon = new ImageIcon("D_resized.png");
									numOf_D--;
									break;
							}//end of switch
							switch(orientation){
								case 1:
									endPoint = new Point(startX+(5-index), startY);
									playerShips.add(new Battleship(tag, startPoint, endPoint));
									break;
								case 2:
									endPoint = new Point(startX, startY-(5-index));
									playerShips.add(new Battleship(tag, endPoint, startPoint));
									break;
								case 3:
									endPoint = new Point(startX-(5-index), startY);
									playerShips.add(new Battleship(tag, endPoint, startPoint));
									break;
								case 4:
									endPoint = new Point(startX, startY+(5-index));
									playerShips.add(new Battleship(tag, startPoint, endPoint));
									break;
							}//end of switch
							Point toAdd = new Point(startPoint.x,startPoint.y);
							if(startPoint.y > endPoint.y){
								if(startPoint.x == endPoint.x) {
									for (int i=0; i< length; i++){
										playerBG[toAdd.x][toAdd.y].setMSIcon(msIcon);
										toAdd.y--;
									}
								}//end of if
							}//end of outer if
							else if(startPoint.y < endPoint.y){
								if(startPoint.x == endPoint.x) {
									for (int i=0; i< length; i++){
										playerBG[toAdd.x][toAdd.y].setMSIcon(msIcon);
										toAdd.y++;
									}
								}//end of if
							}//end of outer else if
							if(startPoint.x > endPoint.x){
								if(startPoint.y == endPoint.y) {
									for (int i=0; i< length; i++){
										playerBG[toAdd.x][toAdd.y].setMSIcon(msIcon);
										toAdd.x--;
									}
								}//end of else if
							}//end of outer if
							else if(startPoint.x < endPoint.x){
								if(startPoint.y == endPoint.y) {
									for (int i=0; i< length; i++){
										playerBG[toAdd.x][toAdd.y].setMSIcon(msIcon);
										toAdd.x++;
									}
								}//end of else if
							}//end of outer if
						}//end of if valid
							
						enableGrid(true, playerBG);
						PSW.dispose();
					}//end of button listener
				});
				bottomPanel.add(placeShipButton);
				enableGrid(false, playerBG);
				PSW.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent evt){
						enableGrid(true, playerBG);
						PSW.dispose();
					}
				});
				PSW.add(bottomPanel);	
				PSW.setVisible(true);
				
			}//end of if in edit mode	
		}//end of action performed	
	}//end of PlaceShipsAdapter
//========================================= ENABLE GRID	
	public synchronized void enableGrid(boolean set, MyButton grid[][]){
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				grid[i][j].setEnabled(set);
			}// end of inner for 
		}//end of outer for
		//startButton.setEnabled(set);
		//openFileButton.setEnabled(set);
	}//end of enable grid buttons
//========================================= REMOVE SHIP	
	public void removeShip(int x, int y){
		int target = placementGrid[x][y];
		switch(target){
			case 5:
				numOf_AC++;
				break;
			case 4:
				numOf_BS++;
				break;
			case 3:
				numOf_C++;
				break;
			case 2: 
				numOf_D++;
				break;
			case 1:
				numOf_D++;
				break;
			
		}//end of switch
		for(int i=0; i < 10; i++){
			for(int j=0; j < 10; j++){
				if(placementGrid[i][j] == target){
					placementGrid[i][j] = 0;
					playerBG[i][j].setMSIcon(null);
					playerBG[i][j].setMSVisible(false);
				}//end of if
			}//end of inner for
		}//end of outer for
	}//end of removing ship
	
	public boolean validPlace(int startX, int startY, int shipNum, int orientation){
		int grid[][] = placementGrid;
		int length = 6-shipNum;
		int id = length;
		if(length == 2 && numOf_D == 1){
			id = 1;
		}

		switch(orientation){
			case 1:			//face north
				for(int i=0; i < length; i++){
					if(startX > 9){
						return false;
					}
					if(grid[startX][startY] != 0){
						return false;
					}
					startX++;
				}//end of for
				startX -= (length);
				break;
			case 2:			//face east
				for(int i=0; i < length; i++){
					if(startY < 0){
						return false;
					}
					if(grid[startX][startY] != 0){
						return false;
					}
					startY--;
				}//end of for
				startY += (length);
				break;
			case 3:			//face south
				for(int i=0; i < length; i++){
					if(startX < 0){
						return false;
					}
					if(grid[startX][startY] != 0){
						return false;
					}
					startX--;
				}//end of for
				startX += (length);
				break;
			case 4:			//face west
				for(int i=0; i < length; i++){
					if(startY > 9){
						return false;
					}
					if(grid[startX][startY] != 0){
						return false;
					}
					startY++;
				}//end of for
				startY -= (length);
				break;
		}//end of switch
	

		//=================resetting buttons
		switch(orientation){
		case 1:			//face north
			for(int i=0; i < length; i++){
				grid[startX][startY] = id;
				startX++;
			}//end of for
			break;
		case 2:			//face east
			for(int i=0; i < length; i++){
				grid[startX][startY] = id;
				startY--;
			}//end of for
			break;
		case 3:			//face south
			for(int i=0; i < length; i++){
				grid[startX][startY] = id;
				startX--;
			}//end of for
			break;
		case 4:			//face west
			for(int i=0; i < length; i++){
				grid[startX][startY] = id;
				startY++;
			}//end of for
			break;
	}//end of switch
		placementGrid = grid;
		return true;
	}//end of valid placement method
	
	public int getNumSunk(ArrayList<Battleship> AL) {
		int total = 0;
		for(Battleship b : AL) {
			if(b.isSunk()) total++;
		}
		return total;
	}
	
	public boolean hitCoord(String coord, int grid) {
		if(coord.length()<2 || coord.length()>3) return false;
		char y = coord.charAt(0);
		if(y <'A' || y > 'J'){
			return false;
		}//end of if invalid coord
		
		String x;
		if(coord.charAt(1) <'1' || coord.charAt(1) > '9') return false;
		else x = ""+coord.charAt(1);
		if(coord.length() == 3) {
			if(coord.charAt(2) != '0'){
				return false;
			}
			else x+=coord.charAt(2);
		}
		
		int yPos = (int)(y-'A');
		int xPos = Integer.valueOf(x)-1;
		if(xPos >9) return false;
		
		return hitShips(new Point(xPos,yPos), grid);
	}
	
	private synchronized boolean hitShips(Point point, int grid) {
		//============================PLAYER GUESSING
		if(grid == 2){
			System.out.println("Checking coord in hitships");
			boolean hit = false;
			for(Battleship bs : compShips) {
				if(bs.attackPoint(point)) {
					compBG[point.x][point.y].isHit();
					if(isHost){
						sendMessageToClients(new InvokeButtonAction(point.x, point.y, true));
					}
					else if(!isHost && !isSinglePlayer){
						try {
							oos.writeObject(new InvokeButtonAction(point.x, point.y, true));
							oos.flush();
						} catch (IOException e) {
							System.out.println("IOE when trying to write invoke button actions when hit: "+e.getMessage());
						}
						
					}
					//===========================set icon
					if(bs.getTag() == 'A'){
						compBG[point.x][point.y].setMSIcon(new ImageIcon("A_resized.png"));
						compBG[point.x][point.y].setMSVisible(false);
					}//end of if A
					else if(bs.getTag() == 'B'){
						compBG[point.x][point.y].setMSIcon(new ImageIcon("B_resized.png"));
						compBG[point.x][point.y].setMSVisible(false);
					}//end of if B
					else if(bs.getTag() == 'C'){
						compBG[point.x][point.y].setMSIcon(new ImageIcon("C_resized.png"));
						compBG[point.x][point.y].setMSVisible(false);
					}//end of if C
					else if(bs.getTag() == 'D'){
						compBG[point.x][point.y].setMSIcon(new ImageIcon("D_resized.png"));
						compBG[point.x][point.y].setMSVisible(false);
					}//end of if D
					//=================done setting icon
					hit = true;
					console2.append("\n"+playerName+" hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")");
					console3.append("\n"+playerName+" hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")");
					if(isHost){
						sendMessageToClients(new ChatMessageObject("\n"+playerName+" hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")", 2));
					}
					else if(!isHost && !isSinglePlayer){
						try {
							oos.writeObject(new ChatMessageObject("\n"+playerName+" hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")", 2));
							oos.flush();
						} catch (IOException e) {
							System.out.println("IOE in writing to server that player hit ship: "+e.getMessage());
						}
					}
					if(bs.getHP() == 0){
						console2.append("\n"+playerName+" destroyed Computer's group of "+bs.getName()+"s!");
						console3.append("\n"+playerName+" destroyed Computer's group of "+bs.getName()+"s!");
						if(isHost){
							sendMessageToClients(new ChatMessageObject("\n"+playerName+" destroyed Computer's group of "+bs.getName()+"s!", 2));
						}
						else if(!isHost && !isSinglePlayer){
							try {
								oos.writeObject(new ChatMessageObject("\n"+playerName+" destroyed Computer's group of "+bs.getName()+"s!", 2));
								oos.flush();
							} catch (IOException e) {
								System.out.println("IOE in writing to server that player sunk ship: "+e.getMessage());
							}
						}
						Thread t = new Thread();
						t.start();
						for(int i=0; i < 25; i++){
							try {
								t.sleep(i);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						SendSunkSignal(bs, compBG);
					}//end of if sunk
					//setIcons(point.x, point.y, 2);
					break;
				} 
			}//end of for
			if(!hit){
				compBG[point.x][point.y].isMiss();
				if(isHost){
					sendMessageToClients(new InvokeButtonAction(point.x, point.y, true));
				}
				else if(!isHost && !isSinglePlayer){
					try {
						oos.writeObject(new InvokeButtonAction(point.x, point.y, true));
						oos.flush();
					} catch (IOException e) {
						System.out.println("IOE when trying to write invoke button actions when miss: "+e.getMessage());
					}
				}
				console2.append("\nPlayer hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")");
				console3.append("\nPlayer hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")");
				if(isHost){
					sendMessageToClients(new ChatMessageObject("\n"+playerName+" hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")", 2));
				}
				else if(!isHost && !isSinglePlayer){
					try {
						oos.writeObject(new ChatMessageObject("\n"+playerName+" hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")", 2));
						oos.flush();
					} catch (IOException e) {
						System.out.println("IOE in writing to server that player missed ship: "+e.getMessage());
					}
				}
			}
			compBG[point.x][point.y].setEnabled(false);
		}//end of if grid2
		//===================================COMPUTER GUESSING 
		else if(grid == 1){
			System.out.println("in hitships function, should get here if action sent from over the server");
			boolean hit = false;
			for(Battleship bs : playerShips) {
				if(bs.attackPoint(point)) {
					playerBG[point.x][point.y].isHit();
					//===========================set icon
					if(placementGrid[point.x][point.y] == 5){
						playerBG[point.x][point.y].setMSIcon(new ImageIcon("A_resized.png"));
						playerBG[point.x][point.y].setMSVisible(false);
					}//end of if A
					else if(placementGrid[point.x][point.y] == 4){
						playerBG[point.x][point.y].setMSIcon(new ImageIcon("B_resized.png"));
						playerBG[point.x][point.y].setMSVisible(false);
					}//end of if B
					else if(placementGrid[point.x][point.y] == 3){
						playerBG[point.x][point.y].setMSIcon(new ImageIcon("C_resized.png"));
						playerBG[point.x][point.y].setMSVisible(false);
					}//end of if C
					else if(placementGrid[point.x][point.y] == 2){
						playerBG[point.x][point.y].setMSIcon(new ImageIcon("D_resized.png"));
						playerBG[point.x][point.y].setMSVisible(false);
					}//end of if D
					//=================done setting icon
					hit = true;
					if(isSinglePlayer){
						console2.append("\nComputer hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")");
						console3.append("\nComputer hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")");
					}//end of if single player
					if(isHost){
						sendMessageToClients(new ChatMessageObject("\n"+computerName+" hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")", 2));
					}
					else if(!isHost && !isSinglePlayer){
						try {
							oos.writeObject(new ChatMessageObject("\n"+computerName+" hit "+coordGuess+ " and hit a "+bs.getName()+"! " + "("+clockLabel.getText()+")", 2));
							oos.flush();
						} catch (IOException e) {
							System.out.println("IOE in writing to other side that player hit ship: "+e.getMessage());
						}
					}
					if(bs.getHP() == 0){
						if(isSinglePlayer){
							console2.append("\nComputer destroyed Player's group of "+bs.getName()+"s!");
							console3.append("\nComputer destroyed Player's group of "+bs.getName()+"s!");
						}	
						if(isHost){
							sendMessageToClients(new ChatMessageObject("\n"+computerName+" destroyed Computer's group of "+bs.getName()+"s!", 2));
						}
						else if(!isHost && !isSinglePlayer){
							try {
								oos.writeObject(new ChatMessageObject("\n"+computerName+" destroyed Computer's group of "+bs.getName()+"s!", 2));
								oos.flush();
							} catch (IOException e) {
								System.out.println("IOE in writing to server that player sunk ship: "+e.getMessage());
							}
						}
						Thread t = new Thread();
						t.start();
						for(int i=0; i < 25; i++){
							try {
								t.sleep(i);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						SendSunkSignal(bs, playerBG);
					}
					//setIcons(point.x, point.y, 1);				
					break;
				} 
			}//end of for
			if(!hit){
				playerBG[point.x][point.y].isMiss();
				if(isSinglePlayer){
					console2.append("\nComputer hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")");
					console3.append("\nComputer hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")");
				}	
				if(isHost){
					sendMessageToClients(new ChatMessageObject("\n"+computerName+" hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")", 2));
				}
				else if(!isHost && !isSinglePlayer){
					try {
						oos.writeObject(new ChatMessageObject("\n"+computerName+" hit "+coordGuess+ " and missed! " + "("+clockLabel.getText()+")", 2));
						oos.flush();
					} catch (IOException e) {
						System.out.println("IOE in writing to server that player missed ship: "+e.getMessage());
					}
				}
			}
		}//end of if grid 1
		return true;
	}

	private void SendSunkSignal(Battleship bs, MyButton[][] grid) {
		Point sPoint = bs.getStartPoint();
		Point ePoint = bs.getEndPoint();
		
		int x, y;
		if(sPoint.x > ePoint.x){
			x = sPoint.x;
			while(x >= ePoint.x){
				grid[x][sPoint.y].setSunk(true);
				x--;
			}//end of while
		}//end of if start x is bigger
		else if(sPoint.x < ePoint.x){
			x = sPoint.x;
			while(x <= ePoint.x){
				grid[x][sPoint.y].setSunk(true);
				x++;
			}//end of while
		}//end of if start x is smaller
		if(sPoint.y > ePoint.y){
			y = sPoint.y;
			while(y >= ePoint.x){
				grid[sPoint.x][y].setSunk(true);
				y--;
			}//end of while
		}//end of if start y is bigger
		else if(sPoint.y < ePoint.y){
			y = sPoint.y;
			while(y <= ePoint.y){
				grid[sPoint.x][y].setSunk(true);
				y++;
			}//end of while
		}//end of if start y is bigger
	}//end of sending signal sunk

	public void clearGrid() {
		for(int i = 0; i < 10; i++) {
			for(MyButton square : playerBG[i]) {
				square.setMSIcon(null);
				square.setMSVisible(false);
			}
			for(MyButton square : compBG[i]) {
				square.setMSIcon(null);
				square.setMSVisible(false);
			}
		}//end of for
		//openFileButton.setEnabled(true);
		new BattleshipGrid(bsf, isHost, isSinglePlayer, ip, new String(""+port), mapContentsVector, playerNameLabel.getText());
		
	}//end of cleargrid

	public Vector<String> OrientateMap(Vector<String> mapsContentVector){
		Vector<String> fixedVector = new Vector<String>();
		String str;
		String fixedStr = "";
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				str = mapsContentVector.elementAt(j);
				fixedStr += str.charAt(i);
			}
			fixedVector.add(fixedStr);
			fixedStr = "";
		}
		return fixedVector;
	}
	public boolean loadMap(Vector<String> mapContentsVector) {
		Scanner inputScan = null;
		try {			
			char[][] inputMatrix = new char[10][10];
			mapContentsVector = OrientateMap(mapContentsVector);
			for(int i = 0; i < 10; i++) {
				String temp = mapContentsVector.elementAt(i);
				if(temp.length() != 10) return false;
				else inputMatrix[i] = temp.toCharArray();
			}
			
			for(char[] ca : inputMatrix){
				for(char c : ca){
					//System.out.print(c);
				}
				//System.out.println();
			}
			
			ArrayList<Battleship> dShips = new ArrayList<Battleship>();
			ArrayList<Battleship> dShipsR = new ArrayList<Battleship>();
			
			Map<Character,Integer> charToSize = new HashMap<Character,Integer>();
			charToSize.put('A', 5);
			charToSize.put('B', 4);
			charToSize.put('C', 3);
			charToSize.put('D', 2);
			
			char currentHoriz = '#';
			char currentVerti = '#';
			char currentHorizR = '#';
			char currentVertiR = '#';
			int horizStreak = 0;
			int vertiStreak = 0;
			int horizStreakR = 0;
			int vertiStreakR = 0;
			
			int horizDCount = 0;
			int vertiDCount = 0;
			
			int charCount[] = new int[5];
			for(int i = 0; i < charCount.length; i++) charCount[i] = 0;
			
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					
					if(inputMatrix[i][j]!='X') charCount[inputMatrix[i][j]-'A']++;
					
					if(inputMatrix[i][j] == currentHoriz) {
						horizStreak++;
						if(horizStreak == charToSize.get(currentHoriz)) {
							Battleship bs = new Battleship(currentHoriz,new Point(j-charToSize.get(currentHoriz)+1,i),new Point(j,i));
							if(currentHoriz == 'D') {
								dShips.add(bs);
								horizDCount++;
							}
							else compShips.add(bs);
							currentHoriz = '#';
							horizStreak = 0;
						}
					} 
					else if(inputMatrix[i][j]!='X') {
						currentHoriz = inputMatrix[i][j];
						horizStreak = 1;
					} else {
						currentHoriz = '#';
						horizStreak = 0;
					}
					
					if(inputMatrix[9-i][9-j] == currentHorizR) {
						horizStreakR++;
						if(horizStreakR == 2) {
							dShipsR.add(new Battleship('D',new Point(9-j,9-i),new Point(9-j+2-1,9-i)));
							currentHorizR = '#';
							horizStreakR = 0;
						}
					} 
					else if(inputMatrix[9-i][9-j]=='D') {
						currentHorizR = 'D';
						horizStreakR = 1;
					} else {
						currentHorizR = '#';
						horizStreakR = 0;
					}
					
					if(inputMatrix[j][i] == currentVerti) {
						vertiStreak++;
						if(vertiStreak == charToSize.get(currentVerti)) {
							Battleship bs = new Battleship(currentVerti,new Point(i,j-charToSize.get(currentVerti)+1),new Point(i,j));
							if(currentVerti == 'D') {
								dShips.add(bs);
								vertiDCount++;
							}
							else compShips.add(bs);
							currentVerti = '#';
							vertiStreak = 0;
						}
					}
					else if(inputMatrix[j][i]!='X') {
						currentVerti = inputMatrix[j][i];
						vertiStreak = 1;
					} else {
						currentVerti = '#';
						vertiStreak = 0;
					}
					
					if(inputMatrix[9-j][9-i] == currentVertiR) {
						vertiStreakR++;
						if(vertiStreakR == 2) {
							dShipsR.add(new Battleship('D',new Point(9-i,9-j),new Point(9-i,9-j+2-1)));
							currentVertiR = '#';
							vertiStreakR = 0;
						}
					}
					else if(inputMatrix[9-j][9-i]=='D') {
						currentVertiR = 'D';
						vertiStreakR = 1;
					} else {
						currentVertiR = '#';
						vertiStreakR = 0;
					}
					
				}
			}
			
			if(charCount['A'-'A'] != 5) return false;
			if(charCount['B'-'A'] != 4) return false;
			if(charCount['C'-'A'] != 3) return false;
			if(charCount['D'-'A'] != 4) return false;
						
			if(horizDCount == 2 && vertiDCount == 1) {
				int posToDestroy = -1;
				int pos = 0;
				for(Battleship b : dShips) {
						if(b.getStartPoint().x == b.getEndPoint().x) posToDestroy = pos;
					pos++;
				}
				if(posToDestroy != -1) dShips.remove(posToDestroy);
			}
			
			if(horizDCount == 1 && vertiDCount == 2) {
				int posToDestroy = -1;
				int pos = 0;
				for(Battleship b : dShips) {
						if(b.getStartPoint().y == b.getEndPoint().y) posToDestroy = pos;
					pos++;
				}
				if(posToDestroy != -1) dShips.remove(posToDestroy);
			}
			
			if(horizDCount == 2 && vertiDCount == 2) {
				int posToDestroy = -1;
				int pos = 0;
				for(Battleship b : dShips) {
						if(b.getStartPoint().y == b.getEndPoint().y) posToDestroy = pos;
					pos++;
				}
				if(posToDestroy != -1) dShips.remove(posToDestroy);
				posToDestroy = -1;
				pos = 0;
				for(Battleship b : dShips) {
						if(b.getStartPoint().y == b.getEndPoint().y) posToDestroy = pos;
					pos++;
				}
				if(posToDestroy != -1) dShips.remove(posToDestroy);
			}
			
			if(dShips.size()!=2) return false;
			if(dShips.get(0).getStartPoint().equals(dShips.get(1).getStartPoint())
					|| dShips.get(0).getEndPoint().equals(dShips.get(1).getEndPoint())
					|| dShips.get(0).getStartPoint().equals(dShips.get(1).getEndPoint())
					|| dShips.get(0).getEndPoint().equals(dShips.get(1).getStartPoint())) {
				dShips = dShipsR;
			}
			
			compShips.addAll(dShips);
			
			return true;
		} finally {
			if(inputScan != null)
			inputScan.close();
			mapLoaded = true;
			if(isHost){
				sendMessageToClients(2);
			}
			else if(!isSinglePlayer && !isHost){			
				try {
					oos.writeObject(2);
					oos.flush();
				} catch (IOException e) {
					System.out.println("IOE in sending message that map is loaded");
				}
			}
		}
		
		
		
	}//========================================end of loadmap
	
	public Vector<String> getMap(int[][] map){
		Vector<String> mapContents = new Vector<String>();
		String str = "";
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				switch(placementGrid[i][j]){
					case 0:
						str += "X";
						break;
					case 1:
						str += "D";
						break;
					case 2:
						str += "D";
						break;
					case 3:
						str += "C";
						break;
					case 4:
						str += "B";
						break;	
					case 5:
						str += "A";
						break;
				}//end of switch	
				
				System.out.print(placementGrid[i][j]);
			}//end of inner for
			System.out.println();
			mapContents.add(str);
			str = "";
		}//end of outer for
		return mapContents;
	}
	
	class PVPTurnThread extends Thread{
		public PVPTurnThread(){
			gameRunning = true;
			pvpTurnTime = 15;
		}
		
		public synchronized void run(){ 
			console2.append("\nRound "+roundCount);
			console3.append("\nRound "+roundCount);
			sendMessageToClients(new ChatMessageObject("\nRound "+roundCount, 2));
			
			while((oppTurnTaken != true || playerTurnTaken != true) && gameRunning){	
				
				try {
					sleep(1000);
				} catch (InterruptedException ie) {
					System.out.println("Interrupted exception in TurnThread::run() "+ie.getMessage());
				}
				
				if(pvpTurnTime == 3){
					clockLabel.setText("Time - "+returnTime(pvpTurnTime));
					sendMessageToClients(new NewClockTimeObject(clockLabel.getText()));
					console2.append("\nWarning - "+returnTime(pvpTurnTime) + " remaining in the round!");
					console3.append("\nWarning - "+returnTime(pvpTurnTime) + " remaining in the round!");
					sendMessageToClients(new ChatMessageObject("\nWarning - "+returnTime(pvpTurnTime) + " remaining in the round!", 2));
				}
				else if (pvpTurnTime != 3){
					clockLabel.setText("Time - "+returnTime(pvpTurnTime));
					sendMessageToClients(new NewClockTimeObject(clockLabel.getText()));
				}
				if((oppTurnTaken == true && playerTurnTaken == true) || pvpTurnTime <= 0){
					pvpTurnTime = 15;
					clockLabel.setText("Time - "+returnTime(pvpTurnTime));
					sendMessageToClients(new NewClockTimeObject(clockLabel.getText()));
					sendMessageToClients(-1);
					roundCount++; 
					playerTurnTaken = false;
					enableGrid(true, playerBG);
					enableGrid(true, compBG);
					oppTurnTaken = false;
					console2.append("\nRound "+roundCount);
					console3.append("\nRound "+roundCount);
				}//end of if
				clockLabel.setText("Time - "+returnTime(pvpTurnTime));
				sendMessageToClients(new NewClockTimeObject(clockLabel.getText()));		
				try {
					sleep(1000);
				} catch (InterruptedException ie) {
					System.out.println("Interrupted exception in TurnThread::run() "+ie.getMessage());
				}
				while(!updatedTime){
					//System.out.println("waiting to updated time on client side");
				};
				updatedTime = false;
				pvpTurnTime--;
			}//end of while
			System.out.println("exited while loop, canceled timer");
		}//end of run
		public String returnTime(int t){
			String str = "0:"+t;
			if(t < 10){
				str = "0:0"+t;
			}
			return str;
		}//end of returning time
	}//end of pvp turn thread
	
	class TurnThread extends Thread{
		private boolean gameRunning;
		private boolean issueCompTurn;
		public TurnThread(){
			issueCompTurn = false;
			gameRunning = true;
		}//end of constructor
		public void run(){ 
			console2.append("\nRound "+roundCount);
			console3.append("\nRound "+roundCount);
			while((compTurnTaken != true || playerTurnTaken != true) && gameRunning){	
				if(compTurnTaken != true && issueCompTurn == false){
					issueCompTurn = true;
					if(isSinglePlayer){
						new CompTurn(roundCount).start();
					}//start computer if single player	
				}//end of if 	
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					System.out.println("Interrupted exception in TurnThread::run() "+ie.getMessage());
				}
				turnTime--;
				if(turnTime == 3){
					clockLabel.setText("Time - "+returnTime(turnTime));
					console2.append("\nWarning - "+returnTime(turnTime) + " remaining in the round!");
					console3.append("\nWarning - "+returnTime(turnTime) + " remaining in the round!");
				}
				else{
					clockLabel.setText("Time - "+returnTime(turnTime));
				}
				if((compTurnTaken == true && playerTurnTaken == true) || turnTime <= 0){
					turnTime = 15;
					clockLabel.setText("Time - "+returnTime(turnTime));
					roundCount++; 
					playerTurnTaken = false;
					enableGrid(true, playerBG);
					compTurnTaken = false;
					issueCompTurn = false;
					enableGrid(true, compBG);
					console2.append("\nRound "+roundCount);
					console3.append("\nRound "+roundCount);
				}//end of if
			}//end of while
		}//end of run
		
		public String returnTime(int t){
			String str = "0:"+t;
			if(t < 10){
				str = "0:0"+t;
			}
			return str;
		}//end of returning time
		
		public void EndGame(){
			gameRunning = false;
		}
	}//end of inner class
	class CompTurn extends Thread{
		//private SoundLibrary soundCannon = new SoundLibrary("cannon.wav");
		public CompTurn(int roundCount){
		}
		
		public synchronized void run(){
			if(compTurnTaken != true){
				char c;
				Random bag = new Random();
				int x = bag.nextInt(10);
				int delay = bag.nextInt(17)+1;
				int iteration = 0;
				boolean exit = false;
				if(delay > 14){
					compTurnTaken = true;
					Thread t = new Thread();
					for(int i=0; i < 100; i++){
						try {
							t.sleep(i);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return;
				}
				(new Timer(delay)).run();
				x++;
				int y = bag.nextInt(10);
				c = getLetter(y);
				String temp = ""+c+x;
				if(hitCoord(temp, 1)){
					if(getNumSunk(playerShips)==5){
						for(int i=0; i < 10; i++){
							for(int j=0; j < 10; j++){
								playerBG[i][j].endGame();
								compBG[i][j].endGame();
							}//end of inner for
						}//end of outer for
						gameRunning = false;
						Thread t = new Thread();
						t.start();
						for(int i=0; i < 50; i++){
							try {
								t.sleep(i);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						console2.append("\nYou Lost!");
						console3.append("\nYou Lost!");
						JOptionPane.showMessageDialog(null, "You Lost!", "Game Over", JOptionPane.PLAIN_MESSAGE);
						int n = JOptionPane.showConfirmDialog(BattleshipGrid.this, "Rematch?", "Game Over", JOptionPane.YES_NO_OPTION);
						switch(n){
						case JOptionPane.YES_OPTION:
							if(isHost){
								while(!wantsRematch){}
								bsf.dispose();
								bsf = new BattleshipFrame();
								BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, isSinglePlayer, ip, new String(""+ port) , mapContentsVector, playerName);
								bsf.add(bsg);
								sendMessageToClients(-5);
							}
							else if(!isHost && !isSinglePlayer){
								try {
									oos.writeObject(-5);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								while(!wantsRematch){}
								bsf.dispose();
								bsf = new BattleshipFrame();
								BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, isSinglePlayer, ip, new String(""+ port) , mapContentsVector, playerName);
								bsf.add(bsg);
							}
							break;
						case JOptionPane.NO_OPTION:
							break;
						}//end of switch
						gameFinished = true;
						enableGrid(false, compBG);
						enableGrid(false, playerBG);
					}//end of if end of game
				}//end of if hit
				compTurnTaken = true;
			}//end of if
		}//end of run
	}//end of comp turn class
	class CreateConnections extends Thread{
		private int timeLeft = 30;
		final private String s1 = "Waiting for another player...";
		final private String s2 = "s until timeout.";
		WaitingForPlayer wfp;
		
		public CreateConnections(){
			if(!isHost){
				try {
					s = new Socket(ip, port);
					System.out.println("was able to connect to server");
					bsf.setVisible(true);
					bsf.GameStarted();
					oos = new ObjectOutputStream(s.getOutputStream());
					ois = new ObjectInputStream(s.getInputStream());

					new ReadObject().start();
				} catch (IOException ioe) {
					System.out.println("IOE client: " + ioe.getMessage());
					//JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(BattleshipGrid.this);
					ShutDownServer();
					bsf.dispose();
					this.interrupt();					
					JOptionPane.showMessageDialog(null, "Could not connect to server!", "Battleship", JOptionPane.PLAIN_MESSAGE);
					new ConnectWindow();					
				} catch (IllegalArgumentException iae){
					System.out.println("illegal argument exception: "+iae.getMessage());
					//JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(BattleshipGrid.this);
					ShutDownServer();
					bsf.dispose();
					this.interrupt();					
					JOptionPane.showMessageDialog(null, "Could not connect to server!", "Battleship", JOptionPane.PLAIN_MESSAGE);
					new ConnectWindow();	
				} finally{

					
				}
				//new CheckConnectionThread().start();
			}//end of if not host
		}//end of constructor
		public void run(){
			if(isHost){
				try {
					System.out.println("Starting Chat Server");
					ss = new ServerSocket(port);
					while (true) {
						System.out.println("Waiting for client to connect...");
						if(playersConnected == 0){
							wfp = new WaitingForPlayer(bsf, BattleshipGrid.this);
						}	
						Socket s = ss.accept();
						bsf.GameStarted();
						playersConnected++;
						clientConnected = true;
						wfp.close(true);
						System.out.println("Client " + s.getInetAddress() + ":" + s.getPort() + " connected");
						ChatThread ct = new ChatThread(s);
						ctVector.add(ct);
						ct.start();
						ss.close();
						//new CheckConnectionThread().start();
						return;
						/*
						 * to add spectators, remove break and if statement to allow more  connections but handle case if spectator
						*/
					}//end of while
				} catch (IOException ioe) {
					System.out.println("IOE: in createconnections run" + ioe.getMessage());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

				}//end of finally			
			}//end of if host
		}
	}//end of create connections
	
	public void ShutDownServer(){
		
		try {
			if(ois != null){
				ois.close();
			}
			if(oos != null){
				oos.close();
			}
			if(s != null){
				s.close();
			}
			if(ss != null){
				ss.close();
			}
		} catch (IOException e) {
			System.out.println("IOE in shutdown method");
		}
		
	}//end of shutdown server
	
	public void removeChatThread(ChatThread ct) {
		ctVector.remove(ct);
	}
	public synchronized void sendMessageToClients(Object obj) {
		if(isHost){
			for (ChatThread ct1 : ctVector) {
				System.out.println("sending msg: " + obj.getClass());
				ct1.sendMessage(obj);
			}
		}	
	}
	class ChatThread extends Thread {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private Socket s;
		public ChatThread(Socket s) {
			this.s = s;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(playerName);
				oos.flush();
			} catch (IOException ioe) {
				System.out.println("IOE in ChatThread constructor: " + ioe.getMessage());
			}
		}//end of chat thread

		public void sendMessage(Object obj) {
			try {
				oos.writeObject(obj);
				oos.flush();
			} catch (IOException e) {
				Socket s = new Socket();
				InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
				try {
					s.connect(address, 5000);
					rageQuit();
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, "Lost Connection!", "Connection Error", JOptionPane.WARNING_MESSAGE);
				}
				System.out.println("IOE from ChatThread.sendMessage() from server to client: "+e.getMessage());
			}
		}//end of send message
		
		//server reading from client
		public synchronized void run(){
			try {
				obj = ois.readObject();
				while(obj != null){
					if(obj instanceof String){
						computerNameLabel.setText((String)obj);
						computerName = (String) obj;
					}
					else if (obj instanceof ChatMessageObject){
						switch(((ChatMessageObject)obj).getLogNum()){
							case 1:
								console1.append(((ChatMessageObject)obj).getMessage());
								console3.append(((ChatMessageObject)obj).getMessage());
								break;
							case 2:
								console2.append(((ChatMessageObject)obj).getMessage());
								console3.append(((ChatMessageObject)obj).getMessage());
								break;
							case 3:
								console3.append(((ChatMessageObject)obj).getMessage());
								break;
						}//end of switch
					}
					else if(obj instanceof Boolean){
						System.out.println("server got boolean from client");
						oppReadyStatus = (Boolean)obj;
						if(oppReadyStatus && readyStatus){
							Vector<String> mapContents;
							mapContents = getMap(placementGrid);
							sendMessage(mapContents);
						}//end of if both players are ready
					}//end of if read in a boolean
					else if(obj instanceof Vector<?>){
						System.out.println("got map contents, loading into map");
						loadMap((Vector<String>)obj);
						mapLoaded = true;
						sendMessage(3);
					}//end of else if read in a vector
					else if(obj instanceof AttackCoord){
						System.out.println("got instance of attack coord from other player, about to check in hitcoord");
						if(hitCoord(((AttackCoord)obj).getCoord(), 1)){
							if(getNumSunk(playerShips)==5){
								for(int i=0; i < 10; i++){
									for(int j=0; j < 10; j++){
										playerBG[i][j].endGame();
										compBG[i][j].endGame();
									}//end of inner for
								}//end of outer for
								gameRunning = false;
								Thread t = new Thread();
								t.start();
								for(int i=0; i < 50; i++){
									try {
										t.sleep(i);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								console2.append("\nYou Lost!");
								console3.append("\nYou Lost!");
								JOptionPane.showMessageDialog(null, "You Lost!", "Game Over", JOptionPane.PLAIN_MESSAGE);
								enableGrid(false, compBG);
								enableGrid(false, playerBG);
							}//end of if end of game
						}//end of if hit
						oppTurnTaken = true;
						enableGrid(true, compBG);
					}//end of if read in an attack coord from other player
					else if(obj instanceof InvokeButtonAction){
						InvokeButtonAction temp = (InvokeButtonAction)obj;
					}//ed of if read in an invoke action button from other player
					else if(obj instanceof Integer){
						switch((Integer)obj){
							case -10:
								JOptionPane.showMessageDialog(null, "The other player has quit the game!", "Connection Error", JOptionPane.ERROR_MESSAGE);
								bsf.dispose();
								new ConnectWindow();
								break;
							case 5:
								wantsRematch = true;
								break;
							case -1:
								updatedTime = true;
								break;
							case 1:
								if(!gameRunning){
									playGame();
								}	
								break;
							case 2:
								
								break;
							case 3:
								oppLoadedMap = true;
								if(oppLoadedMap && mapLoaded){
									playGame();
									gameRunning = true;
									sendMessage(1);								
								}
								break;
							case 80:
								sendMessage(80);
								break;	
						}//end of switch
					}//end of else if read in an integer
					obj = ois.readObject();
				}//end of while	
			}catch(IOException ioe){
				System.out.println("IOE in chatthread.run: " + ioe.getMessage());
			} catch(ClassNotFoundException cnfe){
				System.out.println("CNFE in chatthread.run: " + cnfe.getMessage());	
			}//end of finally block
		}//end of run
	}//end of chathread

	//client reading from server
	public class ReadObject extends Thread{
		ReadObject(){
		}
		
		public synchronized void run(){
			try {
				obj = ois.readObject();
				while(obj != null){					
					if(obj instanceof String){
						computerNameLabel.setText((String)obj);
						computerName = (String) obj;
						oos.writeObject(playerName);
						oos.flush();
					}
					else if(obj instanceof Boolean){
						System.out.println("server got boolean from client");
						oppReadyStatus = (Boolean)obj;
						if(oppReadyStatus && readyStatus){
							Vector<String> mapContents;
							mapContents = getMap(placementGrid);
							oos.writeObject(mapContents);
							oos.flush();
						}//end of if both players are ready
					}//end of if read in a boolean
					else if (obj instanceof NewClockTimeObject){
						clockLabel.setText(((NewClockTimeObject)obj).getNewTime());
						System.out.println("got time: " +clockLabel.getText());
						oos.writeObject(-1);
						oos.flush();
					}
					else if (obj instanceof ChatMessageObject){
						switch(((ChatMessageObject)obj).getLogNum()){
							case 1:
								console1.append(((ChatMessageObject)obj).getMessage());
								console3.append(((ChatMessageObject)obj).getMessage());
								break;
							case 2:
								console2.append(((ChatMessageObject)obj).getMessage());
								console3.append(((ChatMessageObject)obj).getMessage());
								break;
							case 3:
								console3.append(((ChatMessageObject)obj).getMessage());
								break;
						}//end of switch
					}//end of else read in chat message object
					else if(obj instanceof Vector<?>){
						System.out.println("got map contents, loading into map");
						loadMap((Vector<String>)obj);
						mapLoaded = true;
						oos.writeObject(3);
						oos.flush();
					}//end of else if read in a vector
					else if(obj instanceof AttackCoord){
						System.out.println("got instance of attack coord from server, about to check in hitcoord");
						if(hitCoord(((AttackCoord)obj).getCoord(), 1)){
							if(getNumSunk(playerShips)==5){
								for(int i=0; i < 10; i++){
									for(int j=0; j < 10; j++){
										playerBG[i][j].endGame();
										compBG[i][j].endGame();
									}//end of inner for
								}//end of outer for
								gameRunning = false;
								Thread t = new Thread();
								t.start();
								for(int i=0; i < 50; i++){
									try {
										t.sleep(i);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								console2.append("\nYou Lost!");
								console3.append("\nYou Lost!");
								JOptionPane.showMessageDialog(null, "You Lost!", "Game Over", JOptionPane.PLAIN_MESSAGE);
								enableGrid(false, compBG);
								enableGrid(false, playerBG);
							}//end of if end of game
						}//end of if hit
						oppTurnTaken = true;
					}//end of if read in attack coord from server
					else if(obj instanceof Integer){
						switch((Integer)obj){
							case -10:
								JOptionPane.showMessageDialog(null, "The other player has quit the game!", "Connection Error", JOptionPane.ERROR_MESSAGE);
								bsf.dispose();
								new ConnectWindow();
								break;
							case -5:
								wantsRematch = true;
								break;
							case -1:
								roundCount++;
								enableGrid(true, playerBG);
								enableGrid(true, compBG);
								break;
							case 1:
								if(!gameRunning){
									playGame();
								}	
								break;
							case 2:
								break;
							case 3:
								oppLoadedMap = true;
								if(oppLoadedMap && mapLoaded){
									playGame();
									gameRunning = true;
									oos.writeObject(1);
									oos.flush();							
								}
								break;
							case 80:
								try {
									oos.writeObject(80);
									oos.flush();
								} catch (IOException e) {
									System.out.println("IOE in readobject.run: " + e.getMessage());
								}
								break;
						}//end of switch
					}//end of else if read in an integer
					obj = ois.readObject();
				}//end of while	
			}catch(IOException ioe){
				System.out.println("IOE in readobject.run: " + ioe.getMessage());
			} catch(ClassNotFoundException cnfe){
				System.out.println("CNFE in IOE in  readobject.run: " + cnfe.getMessage());
			}
		}//end of run
	}//end of inner class read object
	
	
	class CheckConnectionThread extends Thread{
		private Socket s = new Socket();
		private InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
		public CheckConnectionThread(){

		}//end of constructor
		
		public void run(){
			JOptionPane.showMessageDialog(null, "Lost Connection!", "Connection Error", JOptionPane.WARNING_MESSAGE);
			while(true){
				if(isHost){
					sendMessageToClients(80);
				}
				else{
					try {
						oos.writeObject(80);
						oos.flush();
					} catch (IOException e) {
						Socket s = new Socket();
						InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
						try {
							s.connect(address, 5000);
							rageQuit();
						} catch (IOException ioe) {
							JOptionPane.showMessageDialog(null, "Lost Connection!", "Connection Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}//end of while	
		}//end of run
	}//end of checking internet connections
	
	public void rageQuit(){
		if(isHost){
			sendMessageToClients(-10);
		}
		else if(!isHost && !isSinglePlayer){
			try {
				oos.writeObject(-10);
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}//end of rage quit
}//end of BattleshipGrid class

class Battleship {
	private Point startPoint;
	private Point endPoint;
	private char tag;
	private static final String[] NAMES = {"Hellion", "Gouf", "Leo", "Dom Trooper"};
	
	@SuppressWarnings("serial")
	class HitPoint extends Point{boolean hit; HitPoint(int x, int y){super(x,y);hit=false;}}
	private ArrayList<HitPoint> points;
	private int hp;
	public Battleship(char tag, Point startPoint, Point endPoint) {
		this.tag = tag;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		points = new ArrayList<HitPoint>();
		Point toAdd = new Point(startPoint.x,startPoint.y);
		if(startPoint.x == endPoint.x) {
			while(toAdd.y!=endPoint.y) {
				points.add(new HitPoint(toAdd.x,toAdd.y));
				toAdd.y++;
			}
			points.add(new HitPoint(toAdd.x,toAdd.y));
		}
		else if(startPoint.y == endPoint.y) {
			while(toAdd.x!=endPoint.x) {
				points.add(new HitPoint(toAdd.x,toAdd.y));
				toAdd.x++;
			}
			points.add(new HitPoint(toAdd.x,toAdd.y));
		}
		hp = points.size();
	}
	
	public String getName() {
		return NAMES[tag-'A'];
	}
	public int getHP(){
		return hp;
	}

	public boolean attackPoint(Point point) {
		boolean gotHit = false;
		for(HitPoint hitPoint: points) {
			if(hitPoint.equals(point)) {
				hitPoint.hit = true;
				gotHit = true;
				hp--;
			}
		}
		return gotHit;
	}
	public Point getStartPoint() {
		return startPoint;
	}
	public Point getEndPoint() {
		return endPoint;
	}
	public char getTag() {
		return tag;
	}
	public boolean isSunk() {
		if(hp==0) return true;
		else return false;
	}
		
}//end of Battleship inner class



