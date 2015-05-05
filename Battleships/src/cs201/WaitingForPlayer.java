package cs201;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class WaitingForPlayer extends Thread{
	private int timeLeft;
	private JLabel label = new JLabel();
	final private String s1 = "Waiting for another player...";
	final private String s2 = "s until timeout.";
	private JDialog myDialog;
	private BattleshipGrid bsg;
	private JFrame jf = new JFrame();
	
	public WaitingForPlayer(BattleshipGrid bsg){			
		jf.setTitle("Battleship Menu");
		jf.setSize(300,300);
		jf.setLocation(100,50);
		jf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(bsg);
				bsg.ShutDownServer();
				topFrame.dispose();
				new ConnectWindow();
				jf.dispose();
			}
		});
		this.bsg = bsg;
		jf.setLayout(new BorderLayout());
		bsg.setVisible(false);
		timeLeft = 30;
		label.setText(s1 + " " + timeLeft + s2);
		jf.add(label, BorderLayout.CENTER);
		jf.setVisible(true);
		myDialog = new JDialog(jf);
		myDialog.setModal(true);
		myDialog.setAlwaysOnTop(true);
		start();
	}//end of constructor

	public void run() {
		while(!this.isInterrupted()){
			if(timeLeft == 1){
				this.interrupt();
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("in waiting for player.run, trying to sleep");
			}
			timeLeft--;
			label.setText(s1 + " " + timeLeft + s2);
		}//end of while	
		close(false);
	}//end of run
	public void close(boolean connected){
		if(connected){
			System.out.println("force closing wfp");
			this.interrupt();
			bsg.setVisible(true);
			jf.dispose();
			myDialog.dispose();	
			myDialog.setEnabled(false);
			myDialog.setVisible(false);
			
		}
		else{
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(bsg);
			bsg.ShutDownServer();
			topFrame.dispose();
			myDialog.dispose();	
		}
	}
}//end of class