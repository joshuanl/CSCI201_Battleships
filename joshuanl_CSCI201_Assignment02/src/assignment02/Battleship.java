package assignment02;


	import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

	public class Battleship extends JFrame{
		public static final long setSerializationUID = 1; 
		private JLabel spaces[][] = new JLabel[11][11];
		protected static JLabel highscores[] = new JLabel[10];
		private char xAxis[] = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', ' '};
		private static JTextField jtf;
		private JLabel inputResultLabel;
		private JLabel scoreCountLabel;
		private static int scoreCount;
		private int shipAalive = 1, shipBalive = 1,shipCalive = 1, shipDalive = 2;
		protected static int board[][][];
		protected static boolean visited[][];
		protected static Vector<Player> playerList;
		
		
		public Battleship(){
			super("Single Player Battleship");
			setSize(600,500);
			setLocation(200,20);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JPanel GLPanel = getGLPanel();
			add(GLPanel, BorderLayout.CENTER);
//========================================CREATING TEXTFIELD INTERFACE			
			jtf = new JTextField("Input Coordinates here",30);
			inputResultLabel = new JLabel("HIT OR MISS?");
			scoreCount = 100;
			scoreCountLabel = new JLabel("SCORE: "+scoreCount);
			JPanel flowPanel = new JPanel();
			flowPanel.setLayout(new FlowLayout());
			flowPanel.add(jtf);
			flowPanel.add(inputResultLabel);
			flowPanel.add(scoreCountLabel);
			//=============================================ADDING LISTENER TO CHECK HITS
			jtf.addActionListener(new ActionListener(){
				//anonymous inner class
				public void actionPerformed(ActionEvent ae){
					String input_coord = jtf.getText();
					int x_coord = -1, y_coord = -1;
					if(input_coord.length() <= 1){
						jtf.setText("Invalid input");
						return;
					}
					for(int i=0; i< xAxis.length; i++){
						if(input_coord.toUpperCase().charAt(0) == xAxis[i]){
							x_coord = i;
						}
					}//end of for
					if(x_coord == -1){
						jtf.setText("Invalid input");
						return;
					}
					input_coord = input_coord.substring(1);
					if(isInteger(input_coord)){
						y_coord = Integer.parseInt(input_coord);
						if(y_coord < 1 || y_coord > 10){
							jtf.setText("Invalid input");
							return;
						}//end of if
						else if(board[x_coord][y_coord][1] != 0){
							jtf.setText("Already guessed this spot!");
							return;
						}
						if(board[x_coord][y_coord][0] != 0){
							scoreCount--;
							scoreCountLabel.setText("SCORE: "+scoreCount);
							inputResultLabel.setText("HIT!");
							System.out.println("x: " + x_coord + "\ny: " +y_coord);
							spaces[x_coord][y_coord].setText("HIT");
							board[x_coord][y_coord][1] = 1;
							switch(isSunk(board, x_coord, y_coord, board[x_coord][y_coord][0], 1)){
								case 2:
									jtf.setText("You sunk a Destroyer!");
									inputResultLabel.setText("HIT!");
									shipDalive--;
								break;
								case 3:
									jtf.setText("You sunk a Cruiser!");
									inputResultLabel.setText("HIT!");
									shipCalive--;
								break;
								case 4:
									jtf.setText("You sunk a Battleship!");
									inputResultLabel.setText("HIT!");
									shipBalive--;
								break;
								case 5:
									jtf.setText("You sunk an Aircraft Carrier!");
									inputResultLabel.setText("HIT!");
									shipAalive--;
								break;
							}//end of switch
						}
						else{
							scoreCount--;
							scoreCountLabel.setText("SCORE: "+scoreCount);
							inputResultLabel.setText("MISS!");
							System.out.println("x: " + x_coord + "\ny: " +y_coord);
							spaces[x_coord][y_coord].setText("MISS");
							board[x_coord][y_coord][1] = -1;
						}
					}//end of if
					else{
						jtf.setText("Invalid input");
						return;
					}
				}//end of method
			});
			//===============================================END OF LISTENER 
			add(flowPanel, BorderLayout.SOUTH);
//=========================================CREATING HIGHSCORES PANEL			
			JLabel tempLabel;
			JPanel highscoreBoxPanel = new JPanel();
			highscoreBoxPanel.setLayout(new BoxLayout(highscoreBoxPanel, BoxLayout.Y_AXIS));
			tempLabel = new JLabel("Highscores                    ");
			highscoreBoxPanel.add(tempLabel);
			for(int i=1; i<=10; i++){
				highscores[(i-1)] = new JLabel(i+".     ");
				highscoreBoxPanel.add(highscores[(i-1)]);
			}
			add(highscoreBoxPanel, BorderLayout.EAST);
			
			
//===================================END OF CONSTRUCTOR			
		}//end of constructor
//===================================GRIDLAYOUT		
		private JPanel getGLPanel(){
			JPanel jp = new JPanel();
			jp.setLayout(new GridLayout(11,11));
			JLabel jl;
			for(int i=0; i < 10; i++){
				jl = new JLabel(""+xAxis[i]);
				jp.add(jl);
				for(int j=1; j < 11; j++){
					jl = new JLabel("?");
					spaces[i][j] = jl;
					jp.add(spaces[i][j]);
				}//end of inner for loop
			}//end of outer loop
			jl = new JLabel(""+xAxis[10]);
			jp.add(jl);
			for(int i=0; i<10; i++){
				jl = new JLabel(""+(i+1));
				jp.add(jl);
			}
			return jp;
		}//end of getGLPanel

		public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    // only got here if we didn't return false
		    return true;
		}//end of if integer
		public static void printScores(){
			for(int i=0; i<10; i++){
				if(playerList.get(i).getScore() == -1){
					highscores[i].setText((i+1)+".     ");
				}
				else{
					highscores[i].setText((i+1)+".     "+playerList.get(i).getName() + " - " + playerList.get(i).getScore());
				}	
			}//end of for
		}//end of printing highscores
		
		public static void endGame(){
			jtf.setText("Game is finished please enter name");
			jtf.addActionListener(new ActionListener(){
				//anonymous inner class
				public void actionPerformed(ActionEvent ae){
					String player_name = jtf.getText();
					int highscore = scoreCount;
					Player newPlayer = new Player(player_name, highscore);
					playerList.addElement(newPlayer);
				}
			});//end of adding listener	
			System.exit(0);
		}//end of end game
		
//========================ISSUNK		
		public static int isSunk(int board[][][], int i, int j, int ship, int length){
			if((j+1) < 10){
				if(board[i][j+1][0] == board[i][j][0]){
					for(int k=(j+1); k < 10; k++){
						if(board[i][k][0] == board[i][j][0]){
							length++;
						}
					}//end of for to the right
				}
			}	
			if((j-1) > 0){	
				if(board[i][j-1][0] == board[i][j][0]){
					for(int k=(j-1); k > 10; k--){
						if(board[i][k][0] == board[i][j][0]){
							length++;
						}
					}//end of for to the right
				}	
			}//end of horizontal check
			if(length > 1 && length != ship){
				return -1;
			}
			else if(length == ship){
				return ship;
			}
			if((i+1) < 10){
				if(board[i+1][j][0] == board[i][j][0]){
					for(int k=(i+1); k < 10; k++){
						if(board[k][j][0] == board[i][j][0]){
							length++;
						}
					}//end of for to the right
				}
			}	
			if((i-1) < 10){	
				if(board[i-1][j][0] == board[i][j][0]){
					for(int k=(j-1); k > 10; k--){
						if(board[k][j][0] == board[i][j][0]){
							length++;
						}
					}//end of for to the right
				}	
			}//end of horizontal check
			if(length > 1 && length != ship){
				return -1;
			}
			else if(length == ship){
				return ship;
			}
			return 0;
		}//end of BFS	
//===================================MAIN		
		public static void main(String args[]){
			
			Battleship BS = new Battleship();
			//BS.setVisible(true);
			boolean accept_input = false;
			visited  = new boolean[10][10];
			String filename, input;
			Scanner keyboard = new Scanner(System.in);
			FileReader fr;
			BufferedReader br = null;
			board = new int[10][10][2]; 
			playerList = new Vector<Player>();
			int ship2Count = 2, ship3Count = 1, ship4Count = 1, ship5Count = 1;
			boolean validBoard = false;
			JLabel tempLabel;
	//============================ INIT arrays		
			Player temp_player;
			for(int i=0; i<10; i++){
				temp_player = new Player();
				playerList.add(temp_player);
			}
	//=============================READING FILENAME
			while(!accept_input){
				System.out.print("Input filename: ");
				filename = keyboard.nextLine();
				try{
					System.out.println("trying to read file: " + filename);
					fr = new FileReader(filename);
					br = new BufferedReader(fr);		
				}catch (FileNotFoundException fnfe){
					System.out.println("Found FNFE: " + fnfe.getMessage());	
				}//end of try-catch
				accept_input = true;
			}//end of while	
	//===============================READING FILE
			StringTokenizer st;
			String temp_str;
			int temp_int = 0;
			int field_row = 0;
			char temp_char;
			try{
				input = br.readLine();
				while(input != null){
					System.out.println("grabbed from file: " + input);
					if(input.equalsIgnoreCase("highscores:")){
						System.out.println("reading high scores");
						for(int i = 0; i < 10; i++){
							input = br.readLine();
							st = new StringTokenizer(input);
							temp_str = st.nextToken();
							temp_str = temp_str.substring(0, temp_str.length()-1);
							if(isInteger(temp_str)){
								temp_int = Integer.parseInt(temp_str);
								if((temp_int-1) == i){
									if(st.hasMoreTokens()){
										temp_str = st.nextToken();
										temp_player = new Player(temp_str);
										System.out.println("=============grabbed player name: "+temp_player.getName());
										//highscores[i].setText((i+1)+".     "+temp_str);
										if(st.hasMoreTokens()){
											temp_str = st.nextToken();
											if(temp_str.charAt(0) == '-'){
												if (st.hasMoreTokens()) {
													temp_str = st.nextToken();
													if(isInteger(temp_str)){
														temp_player.setScore(Integer.parseInt(temp_str));
														System.out.println("===================players score: "+temp_player.getScore());
														playerList.set(i, temp_player);
														highscores[i].setText((i+1)+".     "+playerList.get(i).getName() + " - "+playerList.get(i).getScore());
													}
													else{
														System.out.println("Invalid input in file, found token but not integer");
														System.exit(0);
													}
												}//end of if has token for score
												else{
													System.out.println("Invalid input in file, found - but no token");
													System.exit(0);
												}
											}//end of equals "-"
											else{
												System.out.println("Invalid input in file, found token but not -");
												System.exit(0);
											}
										}//end of if theres token "-"
										else{
											System.out.println("Invalid input in file, didnt find -");
											System.exit(0);

										}
									}//end of if theres a name
									else{
										highscores[i].setText((i+1)+".     "+playerList.get(i).getName());
									}//end of else theres no name
								}//end of if correct integer
								else{
									System.out.println("Invalid number order");
									System.out.println("Exiting program");
									System.exit(0);
								}//end of else number out of order
							}//end of if integer
							else{
								System.out.println("Invalid input, not integer");
								System.out.println(temp_str);
								System.out.println("Exiting program");
								System.exit(0);
							}//end of else
						}//end of for
//===============================================PRINT HIGH SCORES
						//playerList = orderHighScores(playerList);
						System.out.println("checking playerlist if ordered");
						for(int i=0; i < playerList.size(); i++){
							System.out.println(playerList.get(i).getScore());
						}
						printScores();
					}//end of reading high scores
//===============================================END OF READING HIGH SCORES					
					else{
						if(input.length() != 10){
							System.out.println("Length of field should be 10, found: "+input.length());
							System.out.println(input);
							System.out.println("Exiting program");
							System.exit(0);
						}//end of if length of field is wrong
						else{
							for(int i = 0; i < 10; i++){	
								//System.out.println("looking at: " + input.charAt(i));
								switch(input.charAt(i)){
									case 'X':
										//System.out.println("assigning 0 for X");
										board[field_row][i][0] = 0;
										break;
									case 'A':
										board[field_row][i][0] = 5;
										//System.out.println("assigning 5 for A");
										break;
									case 'B':
										board[field_row][i][0] = 4;
										//System.out.println("assigning 4 for B");
										break;
									case 'C':
										board[field_row][i][0] = 3;
										//System.out.println("assigning 3 for C");
											break;
									case 'D':
										board[field_row][i][0] = 2;
										//System.out.println("assigning 2 for D");
										break;
									default:
										System.out.println("Found invalid input: " + input.charAt(i));
										System.out.println("Exiting program");
										System.exit(0);
										break;	
								}//end of switch case
							}//end of for
							field_row++;
						}//reading line
					}//end of 
					input = br.readLine();
				}//end of while not eof
			}catch (IOException ioe){
				System.out.println("Encountered ioe: " + ioe.getMessage());
				System.exit(0);
			}//end of try-catch
			System.out.println();
			System.out.println();
			System.out.println("Echoing graph");
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 10; j++){
					System.out.print(board[i][j][0]);
				}//end of inner for
				System.out.println();
			}//end of outer for
//====================================CHECKING FOR VALID BOARD			
			for(int i=0; i < 10; i++){
				for(int j=0; j < 10; j++){
					if(board[i][j][0] != 0 && !visited[i][j]){
						//System.out.println("ran into ship: "+board[i][j][0]);
						visited[i][j] = true;
						int result = BFS(board, i, j, board[i][j][0], 1);
						System.out.println("result: "+result);
						switch(result){
							case 2:
								ship2Count--;
							break;
							case 3:
								ship3Count--;
							break;
							case 4:
								ship4Count--;
							break;
							case 5:
								ship5Count--;
							break;
							default:
								System.out.println("default");
							break;	
						}//end of switch	
					}//end of else not water
				}//end of inner for
			}//end of outer for
			if(ship2Count == 0 && ship3Count == 0 && ship4Count == 0 && ship5Count == 0){
				System.out.println("valid board");
			}
			else{
				System.out.println("no valid board");
				System.out.println("ship2: " + ship2Count);
				System.out.println("ship3: " + ship3Count);
				System.out.println("ship4: " + ship4Count);
				System.out.println("ship5: " + ship5Count);
			}
			BS.setVisible(true);
		}//end of main
//===============================================METHODS		
		public static int BFS(int board[][][], int i, int j, int ship, int length){
			//System.out.println("ship: " + ship);
			//System.out.println("length: " + length);
			if(ship == length){
				//System.out.println("=============found valid ship: "+ship);
				return ship;
			}
			if((j+1) < 10){
				//System.out.println("in j bounds");
				if(board[i][j+1][0] == ship && !visited[i][j+1]){
					//System.out.println("found ship to right");
					visited[i][j+1] = true;
					return BFS(board, i, (j+1), ship, ++length);
					//return -1;
				}
			}//end of if in bounds to right	
			if((i+1) < 10){
				//System.out.println("in i bounds");
				if(board[i+1][j][0] == ship && !visited[i+1][j]){
					//System.out.println("found ship to down");
					visited[i+1][j] = true;
					return BFS(board, (i+1), j, ship, ++length);
					//return -1;
				}
			}//end of if in bounds to down
			return 0;
		}//end of BFS
		
		public static Vector<Player> orderHighScores(Vector<Player> playerList){
			Vector<Player> sortedList = new Vector<Player>();
			boolean boolarry[] = new boolean[playerList.size()];
			Player tempPlayer = playerList.get(0);
			int maxIndex = 0;
			System.out.println("checking playerlist from method");
			for(int i=0; i < playerList.size(); i++){
				boolarry[i] = false;
			}
			int n = playerList.size();
			for(int i=0; i< n; i++){
				for(int k=0; k < n;k++){
					if(!boolarry[k]){
						maxIndex = k;
						tempPlayer = playerList.get(k);
						k = n+1;
					}
				}
				for(int j=0; j < n; j++){
					//System.out.println("looking at score: "+playerList.get(j).getScore());
					if(playerList.get(j).getScore() > tempPlayer.getScore() && !boolarry[j]){
						maxIndex = j;
						tempPlayer = new Player(playerList.get(j).getName());
						tempPlayer.setScore(playerList.get(j).getScore());
						boolarry[j] = true;
						System.out.println("new tempPlayer max: "+tempPlayer.getScore());

					}//end of if bigger
				}//end of inner for
				//System.out.println("new tempPlayer max: "+tempPlayer.getScore());
				sortedList.add(tempPlayer);
			}//end of outer for
			return sortedList;
		}//end of ordering high scores list 
		
}//end of class
