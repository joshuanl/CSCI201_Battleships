package cs201;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class BattleshipGrid extends JPanel {
	private JButton buttonGrid1[][];
	//private JButton buttonGrad1[][];
	private JButton buttonGrid2[][];
	private JLabel openedFileLabel;
	private JLabel playerName = new JLabel();
	private JLabel computerName = new JLabel();
	private JButton openFileButton;
	private JButton startButton;
	private JTextArea console;
	private String spacer = "                      ";
	private boolean editMode;
	private ArrayList<Battleship> compShips;
	private ArrayList<Battleship> ship2;
	private static int placementGrid[][];
	
	BattleshipGrid() {
		setLayout(new BorderLayout());
		placementGrid = new int[10][10];
		JPanel jp1 = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jpCase = new JPanel();
		JPanel jpbottom = new JPanel();
		jp1.setLayout(new GridLayout(11,11));
		jp2.setLayout(new GridLayout(11,11));
		buttonGrid1 = new JButton[10][10];
		buttonGrid2 = new JButton[10][10];
		compShips = new ArrayList<Battleship>();
		JButton temp_button;
//====================================================================SET NORTH LABELS
		playerName.setText("Player");
		computerName.setText("Computer");
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1,2));
		JPanel playerNamePanel = new JPanel();
		playerNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel computerNamePanel = new JPanel();
		computerNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		playerNamePanel.add(playerName);
		computerNamePanel.add(computerName);
		
		northPanel.add(playerNamePanel);
		northPanel.add(computerNamePanel);
		add(northPanel, BorderLayout.NORTH);
//==================================================================== CREATE BOARD		
		for(int i = 0; i < 10; i++) {
			jp1.add(new JLabel(Character.toString((char)(0x41+i)), SwingConstants.CENTER));//0x41 is 'A' increment by i to go down the alphabet
			for(int j = 0; j < 10; j++) {
				temp_button = new JButton("?");
				temp_button.setPreferredSize(new Dimension(45, 45));
				PlaceShipsAdapter psa = new PlaceShipsAdapter(i, j);
				temp_button.addActionListener(psa);
				buttonGrid1[j][i] = temp_button;
				jp1.add(buttonGrid1[j][i]);
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
				temp_button = new JButton("?");
				temp_button.setPreferredSize(new Dimension(45, 45));
				buttonGrid2[j][i] = temp_button;
				jp2.add(buttonGrid2[j][i]);
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
		JPanel consolePanel = new JPanel();
		consolePanel.setLayout(new BoxLayout(consolePanel, BoxLayout.X_AXIS));
		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.X_AXIS));
		bottomA.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpbottom.setLayout(new BoxLayout(jpbottom, BoxLayout.Y_AXIS));
		editMode = true;
		console = new JTextArea(3,50);
		JScrollPane scroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		consolePanel.add(scroll);
		consolePanel.add(Box.createGlue());
		bottomA.add(consolePanel);
		console.setText("You are in edit mode.  Click button on Player's Grid to place ships\n");
		JLabel logLabel = new JLabel("   Log");
		logPanel.add(logLabel);
		logPanel.add(Box.createGlue());
		openFileButton = new JButton("Load File");
		bottomA.add(openFileButton);
		openedFileLabel = new JLabel("File:"+spacer);
		bottomA.add(openedFileLabel);
		startButton = new JButton("START");
		bottomA.add(startButton);
		
		openFileButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter(".battle",".txt");
				fileChooser.setFileFilter(filter);
				FileReader fr = null;
				BufferedReader br = null;
				System.out.println("in here");
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	File selectedFile = fileChooser.getSelectedFile();
		        	if(selectedFile.getPath().contains(".battle")){
		        		System.out.println(selectedFile.getPath());
		        		loadMap(selectedFile.getPath());
		        	}
		        }//end of if
			}
		});
		jpbottom.add(logPanel);
		jpbottom.add(bottomA);
		//logPanel.add(console);
		//jpbottom.add(logPanel);
		add(jpbottom, BorderLayout.SOUTH);
		
	}//=================================================================================end of constructor
	
	class PlaceShipsAdapter implements ActionListener{
		int startX; int startY;
		PlaceShipsAdapter(int x, int y){
			startX = x;
			startY = y;
		}
		public void actionPerformed(ActionEvent e) {
			PlaceShipWindow psw = new PlaceShipWindow("AirCraft Carrier", placementGrid, 2, 3, 1, 1, 1, 2);
			char tag = psw.showScreen();
		}//end of action performed	
	}//end of PlaceShipsAdapter
	
	public int getNumSunk() {
		int total = 0;
		for(Battleship b : compShips) {
			if(b.isSunk()) total++;
		}
		return total;
	}
	
	public boolean hitCoord(String coord) {
		if(coord.length()<2 || coord.length()>3) return false;
		char y = coord.charAt(0);
		if(y <'A' || y > 'J') return false;
		
		String x;
		if(coord.charAt(1) <'1' || coord.charAt(1) > '9') return false;
		else x = ""+coord.charAt(1);
		if(coord.length() == 3) {
			if(coord.charAt(2) != '0') return false;
			else x+=coord.charAt(2);
		}
		
		int yPos = (int)(y-'A');
		int xPos = Integer.valueOf(x)-1;
		if(xPos >9) return false;
		
		return hitShips(new Point(xPos,yPos));
	}
	
	private boolean hitShips(Point point) {
		if(!buttonGrid1[point.x][point.y].getText().equals("?")) return false;
		boolean hit = false;
		for(Battleship bs : compShips) {
			if(bs.attackPoint(point)) {
				buttonGrid1[point.x][point.y].setText(bs.getTag()+"");
				hit = true;
				break;
			} else {
				buttonGrid1[point.x][point.y].setText("MISS!");
			}
		}
		if(!hit) System.out.println("You missed!");
		return true;
	}

	public void clearGrid() {
		for(int i = 0; i < 10; i++) {
			for(JButton square : buttonGrid1[i]) {
				square.setText("?");
			}
		}
	}

	public boolean loadMap(String pathName) {
		Scanner inputScan = null;
		try {
			inputScan = new Scanner(new File(pathName));
			
			char[][] inputMatrix = new char[10][10];
			
			for(int i = 0; i < 10; i++) {
				String temp = inputScan.nextLine();
				if(temp.length() != 10) return false;
				else inputMatrix[i] = temp.toCharArray();
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
			
			System.out.println(horizDCount + "   " + vertiDCount);
			
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
		} catch (FileNotFoundException e) {
			System.out.println("File path is invalid!");
			return false;
		} finally {
			if(inputScan != null)
			inputScan.close();
		}
	}//========================================end of loadmap

}//end of BattleshipGrid class

class Battleship {
	private Point startPoint;
	private Point endPoint;
	private char tag;
	private static final String[] NAMES = {"Aircraft Carrier", "Battleship", "Cruiser", "Destroyer"};
	
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
	
	public boolean attackPoint(Point point) {
		boolean gotHit = false;
		for(HitPoint hitPoint: points) {
			if(hitPoint.equals(point)) {
				hitPoint.hit = true;
				gotHit = true;
				System.out.println("You hit a "+NAMES[tag-'A']+"!");
				hp--;
				if(hp == 0) System.out.println("You have sunken a "+NAMES[tag-'A']+"!");
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
	
}