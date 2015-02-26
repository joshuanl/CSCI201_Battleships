package cs201;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


//start and end point of ship and make a vector, then check for intersection 
//add remove 

public class PlaceShipWindow extends JFrame{
	public static final long serializationUID = 1;
	private static JFrame windowFrame;	
	private int grid[][];
	private int startX;
	private int startY;
	private JButton placeShipButton;
	public PlaceShipWindow(String title, int placementGrid[][], int startX, int startY, int numOf_AC, 
							int numOf_BS, int numOf_C, int numOf_D){
		super(title);
		setLocation(300,300);
		setSize(275,150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		grid = placementGrid;
		this.startX = startX;
		this.startY = startY;
		
		JPanel jp1 = new JPanel();
		jp1.setLayout(new BoxLayout(jp1, BoxLayout.X_AXIS));
		JLabel jl = new JLabel("Select Ship   ");
		jp1.add(jl);
		Vector<String> listofships = new Vector<String>();
		for(int i=0; i < numOf_AC; i++){
			listofships.add("Aircraft Carrier");
		}//end of for
		for(int i=0; i < numOf_BS; i++){
			listofships.add("Battleship");
		}//end of for
		for(int i=0; i < numOf_C; i++){
			listofships.add("Cruiser");
		}//end of for
		for(int i=0; i < numOf_D; i++){
			listofships.add("Destroyer");
		}//end of for
		JPanel comboboxPanel = new JPanel();
		JComboBox<String> jcb = new JComboBox<String>(listofships);
		//comboboxPanel.add(jcb);
		//jp1.add(comboboxPanel);
		jcb.setPreferredSize(new Dimension(10,10));
		jp1.add(jcb);
		add(jp1);
		
		ButtonGroup jbg = new ButtonGroup();
		JRadioButton northRB = new JRadioButton("Face North");
		JRadioButton eastRB = new JRadioButton("Face East");
		JRadioButton southRB = new JRadioButton("Face South");
		JRadioButton westRB = new JRadioButton("Face West");
		jbg.add(northRB);
		jbg.add(eastRB);
		jbg.add(southRB);
		jbg.add(westRB);
		
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));
		jp2.add(northRB);
		jp2.add(eastRB);
		add(jp2);
		
		JPanel jp3 = new JPanel();
		jp3.setLayout(new BoxLayout(jp3, BoxLayout.X_AXIS));
		jp3.add(southRB);
		jp3.add(westRB);
		add(jp3);
		
		JPanel bottomPanel = new JPanel();
		placeShipButton = new JButton("Place Ship");
		bottomPanel.add(placeShipButton);

		add(bottomPanel);		
		
		//setVisible(true);
		windowFrame = this;
		//windowFrame.setVisible(true);
	}//end of constructor 
	
	//public Battleship(char tag, Point startPoint, Point endPoint)
	public char showScreen(){
		char tag = 'A';
		windowFrame.setVisible(true);
		
		return tag;
	}//end of 
}//end of class
