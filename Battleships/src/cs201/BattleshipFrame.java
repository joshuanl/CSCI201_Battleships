package cs201;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BattleshipFrame extends JFrame{
	
	
	public BattleshipFrame() {
		super("Battleship");
		
		setSize(1100,700);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				new ConnectWindow();
			}
		});
		
		
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
		JMenuItem aboutitem = new JMenuItem("About");
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
		
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic('Q');
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				dispose();
			}
		});	
		
		
		jm.add(howtoitem);
		jm.add(aboutitem);
		jm.add(quitItem);
		JMenuBar jmb = new JMenuBar();
		jmb.add(jm);
		setJMenuBar(jmb);
		
		//add(bsg);
		setVisible(true);
	}//end of constructor
	
	public void addGame(BattleshipGrid bsg){
		add(bsg);
	}
}//end of class