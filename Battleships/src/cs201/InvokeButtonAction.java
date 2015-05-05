package cs201;

import java.io.Serializable;

public class InvokeButtonAction implements Serializable{
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private boolean hit;
	
	public InvokeButtonAction(int x, int y, boolean hit){
		this.x = x;
		this.y = y;
		this.hit = hit;
	}//end of constructor

	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isHit(){
		return hit;
	}
}//end of class
