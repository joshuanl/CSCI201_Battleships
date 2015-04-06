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
	private boolean msVisible;
	private boolean hit;
	private boolean miss;
	private boolean sink;
	private boolean animateSink;
	private boolean last;
	private Thread t;

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
		hit = false;
		miss = false;
		msVisible = false;
		animateSink = false;
		last = false;
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
					System.out.println("sink: " + sink);
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
				alt++;
				break;
			}
			if(sink){
				System.out.println("sink is true, sending signal to splashanimate()");
				animateSink = true;
				splashAnimate();
			}
			if(animateSink || last){
				System.out.println("animating sink");
				System.out.println("is last? " + last);

				g.drawImage(splash[missCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				missCounter++;
				if(missCounter > splash.length-1){
					missCounter = 0;
					sink = false;
					animateSink = false;
				}
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
					System.out.println("sink: " + sink);
					
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
			if(sink){
				System.out.println("sink is true, sending signal to splashanimate()");
				animateSink = true;
				splashAnimate();
			}
			if(animateSink){
				System.out.println("animating sink");
				System.out.println("is last? " + last);
				g.drawImage(splash[missCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				missCounter++;
				if(missCounter > splash.length-1){
					missCounter = 0;
					sink = false;
					animateSink = false;
				}
			}
			alt--;
			break;
		}//end of switch

 }//end of paintComponent
	public boolean checkLast(){
		return last;
	}
	
	public void isLast(boolean b){
		last = b;
	}
	
	public void setSunk(boolean b){
		sink = b;
	}
	
	public void setMSIcon(ImageIcon msIcon){
		this.msIcon = msIcon;
		setMSVisible(true);
	}
	
	public void setMSVisible(boolean b){
		msVisible = b;
	}//end of setting MS to true
	
	public void isHit(){
		soundThread st = new soundThread("explode.wav", true);
		st.start();
		try {
			st.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hit = true;
		
	}//end of is hit
	
	public void isMiss(){
		soundThread st = new soundThread("splash.wav", true);
		st.start();
		try {
			st.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			miss = true;
	}//end of is miss 
	public void splashAnimate(){
		soundThread st = new soundThread("sinking.wav", false);
		st.start();
	}
	
	class soundThread extends Thread{
		private String sound;
		private boolean cannon;
		public soundThread(String sound, boolean cannon){
			this.sound = sound;
			this.cannon = cannon;
			
		}//end of constructor
		
		public synchronized void run(){
			Thread t = new Thread();
			t.start();
			if(cannon){
				SoundLibrary.playSound("cannon.wav");
			}//end of if play cannon	
			for(int i=0; i < 40; i++){
				try {
					t.sleep(i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			playsoundtype();
		}//end of run
		
		public void playsoundtype(){
			SoundLibrary.playSound(sound);
		}
	}//end of sound thread inner class
	
}//end of class

