package cs201;

import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {
	
	public static Scanner scan = new Scanner(System.in);
	
	private BattleshipGrid bsg;
	
	BattleshipGame() {
		bsg = new BattleshipGrid();
		new BattleshipFrame(bsg);
	}
	
	public static void main(String[] args) {
		
		BattleshipGame bsgm = new BattleshipGame();
		//bsgm.readFile();
		bsgm.playGame();
		System.exit(0);
	}

	private void playGame() {
		boolean isOver = false;
		int turn = 1;
		Random bag = new Random();
		scan.useDelimiter(System.getProperty("line.separator"));
		do{
			//System.out.print("Turn "+turn+" - Please enter a Coordinate:");
		
		}while(!isOver);
		System.out.println("You sank all the ships!");
	}

	
	private void readFile() {
		do {
			System.out.print("Please enter a valid file path:");
		} while(!bsg.loadMap(scan.nextLine()));
	}
}
