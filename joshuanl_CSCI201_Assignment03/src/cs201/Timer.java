package cs201;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Timer extends Thread{
	private Vector<JLabel> pictures;
	int timeDelay;
	JButton jb = new JButton();
	public Timer(Vector<JLabel> pictures, int interval){
		this.pictures = pictures;
		
	}
	
	public Timer(int timeDelay){
		
	}//end of constructor
	
	public void run(){
		
	}//end of run
}//end of class
