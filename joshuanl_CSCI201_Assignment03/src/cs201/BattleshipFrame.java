package cs201;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BattleshipFrame extends JFrame{
	
	public BattleshipFrame(BattleshipGrid bsg) {
		super("Battleship");
		
		setSize(1100,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		//JPanel bgPanel = new JPanel();
		ImageIcon spaceImage = new ImageIcon("real_space.jpg");
		add(new JPanel(){public void paintComponent(Graphics g) {
			super.paintComponent(g); //call super! super is super important
			g.drawImage(spaceImage.getImage(), 0, 0, this.getSize().width, this.getSize().height,null);
			}
		});
		add(bsg);
		setVisible(true);
	}//end of constructor
}//end of class