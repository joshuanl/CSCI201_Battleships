package cs201;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
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
	private JButton refreshButton;
	private JButton connectButton;
	private JButton clearFieldsButton;
	private String localIP;
	private boolean connectingBoolean = false;
	private boolean hasInternet = true;
	private boolean isHost = false;
	private boolean isCustomPort = false;
	private boolean usingMaps = false;
	
	public ConnectWindow(){
		super("Battleship Menu");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100,50);
		setSize(400,250);
		
		initVars();
		initActions();
		checkConnection();
		connectionUpdate();
		addComponenets();
		
		setVisible(true);
	}//end of constructor
	
	public void initVars(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		topLabel = new JLabel("Your IP: ");
		try {
			localIP = InetAddress.getLocalHost().getHostAddress().toString();
			System.out.println(localIP);
			topLabel.setText(topLabel.getText() + ""+localIP);
		} catch (UnknownHostException e) {
			System.out.println("UknownHost Exception, couldnt get host IP: " + e.getMessage());
			hasInternet = false;
		}//end of try
		
		nameLabel = new JLabel("Name:");
		hostGameLabel = new JLabel("Host Game");
		enterIPLabel = new JLabel("Enter an IP:");
		customPortLabel = new JLabel("Custom Port");
		portLabel = new JLabel("Port:");
		mapsLabel = new JLabel("201 Maps");
		nameTextField = new JTextField(10);
		ipTextField = new JTextField(15);
		portTextField = new JTextField(5);
		mapsTextField = new JTextField(5);
		hostGameRB = new JRadioButton();
		customPortRB = new JRadioButton();
		customPortRB.setEnabled(false);
		mapsRB = new JRadioButton();
		clearFieldsButton = new JButton("Clear Fields");
		connectButton = new JButton("Connect");
		refreshButton = new JButton("Refresh");
	}//end of initializing variables
	
	public void initActions(){
		connectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				connectToGame(isHost);
			}
		});
		
		refreshButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				checkConnection();
			}
		});
		
		clearFieldsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				nameTextField.setText("");
				ipTextField.setText("");
				portTextField.setText("");
				mapsTextField.setText("");
			}
		});
		
		hostGameRB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(hostGameRB.isSelected()){
					isHost = true;
					mapsRB.setEnabled(false);
					mapsRB.setSelected(false);
					mapsTextField.setEnabled(false);
					customPortRB.setEnabled(true);
				}
				else{
					isHost = false;
					mapsRB.setEnabled(true);
					mapsRB.setSelected(false);
					mapsTextField.setEnabled(true);
					customPortRB.setEnabled(false);
					customPortRB.setSelected(false);
				}
			}
		});
		
		customPortRB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(customPortRB.isSelected()){
					isCustomPort = true;
				}
				else{
					isCustomPort = false;
				}
			}
		});
		
		mapsRB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(mapsRB.isSelected()){
					usingMaps = true;
					hostGameRB.setSelected(false);
					ipTextField.setEnabled(false);
					customPortRB.setSelected(false);
					portTextField.setEnabled(false);
				}
				else{
					usingMaps = false;
					hostGameRB.setSelected(false);
					ipTextField.setEnabled(false);
					customPortRB.setSelected(false);
					portTextField.setEnabled(false);

				}
			}
		});
	}//end of creating action listeners
	
	public void addComponenets(){
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		jp.add(topLabel);
		mainPanel.add(jp);
		jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp.add(nameLabel);
		jp.add(nameTextField);
		mainPanel.add(jp);
		jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp.add(hostGameRB);
		jp.add(hostGameLabel);
		jp.add(enterIPLabel);
		jp.add(ipTextField);
		mainPanel.add(jp);
		jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp.add(customPortRB);
		jp.add(customPortLabel);
		jp.add(portLabel);
		jp.add(portTextField);
		mainPanel.add(jp);
		jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp.add(mapsRB);
		jp.add(mapsLabel);
		jp.add(mapsTextField);
		mainPanel.add(jp);
		jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp.add(refreshButton);
		jp.add(connectButton);
		jp.add(clearFieldsButton);
		mainPanel.add(jp);
		add(mainPanel, BorderLayout.CENTER);
	}//end of adding components
	
	public void checkConnection(){
		Socket s = new Socket();
		InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
		try {
			s.connect(address, 5000);
			System.out.println("could connect");
		} catch (IOException e) {
			System.out.println("IO exception in connectwindow.checkconnection(): "+e.getMessage());
			hasInternet = false;
			connectionUpdate();
			return;
		}
		hasInternet = true;
		connectionUpdate();
		return;
	}//end of checking for internet connections
	
	public void connectionUpdate(){
		if(!hasInternet){
			topLabel.setText("Your IP:" + " Error");
			hostGameRB.setEnabled(false);
			hostGameLabel.setEnabled(false);
			customPortRB.setEnabled(false);
			portLabel.setEnabled(false);
			connectButton.setEnabled(false);
		}
		else{
			try {
				localIP = InetAddress.getLocalHost().getHostAddress().toString();
				System.out.println(localIP);
				topLabel.setText("Your IP: "+localIP);
			} catch (UnknownHostException e) {
				System.out.println("UknownHost Exception, couldnt get host IP in connectionUpdate: " + e.getMessage());
				hasInternet = false;
			}//end of try
			hostGameRB.setEnabled(true);
			hostGameLabel.setEnabled(true);
			customPortRB.setEnabled(true);
			portLabel.setEnabled(true);
			connectButton.setEnabled(true);
		}
		
	}//end of updating textfields and buttons due to status of internet connection
	
	public void connectToGame(boolean b){
		
		
	}//end of connecting to the game server
}//end of class
