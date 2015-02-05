package BattleShips;

//public class Battleship {

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
		int board[][][]; 
		
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
		try{
			input = br.readLine();
			while(input != null){
				System.out.println("grabbed from file: " + input);
				if(input.equalsIgnoreCase("highscore:")){
					System.out.println("read first line");
				}
				input = br.readLine();
			}//end of while not eof
		}catch (IOException ioe){
			System.out.println("Encountered ioe: " + ioe.getMessage());
			System.exit(0);
		}//end of try-catch
		
		
		
	}//end of main
}//end of class 


