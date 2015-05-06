package cs201;

import java.io.Serializable;

public class NewClockTimeObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String time;
	
	public NewClockTimeObject(String time){
		this.time = time;
	}
	
	public synchronized String getNewTime(){
		return time;
	}
	
}
