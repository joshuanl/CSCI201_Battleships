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
		splash = new ImageIcon[6];
		for(int i=1; i <=6; i++){
			ii = new ImageIcon("splash/spash"+i+".png");
			splash[i-1] = ii;
		}//end of for
		
		explosion = new ImageIcon[5];
		for(int i=1; i <=5; i++){
			ii = new ImageIcon("explosion/explosion"+i+".png");
			explosion[i-1] = ii;
		}//end of for
		t.start();
	}//end of constructor
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //call super! super is super important
		switch(alt){
		case 1:
			g.drawImage(water1.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			if(hit){
				g.drawImage(explosion[hitCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				hitCounter++;
				if(hitCounter > explosion.length){
					hit = false;
				}
			}
			if(miss){
				g.drawImage(splash[missCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				missCounter++;
				if(missCounter > splash.length){
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
				g.drawImage(explosion[hitCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				hitCounter++;
				if(hitCounter > explosion.length){
					hit = false;
				}
			}
			if(miss){
				g.drawImage(splash[missCounter].getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
				missCounter++;
				if(missCounter > splash.length){
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

