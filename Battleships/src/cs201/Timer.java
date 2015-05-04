package cs201;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Timer extends Thread{
	boolean stop = true;
	int timeDelay;
	public Timer(int timeDelay){
		if(timeDelay >= 15){
			this.timeDelay = 15;
		}
		this.timeDelay = timeDelay*1000;
	}//end of constructor
	
	public void run(){
		try {
			Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
			System.out.println("IE in Timer()");
		}
	}//end of run
	
	public boolean runTimer(){
		run();
		return true;
	}
	
}//end of class
