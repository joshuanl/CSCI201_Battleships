package assignment02;

public class Player {
	private String player_name;
	private int score;
	
	public Player(String player_name){
		this.player_name = player_name;
	}//end of constructor
	
	public Player(String player_name, int score){
		this.player_name = player_name;
		this.score = score;
	}//end of constructor
	
	public Player(){
		player_name = "";
		score = -1;
	}//end of default constructor
	
	public String getName(){
		return player_name;
	}//end of getting the name of the player
	
	public int getScore(){
		return score;
	}//end of getting score method
	
	public void setScore(int score){
		this.score = score;
	}//end of setting score method
	
}//end of class
