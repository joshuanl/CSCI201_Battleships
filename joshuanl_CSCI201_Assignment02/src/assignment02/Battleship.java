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
import java.util.Scanner;
import java.util.StringTokenizer;

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
		private JTextField jtf;
		private JLabel inputResultLabel;
		private JLabel scoreCountLabel;
		private int scoreCount;
		protected static int board[][][];
		
		
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
						if(input_coord.charAt(0) == xAxis[i]){
							x_coord = i;
						}
					}//end of for
					if(x_coord == -1){
						jtf.setText("Invalid input");
						return;
					}
					input_coord = input_coord.substring(1);
					if(isInteger(input_coord)){
						y_coord = Integer.parseInt(input_coord) - 1;
						if(y_coord < 1 || y_coord > 10){
							jtf.setText("Invalid input");
							return;
						}//end of if
						if(board[x_coord][y_coord][0] != 0){
							scoreCount--;
							scoreCountLabel.setText("SCORE: "+scoreCount);
							inputResultLabel.setText("HIT!");
						}
						else{
							scoreCount--;
							scoreCountLabel.setText("SCORE: "+scoreCount);
							inputResultLabel.setText("MISS!");
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
			JPanel highscoreGirdPanel = new JPanel();
			highscoreGirdPanel.setLayout(new GridLayout(11,1));
			tempLabel = new JLabel("Highscores                    ");
			highscoreGirdPanel.add(tempLabel);
			for(int i=1; i<=10; i++){
				highscores[(i-1)] = new JLabel(i+".     ");
				highscoreGirdPanel.add(highscores[(i-1)]);
			}
			add(highscoreGirdPanel, BorderLayout.EAST);
			
			
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
					jp.add(jl);
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
			
//===================================MAIN		
		public static void main(String args[]){
			
			Battleship BS = new Battleship();
			//BS.setVisible(true);
			boolean accept_input = false;
			String filename, input;
			String highscoreNames[] = new String[10];
			Scanner keyboard = new Scanner(System.in);
			FileReader fr;
			BufferedReader br = null;
			board = new int[10][10][1]; 
			int ship2Count = 2, ship3Count = 1, ship4Count = 1, ship5Count = 1;
			boolean validBoard = false;
			JLabel tempLabel;
			
			
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
										highscoreNames[i] = temp_str;
										highscores[i].setText((i+1)+".     "+temp_str);
										
									}//end of if theres a name
									else{
										highscoreNames[i] = "";
										highscores[i].setText((i+1)+".     "+highscoreNames[i]);
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
					}//end of reading high scores
					else{
						if(input.length() != 10){
							System.out.println("Length of field should be 10, found: "+input.length());
							System.out.println(input);
							System.out.println("Exiting program");
							System.exit(0);
						}//end of if length of field is wrong
						else{
							for(int i = 0; i < 10; i++){	
								System.out.println("looking at: " + input.charAt(i));
								switch(input.charAt(i)){
									case 'X':
										System.out.println("assigning 0 for X");
										board[field_row][i][0] = 0;
										break;
									case 'A':
										board[field_row][i][0] = 5;
										System.out.println("assigning 5 for A");
										break;
									case 'B':
										board[field_row][i][0] = 4;
										System.out.println("assigning 4 for B");
										break;
									case 'C':
										board[field_row][i][0] = 3;
										System.out.println("assigning 3 for C");
											break;
									case 'D':
										board[field_row][i][0] = 2;
										System.out.println("assigning 2 for D");
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
			boolean visited[][] = new boolean[10][10];
			switch(BFS(board, visited, 0, 0, 0, 0)){
				case -1:
					break;
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
			}//end of checking ships
			if(ship2Count == 0 && ship3Count == 0 && ship4Count == 0 && ship5Count == 0){
				validBoard = true;
				System.out.println("Valid board");
			}//end of check
			else{
				System.out.println("not valid board");
			}
//==============================================END OF BOARD CHECK	
			
			
			BS.setVisible(true);
		}//end of main
//===============================================METHODS		
//===============================================IS INTEGER		
		
//===================================================BFS
		public static int BFS(int board[][][], boolean visited[][], int i, int j, int ship, int length){
			
			if(i > 9 || j > 9){
				return -1;
			}//end of if out of bounds
			if(visited[i][j]){
				//System.out.println("["+i+"]["+j+"]");
				return -1;
			}//end of if not visited
			else{	
				visited[i][j] = true;
			}
			
			switch(board[i][j][0]){
				case 0:
					System.out.println("found water");
					System.out.println("["+i+"]["+j+"]");
					if((j+1) <= 9){
						return BFS(board, visited, i, j+1, 0, 0);
					}
					else{
						j = 0;
						i++;
						return BFS(board, visited, i, j, 0, 0);
					}
				case 2:
					length++;
					System.out.println("Ship2, Length: "+length);
					System.out.println("["+i+"]["+j+"]");
					BFS(board, visited, i+1, j, 2, length);
					BFS(board, visited, i, j+1, 2, length);
					if(ship == 2 && length == 2){
						System.out.println("=======Found Ship 2");
						return 2;
					}
					break;
				case 3:	
					length++;
					System.out.println("Ship3, Length: "+length);
					System.out.println("["+i+"]["+j+"]");
					BFS(board, visited, i+1, j, 3, length);
					BFS(board, visited, i, j+1, 3, length);
					if(ship == 3 && length == 3){
						System.out.println("=======Found Ship 3");
						return 3;
					}	
					break;
				case 4:
					length++;
					System.out.println("Ship4, Length: "+length);
					System.out.println("["+i+"]["+j+"]");
					BFS(board, visited, i+1, j, 4, length);
					BFS(board, visited, i, j+1, 4, length);
					if(ship == 4 && length == 4){
						System.out.println("======Found Ship 4");
						return 4;
					}	
					break;
				case 5:
					length++;
					System.out.println("Ship5, Length: "+length);
					System.out.println("["+i+"]["+j+"]");
					BFS(board, visited, i+1, j, 5, length);
					BFS(board, visited, i, j+1, 5, length);
					if(ship == 5 && length == 5){
						System.out.println("=======Found Ship 5");
						return 5;
					}	
					break;
			}//end of switch		
			
			return -1;
		}//end of BFS
}//end of class
