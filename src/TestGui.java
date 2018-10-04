import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;

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
	JButton btnConnect;
	JButton btnPlay;
	String playerName;
	InetAddress serverIP;
	JTextField enterTxt;
	Player p;
	
	public TestGui() {
		this.gotoMain();
	}
	
	void gotoMain() {
		Main.setSize(new Dimension(230, 310));
		Main.getContentPane().setLayout(null);
		Main.setVisible(true);
		
		panel.setSize(new Dimension(170, 240));
		panel.setBounds(20, 10, 307, 249);
		Main.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnCreateServer = new JButton("Create Server");
		btnCreateServer.setBounds(38, 123, 126, 23);
		panel.add(btnCreateServer);
		btnCreateServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoCreateServer();
			}
		});
		
		JButton btnJoinServer = new JButton("Join Server");
		btnJoinServer.setBounds(38, 58, 126, 23);
		btnJoinServer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gotoJoin();
			}
		});
		panel.add(btnJoinServer);
		
		JButton btnLocalGame = new JButton("Local Game");
		btnLocalGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CardGame CG = new CardGame();
				
			}
		});
		btnLocalGame.setBounds(38, 10, 126, 23);
		panel.add(btnLocalGame);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 10, 301);
		Main.getContentPane().add(panel_1);
	}
	
	void gotoTestWindow() {
		//close previous windows
		ServerPanel.setVisible(false);
		joinPanel.setVisible(false);
		panel.setVisible(false);
		//builds test window
		testPanel.setSize(new Dimension(170, 240));
		testPanel.setBounds(10, 11, 307, 249);
		Main.getContentPane().add(testPanel);
		testPanel.setLayout(null);
		//adds textArea
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 99, 180, 139);
		testPanel.add(scrollPane);
		textAreaJoin = new JTextArea();
		scrollPane.setViewportView(textAreaJoin);
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
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(102, 70, 85, 23);
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
				makeServer();
				makeServerClient();
			}
		});
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
	}//end of method
	
	void display(String message) {
		textAreaJoin.append(message + "\n");
	}
	
	
	
	//starts server object
	void makeServer() {
		//Starts server
		CardGameServer s = new CardGameServer();
		s.start();
	}
	
	//starts a client on the same server screen
	void makeServerClient() {
		//makeServer();
		playerName = textFieldNameServer.getText();
		try {
			//player1 is port 5000
			p = new Player(playerName, this, InetAddress.getLocalHost(), 5000);
			p.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//starts on client through joining
	void makeClient() {
		playerName = textFieldName.getText();
		try {
			serverIP = InetAddress.getByName(enterTxt.getText());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p = new Player(playerName, this, serverIP, Integer.parseInt(textFieldPort.getText()));
		p.start();
	}
}
