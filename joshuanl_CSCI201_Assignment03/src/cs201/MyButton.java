package cs201;

import java.awt.Graphics;

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
	private Thread t;
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
		hit = false;
		miss = false;
		msVisible = false;
		ImageIcon ii;
		water1 = new ImageIcon("animatedWater/water1.png");
		water2 = new ImageIcon("animatedWater/water2.png");
		splash = new ImageIcon[7];
		splash[0] = new ImageIcon("splash/spash1.png");
		splash[1] = new ImageIcon("splash/spash2.png");
		splash[2] = new ImageIcon("splash/spash3.png");
		splash[3] = new ImageIcon("splash/spash4.png");
		splash[4] = new ImageIcon("splash/spash5.png");
		splash[5] = new ImageIcon("splash/spash6.png");
		splash[6] = new ImageIcon("splash/spash7.png");
		explosion = new ImageIcon[5];
		explosion[0] = new ImageIcon("explosion/expl1.png");
		explosion[1] = new ImageIcon("explosion/expl2.png");
		explosion[2] = new ImageIcon("explosion/expl3.png");
		explosion[3] = new ImageIcon("explosion/expl4.png");
		explosion[4] = new ImageIcon("explosion/expl5.png");
		t.start();
	}//end of constructor
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //call super! super is super important
		switch(alt){
		case 1:
			g.drawImage(water1.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			if(hit){
				System.out.println("displaying hit image");
				g.drawImage(explosion[hitCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				hitCounter++;
				if(hitCounter > explosion.length-1){
					hitCounter = 0;
					hit = false;
				}
			}
			if(miss){
				System.out.println("displaying miss image");
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
				System.out.println("displaying hit image");
				g.drawImage(explosion[hitCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				hitCounter++;
				if(hitCounter > explosion.length-1){
					hitCounter = 0;
					hit = false;
				}
			}
			if(miss){
				System.out.println("displaying miss image");
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
		hit = true;
	}//end of is hit
	
	public void isMiss(){
		miss = true;
	}//end of is miss 
	
}//end of class

