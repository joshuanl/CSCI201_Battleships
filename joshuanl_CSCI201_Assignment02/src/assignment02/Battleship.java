package assignment02;


	import java.io.BufferedReader;
	import java.io.FileReader;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.util.Scanner;
	import java.util.StringTokenizer;

	public class Battleship {
		public static void main(String args[]){
			boolean accept_input = false;
			String filename, input;
			String highscores[] = new String[10];
			Scanner keyboard = new Scanner(System.in);
			FileReader fr;
			BufferedReader br = null;
			int board[][][] = new int[10][10][1]; 
			
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
										highscores[i] = temp_str;
									}//end of if theres a name
									else{
										highscores[i] = "";
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
					
		}//end of main
		public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    // only got here if we didn't return false
		    return true;
		}//end of if integer
}//end of class
