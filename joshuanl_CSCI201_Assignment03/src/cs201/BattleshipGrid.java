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
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
	private ArrayList<Battleship> playerShips;
	private int numOf_AC;
	private int numOf_BS;
	private int numOf_C;
	private int numOf_D;
	private static int placementGrid[][];
	
	BattleshipGrid() {
		numOf_AC = 1;
		numOf_BS = 1;
		numOf_C = 1;
		numOf_D = 2;
		
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
		buttonGrid1 = new JButton[10][10];
		buttonGrid2 = new JButton[10][10];
		compShips = new ArrayList<Battleship>();
		playerShips = new ArrayList<Battleship>();
		JButton temp_button;
//====================================================================SET NORTH LABELS
		playerName.setText("Player");
		computerName.setText("Computer");
		JPanel northPanel = new JPanel();
		northPanel.setOpaque(false);
		northPanel.setLayout(new GridLayout(1,2));
		JPanel playerNamePanel = new JPanel();
		playerNamePanel.setOpaque(false);
		playerNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel computerNamePanel = new JPanel();
		computerNamePanel.setOpaque(false);
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
				buttonGrid1[i][j] = temp_button;
				jp1.add(buttonGrid1[i][j]);
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
				buttonGrid2[i][j] = temp_button;
				jp2.add(buttonGrid2[i][j]);
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
		consolePanel.setOpaque(false);
		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.X_AXIS));
		logPanel.setOpaque(false);
		bottomA.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottomA.setOpaque(false);
		jpbottom.setLayout(new BoxLayout(jpbottom, BoxLayout.Y_AXIS));
		jpbottom.setOpaque(false);
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
//================================================================== FILE CHOOSER		
		openFileButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter(".battle","battle");
				fileChooser.setFileFilter(filter);
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
		setOpaque(false);
	}//=================================================================================end of constructor
	
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
			System.out.println("x: "+startX);
			System.out.println("y: "+startY);
			JFrame PSW = new JFrame();
			PSW.setTitle("Place Ship");
			PSW.setLocation(300,300);
			PSW.setSize(275,150);
			//PSW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			PSW.getContentPane().setLayout(new BoxLayout(PSW.getContentPane(), BoxLayout.Y_AXIS));
		
			
			JPanel jp1 = new JPanel();
			jp1.setLayout(new BoxLayout(jp1, BoxLayout.X_AXIS));
			JLabel jl = new JLabel("Select Ship   ");
			jp1.add(jl);
			Vector<String> listofships = new Vector<String>();
			listofships.add("Select Ship");
			for(int i=0; i < numOf_AC; i++){
				listofships.add("Aircraft Carrier");
			}//end of for
			for(int i=0; i < numOf_BS; i++){
				listofships.add("Battleship");
			}//end of for
			for(int i=0; i < numOf_C; i++){
				listofships.add("Cruiser");
			}//end of for
			for(int i=0; i < numOf_D; i++){
				listofships.add("Destroyer");
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
			placeShipButton = new JButton("Place Ship");
			placeShipButton.setEnabled(false);
			placeShipButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					int index = 0;
					if(jcb.getSelectedItem().toString() == "Aircraft Carrier"){
						index = 1;
					}
					else if(jcb.getSelectedItem().toString() == "Battleship"){
						index = 2;
					}
					else if(jcb.getSelectedItem().toString() == "Cruiser"){
						index = 3;
					}
					else if(jcb.getSelectedItem().toString() == "Destroyer"){
						index = 4;
					}
					if(index == 0){
						System.out.println("no ship selected");
						return;
					}
					if(validPlace(startX, startY, index, orientation)){
						Point startPoint = new Point(startX, startY);
						Point endPoint = null;
						char tag = 'F';
						switch(index){
							case 1:
								endPoint = new Point(startX, startY+index);
								tag = 'A';
								numOf_AC--;
								break;
							case 2:
								endPoint = new Point(startX, startY+index);
								tag = 'B';
								numOf_BS--;
								break;
							case 3:
								endPoint = new Point(startX, startY+index);
								tag = 'C';
								numOf_C--;
								break;
							case 4:
								endPoint = new Point(startX, startY+index);
								tag = 'D';
								numOf_D--;
								break;
						}//end of switch
						playerShips.add(new Battleship(tag, startPoint, endPoint));
					}//end of if
					PSW.dispose();
				}//end of button listener
			});
			bottomPanel.add(placeShipButton);
			
			PSW.add(bottomPanel);	
			PSW.setVisible(true);
		}//end of action performed	
	}//end of PlaceShipsAdapter
	
	public boolean validPlace(int startX, int startY, int shipNum, int orientation){
		int grid[][] = placementGrid;
		int length = 6-shipNum;
		System.out.println("shiplength: " + length);
		System.out.println("getting x: "+startX);
		System.out.println("getting y: "+startY);
		switch(orientation){
			case 1:			//face north
				for(int i=0; i < length; i++){
					if(startX > 9){
						return false;
					}
					if(grid[startX][startY] != 0){
						return false;
					}
					grid[startX][startY] = length;
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
					grid[startX][startY] = length;
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
					grid[startX][startY] = length;
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
					grid[startX][startY] = length;
					startY++;
				}//end of for
				startY -= (length);
				break;
		}//end of switch
		
		System.out.println("was able to place ship, returning true, resetting placementGrid");
		System.out.println("getting x: "+startX);
		System.out.println("getting y: "+startY);
		placementGrid = grid;
		//debug
		for(int i=0; i < 10; i++){
			for(int j=0; j < 10; j++){
				System.out.print(grid[i][j]+ " ");
			}
			System.out.println();
		}
		//=================resetting buttons
		switch(orientation){
		case 1:			//face north
			for(int i=0; i < length; i++){
				System.out.println("in face north button test reset loop");
				buttonGrid1[startX][startY].setText(""+length);
				startX++;
			}//end of for
			break;
		case 2:			//face east
			for(int i=0; i < length; i++){
				buttonGrid1[startX][startY].setText(""+length);
				startY--;
			}//end of for
			break;
		case 3:			//face south
			for(int i=0; i < length; i++){
				buttonGrid1[startX][startY].setText(""+length);
				startX--;
			}//end of for
			break;
		case 4:			//face west
			for(int i=0; i < length; i++){
				buttonGrid1[startX][startY].setText(""+length);
				startY++;
			}//end of for
			break;
	}//end of switch
		return true;
	}//end of valid placement method
	
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