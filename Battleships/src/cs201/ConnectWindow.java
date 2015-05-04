package cs201;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ConnectWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JLabel topLabel;
	private JLabel nameLabel;
	private JLabel hostGameLabel;
	private JLabel enterIPLabel;
	private JLabel customPortLabel;
	private JLabel portLabel;
	private JLabel mapsLabel;
	private JTextField nameTextField;
	private JTextField ipTextField;
	private JTextField portTextField;
	private JTextField mapsTextField;
	private JRadioButton hostGameRB;
	private JRadioButton customPortRB;
	private JRadioButton mapsRB;
	private JButton refreshButon;
	private JButton connectButton;
	private String localIP;
	
	public ConnectWindow(){
		super("Battleship Menu");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100,50);
		setSize(500,400);
		
		initVars();
		
		setVisible(true);
	}//end of constructor
	
	public void initVars(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		topLabel = new JLabel("Your IP: ");
		try {
			localIP = InetAddress.getLocalHost().getHostAddress().toString();
			System.out.println(localIP);
		} catch (UnknownHostException e) {
			System.out.println("UknownHost Exception, couldnt get host IP: " + e.getMessage());
		}
	}//end of initializing variables
	
	
}//end of class
