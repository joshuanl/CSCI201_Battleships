package cs201;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BattleshipFrame extends JFrame{
	
	public BattleshipFrame(BattleshipGrid bsg) {
		super("Battleship");
		
		add(bsg);
		
		setSize(1100,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
