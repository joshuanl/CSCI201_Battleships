package cs201;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private boolean isClient = false;
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
		getRootPane().setDefaultButton(connectButton);
		hostGameRB.setEnabled(true);
		customPortRB.setEnabled(true);
		mapsRB.setEnabled(true);
		setVisible(true);
	}//end of constructor
	
	public void initVars(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		topLabel = new JLabel("Your IP: ");
		try {
			localIP = InetAddress.getLocalHost().getHostAddress().toString();
			topLabel.setText(topLabel.getText() + ""+localIP);
		} catch (UnknownHostException e) {
			System.out.println("UknownHost Exception, couldnt get host IP: " + e.getMessage());
			hasInternet = false;
		}//end of try
		
		nameLabel = new JLabel("Name:");
		hostGameLabel = new JLabel("Host Game --");
		enterIPLabel = new JLabel("Enter an IP:");
		customPortLabel = new JLabel("Custom Port:");
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
				if(!hostGameRB.isSelected() && !mapsRB.isSelected() && ipTextField.getText().length() != 0 && portTextField.getText().length() != 0){
					isClient = true;
					usingMaps = false;
					isHost = false;
				}
				connectToGame();
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
					usingMaps = false;
					isHost = true;
					mapsRB.setEnabled(false);
					mapsRB.setSelected(false);
					mapsTextField.setEnabled(false);
					customPortRB.setEnabled(true);
					ipTextField.setEnabled(true);
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
					portTextField.setEnabled(true);
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
					customPortRB.setSelected(false);
				}
				else{
					usingMaps = false;
					hostGameRB.setSelected(false);
					customPortRB.setSelected(false);

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
			connectButton.setEnabled(false);
		}
		else{
			try {
				localIP = InetAddress.getLocalHost().getHostAddress().toString();
				topLabel.setText("Your IP: "+localIP);
			} catch (UnknownHostException e) {
				System.out.println("UknownHost Exception, couldnt get host IP in connectionUpdate: " + e.getMessage());
				hasInternet = false;
			}//end of try
			hostGameRB.setEnabled(true);
			hostGameLabel.setEnabled(true);
			customPortRB.setEnabled(true);
			connectButton.setEnabled(true);
		}
		
	}//end of updating textfields and buttons due to status of internet connection
	
	public void connectToGame(){
		Vector<String> mapContentsVector = new Vector<String>();
		if(nameTextField.getText().length() == 0 || nameTextField.getText().charAt(0) == ' '){
			nameTextField.setText("No Name");
		}
		if(usingMaps){
			URL toCheckIp;
			String temp = mapsTextField.getText() + ".battle";
			try {
				toCheckIp = new URL("http://www-scf.usc.edu/~csci201/assignments/"+temp);
				BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
				temp = in.readLine();
				while(temp != null){
					//System.out.println("got line: "+temp);
					mapContentsVector.add(temp);
					temp = in.readLine();
				}//end of while	
			} catch (MalformedURLException e) {
				System.out.println("malformedurl exception in connectwindow.connecttogame(), could not read address: "+e.getMessage());
				return;
			} catch (IOException e) {
				System.out.println("IOE in connectwindow.connecttogame(), could not readline: "+e.getMessage());
				JOptionPane.showMessageDialog(ConnectWindow.this, "Unable to locate file, please enter another file name", "Looking for File: "+temp, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			setVisible(false);
			BattleshipFrame bsf = new BattleshipFrame();
			BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, usingMaps, "0", "0" , mapContentsVector, nameTextField.getText());
			bsf.add(bsg);
			dispose();
			return;
		}//end of if
		try{
			int n = Integer.parseInt(portTextField.getText());
		}catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(ConnectWindow.this, "Invalid port entry", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(isHost){
			BattleshipFrame bsf = new BattleshipFrame();
			BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, usingMaps, ipTextField.getText(), portTextField.getText() , mapContentsVector, nameTextField.getText());
			bsf.add(bsg);
			dispose();
		}
		else if(isClient){
			BattleshipFrame bsf = new BattleshipFrame();
			BattleshipGrid bsg = new BattleshipGrid(bsf, isHost, usingMaps, ipTextField.getText(), portTextField.getText() , mapContentsVector, nameTextField.getText());
			bsf.add(bsg);
			dispose();
		}
	}//end of connecting to the game server
}//end of class
