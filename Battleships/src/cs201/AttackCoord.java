package cs201;

import java.io.Serializable;

public class AttackCoord implements Serializable{
	private static final long serialVersionUID = 1L;
	private String coord;
	
	public AttackCoord(String coord){
		this.coord = coord;
	}
	
	public String getCoord(){
		return coord;
	}
}//end of class
