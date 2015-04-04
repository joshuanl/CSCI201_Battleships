package cs201;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Timer extends Thread{
	boolean stop = true;
	int timeDelay;
	public Timer(int timeDelay){
		this.timeDelay = timeDelay*1000;
	}//end of constructor
	
	public void run(){
		System.out.println("about to delay for "+timeDelay);
		try {
			Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
			System.out.println("IE in Timer()");
		}
	}//end of run
	
	
}//end of class
