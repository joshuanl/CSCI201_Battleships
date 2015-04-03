package cs201;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class BattleshipFrame extends JFrame{
	
	public BattleshipFrame(BattleshipGrid bsg) {
		super("Battleship");
		
		setSize(1100,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		JMenu jm = new JMenu("Info");
		JMenuItem howtoitem = new JMenuItem("How To");
		howtoitem.setMnemonic('H');
		howtoitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		howtoitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				JFrame jf = new JFrame();
				jf.setTitle("Instructions");
				jf.setLocation(150,50);
				jf.setSize(350, 350);
				jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JPanel jp = new JPanel();
				jp.setLayout(new BorderLayout());
				JTextArea jta = new JTextArea(3,50);
				JScrollPane scroll = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jta.setLineWrap(true);
				jta.setWrapStyleWord(true);
				jta.setText("Click on grid under Player to add ships.  Click on the ships to remove them.\n"
						+ "Load .battle file for Computer.  After all ships and file is added, press start to begin.\n"
						+ "Click on the grid under Computer to guess");
				jp.add(scroll, BorderLayout.CENTER);
				jf.add(jp);
				jf.setVisible(true);
			}
		});
		JMenuItem aboutitem = new JMenuItem("about");
		aboutitem.setMnemonic('A');
		aboutitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aboutitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
					JFrame jf = new JFrame();
					jf.setTitle("Instructions");
					jf.setLocation(150,50);
					jf.setSize(400, 400);
					JPanel jp = new JPanel();
					jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
					jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					JLabel jl = new JLabel("Made by Joshua Lum");
					JLabel jl2 = new JLabel(new ImageIcon("me.jpg"));
					JLabel jl3 = new JLabel("CSCI201 USC: Assignment 3");
					jp.add(jl);
					jp.add(jl2);
					jp.add(jl3);
					jf.add(jp);
					jf.setVisible(true);
			}
		});
		
		jm.add(howtoitem);
		jm.add(aboutitem);
		JMenuBar jmb = new JMenuBar();
		jmb.add(jm);
		setJMenuBar(jmb);
		
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