package cs201;

import java.awt.Graphics;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MyButton extends JButton{
	private ImageIcon water1;
	private ImageIcon water2;
	private ImageIcon msIcon;
	private ImageIcon splash[];
	private ImageIcon explosion[];
	private int alt;
	private int hitCounter;
	private int missCounter;
	private int waiting;
	private boolean msVisible;
	private boolean hit;
	private boolean miss;
	private boolean soundFinished;
	private Thread t;
//	private SoundLibrary soundMiss = new SoundLibrary("splash.wav");
//	private SoundLibrary soundExplode = new SoundLibrary("explode.wav");
//	private SoundLibrary soundCannon = new SoundLibrary("cannon.wav");
	private static Lock lock = new ReentrantLock();
//	private Condition condition = lock.newCondition();
	//make inner class for sound 
	public MyButton(){
		t = new Thread(){
			public void run(){
				while(true){
					repaint();
					try {
						Thread.sleep(350);
					} catch (InterruptedException e) {
						System.out.println("IE in MyButton.run() "+e.getMessage());
					}
				}//end of while	
			}//end of run
		};
		alt = 1;
		hitCounter = 0;
		missCounter = 0;
		waiting = 0;
		hit = false;
		miss = false;
		msVisible = false;
		soundFinished = false;
		ImageIcon ii;
		water1 = new ImageIcon("animatedWater/water1.png");
		water2 = new ImageIcon("animatedWater/water2.png");
		splash = new ImageIcon[7];
		splash[0] = new ImageIcon("splash/splash1.png");
		splash[1] = new ImageIcon("splash/splash2.png");
		splash[2] = new ImageIcon("splash/splash3.png");
		splash[3] = new ImageIcon("splash/splash4.png");
		splash[4] = new ImageIcon("splash/splash5.png");
		splash[5] = new ImageIcon("splash/splash6.png");
		splash[6] = new ImageIcon("splash/splash7.png");
		explosion = new ImageIcon[5];
		explosion[0] = new ImageIcon("explosion/expl1.png");
		explosion[1] = new ImageIcon("explosion/expl2.png");
		explosion[2] = new ImageIcon("explosion/expl3.png");
		explosion[3] = new ImageIcon("explosion/expl4.png");
		explosion[4] = new ImageIcon("explosion/expl5.png");
		t.start();
	}//end of constructor
	
	
	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g); //call super! super is super important
		switch(alt){
		case 1:
			g.drawImage(water1.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			if(hit){

				miss = false;
				g.drawImage(explosion[hitCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				hitCounter++;
				if(hitCounter > explosion.length-1){
					hitCounter = 0;
					hit = false;
					msVisible = true;
				}
			}
			if(miss){	
				g.drawImage(splash[missCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				missCounter++;
				if(missCounter > splash.length-1){
					missCounter = 0;
					miss = false;
				}
			}
			if(msVisible){
				g.drawImage(msIcon.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			}
			alt++;
			break;
		case 2:
			g.drawImage(water2.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			if(hit){

				miss = false;
				g.drawImage(explosion[hitCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				hitCounter++;
				if(hitCounter > explosion.length-1){
					hitCounter = 0;
					hit = false;
					msVisible = true;
				}
			}
			if(miss){
				g.drawImage(splash[missCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				missCounter++;
				if(missCounter > splash.length-1){
					missCounter = 0;
					miss = false;
				}
			}
			if(msVisible){
				g.drawImage(msIcon.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			}
			alt--;
			break;
		}//end of switch

 }//end of paintComponent
	
	public void setMSIcon(ImageIcon msIcon){
		this.msIcon = msIcon;
		setMSVisible(true);
	}
	
	public void setMSVisible(boolean b){
		msVisible = b;
	}//end of setting MS to true
	
	public void isHit(){
//		lock.lock();
//		try {
////			while(!soundFinished)
////				if(waiting == 0){
//					cannonSound();
//					waiting++;
////				}	
//				System.out.println("awaiting");
//		} finally{
//			System.out.println("in finally block");
//			lock.unlock();	
//			SoundLibrary.playSound("explode.wav");	
		soundThread st = new soundThread("explode.wav", true);
		st.start();
		try {
			st.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			hit = true;
			waiting = 0;
		//}
		
	}//end of is hit
	
	public void isMiss(){
//		lock.lock();
//		try {
////			while(!soundFinished)
////				if(waiting == 0){
//					cannonSound();
////					waiting++;
////				}	
//		} finally{
//			System.out.println("in finally block");
//			lock.unlock();		
//			SoundLibrary.playSound("splash.wav");
		soundThread st = new soundThread("splash.wav", true);
		st.start();
		try {
			st.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			miss = true;
			waiting = 0;
		//}
		
	}//end of is miss 
	
//	public void cannonSound(){
//		lock.lock();
//		try{
//			SoundLibrary.playSound("cannon.wav");	
//			soundFinished = true;
//		} finally{
//			lock.unlock();
//		}
//	}//end of playing cannon
	
	class soundThread extends Thread{
		private String sound;
		private boolean cannon;
		public soundThread(String sound, boolean cannon){
			this.sound = sound;
			this.cannon = cannon;
			
		}//end of constructor
		
		public synchronized void run(){
			System.out.println("playing cannon sound");
			SoundLibrary.playSound("cannon.wav");
			for(int i=0; i < 40; i++){
				try {
					Thread.sleep(i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			playsoundtype();
		}//end of run
		
		public void playsoundtype(){
			System.out.println("playing sound: " + sound);
			SoundLibrary.playSound(sound);
		}
	}//end of sound thread inner class
	
}//end of class

