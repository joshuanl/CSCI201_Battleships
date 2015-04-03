package cs201;

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
		scan.useDelimiter(System.getProperty("line.separator"));
		do{
			//System.out.print("Turn "+turn+" - Please enter a Coordinate:");
		
		}while(!isOver);
		System.out.println("You sank all the ships!");
	}


}
