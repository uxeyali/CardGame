import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import com.sun.glass.events.WindowEvent;
import java.awt.Font;

/* *****************This Class only exists to test the Networking***************** */
public class TestGui extends JFrame{
	JFrame Main = new JFrame();
	JPanel panel = new JPanel();
	JPanel ServerPanel = new JPanel();
	JPanel joinPanel = new JPanel();
	JPanel testPanel = new JPanel();
	JPanel GamePanel = new JPanel();
	//client
	JTextArea textAreaJoin;
	TestGui joinView = this;
	static int PlayerNo = 0;
	private JTextField textFieldName;
	private JTextField textFieldNameServer;
	JTextField textFieldPort;
	JLabel titleLbl;
	JButton btnCreateServer;
	JButton btnJoinServer;
	JButton btnConnect;
	JButton btnPlay;
	String playerName;
	InetAddress serverIP;
	JTextField enterTxt;
	Player p;
	JTextArea textAreaScores;
	//server
	CardGameServer s;
	
	public TestGui() {
		this.gotoMain();
	}
	
	void gotoMain() {
		Main.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Main.setSize(new Dimension(230, 310));
		Main.getContentPane().setLayout(null);
		Main.setVisible(true);
		
		panel.setSize(new Dimension(170, 240));
		panel.setBounds(0, 10, 214, 249);
		Main.getContentPane().add(panel);
		panel.setLayout(null);
		
		titleLbl = new JLabel("Card Game");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Arial Black", Font.PLAIN, 18));
		titleLbl.setBounds(38, 11, 126, 23);
		panel.add(titleLbl);
		
		btnCreateServer = new JButton("Create Game");
		btnCreateServer.setBounds(38, 44, 126, 23);
		panel.add(btnCreateServer);
		btnCreateServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoCreateServer();
			}
		});
		
		btnJoinServer = new JButton("Join Game");
		btnJoinServer.setBounds(38, 78, 126, 23);
		btnJoinServer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gotoJoin();
			}
		});
		panel.add(btnJoinServer);
		Main.repaint();
	}
	
	void gotoTestWindow() {
		//close previous windows
		ServerPanel.setVisible(false);
		joinPanel.setVisible(false);
		panel.setVisible(false);
		//builds test window
//		JFrame test = new JFrame();
//		test.setSize(new Dimension(230, 310));
//		test.getContentPane().setLayout(null);
//		test.setVisible(true);
		testPanel.setSize(new Dimension(170, 240));
		testPanel.setBounds(10, 11, 307, 249);
		Main.getContentPane().add(testPanel);
		testPanel.setLayout(null);
		//adds textArea
		
		textAreaJoin = new JTextArea();
		JScrollPane scrollPane = new JScrollPane();
		DefaultCaret caret = (DefaultCaret) textAreaJoin.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		testPanel.add(scrollPane);
		scrollPane.setBounds(0, 99, 180, 139);
		scrollPane.setViewportView(textAreaJoin);
		//adds textAreaScores
		textAreaScores = new JTextArea();
		textAreaScores.setBounds(0, 0, 80, 50);
		testPanel.add(textAreaScores);
		//adds button
		btnPlay = new JButton("Play Card");
		btnPlay.setBounds(50, 70, 100, 23);
		btnPlay.setEnabled(false);
		testPanel.add(btnPlay);
		//plays first card
		btnPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				display("You played: " + p.hand.get(0).suit + p.hand.get(0).value);
				try {
					p.sendToServer(p.hand.get(0));
					btnPlay.setEnabled(false);
				} catch (IndexOutOfBoundsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//removes card from hand
				p.play(0);
			}
		});
		Main.repaint();
	}
	
	void gotoCreateServer() {
		//turns off old window
		panel.setVisible(false);
		//builds new window
		ServerPanel.setSize(new Dimension(170, 240));
		ServerPanel.setBounds(10, 11, 307, 249);
		Main.getContentPane().add(ServerPanel);
		ServerPanel.setLayout(null);
		
		//components
		JLabel serverLbl = null;
		try {
			serverLbl = new JLabel("Server IP Address: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverLbl.setBounds(0, 5, 200, 23);
		ServerPanel.add(serverLbl);
		
		textFieldNameServer = new JTextField();
		textFieldNameServer.setBounds(0, 70, 100, 23);
		ServerPanel.add(textFieldNameServer);
		textFieldNameServer.setColumns(10);
		JLabel lblNameServer = new JLabel("Name:");
		lblNameServer.setBounds(0, 55, 46, 14);
		ServerPanel.add(lblNameServer);
		
		textFieldPort = new JTextField("50000");
		textFieldPort.setBounds(100, 70, 100, 23);
		ServerPanel.add(textFieldPort);
		textFieldPort.setColumns(10);
		JLabel lblPort = new JLabel("Port Number:");
		lblPort.setBounds(100, 55, 90, 14);
		ServerPanel.add(lblPort);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(102, 30, 85, 23);
		ServerPanel.add(btnConnect);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 99, 180, 139);
		ServerPanel.add(scrollPane);
		
		textAreaJoin = new JTextArea();
		scrollPane.setViewportView(textAreaJoin);
		
		//makeServer();
		
		//connects to a server
		btnConnect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//makeServer();
				makeServerClient();
			}
		});
		Main.repaint();
	}
	
	void gotoJoin() {
		//turns off old window
		panel.setVisible(false);
		//builds new window
		joinPanel.setSize(new Dimension(170, 240));
		joinPanel.setBounds(10, 11, 307, 249);
		Main.getContentPane().add(joinPanel);
		joinPanel.setLayout(null);
		
		//components
		textFieldName = new JTextField();
		textFieldName.setBounds(0, 70, 100, 23);
		joinPanel.add(textFieldName);
		textFieldName.setColumns(10);
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(0, 55, 46, 14);
		joinPanel.add(lblName);
		
		textFieldPort = new JTextField("50001");
		textFieldPort.setBounds(100, 70, 100, 23);
		joinPanel.add(textFieldPort);
		textFieldPort.setColumns(10);
		JLabel lblPort = new JLabel("Port Number:");
		lblPort.setBounds(100, 55, 90, 14);
		joinPanel.add(lblPort);
		
		JLabel enterLbl = new JLabel("Enter Server IP Address:");
		enterLbl.setBounds(0, 5, 200, 23);
		joinPanel.add(enterLbl);
		try {
			enterTxt = new JTextField(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enterTxt.setBounds(0, 30, 100, 23);
		joinPanel.add(enterTxt);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(102, 30, 85, 23);
		joinPanel.add(btnConnect);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 99, 180, 139);
		joinPanel.add(scrollPane);
		
		textAreaJoin = new JTextArea();
		scrollPane.setViewportView(textAreaJoin);
		
		//connects to a server
		btnConnect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				makeClient();
			}
		});
		Main.repaint();
	}//end of method
	
	
	//********************This is used to display values to the player****************
	void display(String message) {
		textAreaJoin.append(message + "\n");
	}
	
	void displayScores(String messgae) {
		textAreaScores.setText(messgae);
	}
	//********************************************************************************
	
	//**********************Stuff for Server********************************
	//starts server object
	void makeServer() {
		//Starts server
		s = new CardGameServer();
		s.start();
	}
	
	//starts a client on the same server screen
	void makeServerClient() {
		//makeServer();
		playerName = textFieldNameServer.getText();
		//checks if the port is in use
		int compare = Integer.parseInt(textFieldPort.getText());
		if(compare == 30480) {
			display("This port is used by the Server.\nEnter another Port Number");
		}
		else {
			makeServer();
			try {
				p = new Player(playerName, this, InetAddress.getLocalHost(), compare);
				//p.start();
				p.connect();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//starts on client through joining
	void makeClient() {
		playerName = textFieldName.getText();
		int compare = Integer.parseInt(textFieldPort.getText());
		if(compare == 30480) {
			display("This port is used by the Server.\nEnter another Port Number");
		}
		else {
			try {
				serverIP = InetAddress.getByName(enterTxt.getText());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p = new Player(playerName, this, serverIP, compare);
			//p.start();
			p.connect();
		}
	}
	
	void openGameGui() {
		GameGui g = new GameGui();
		p.setGameGui(g);
	}
}
