import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	//client
	JTextArea textAreaJoin;
	TestGui joinView = this;
	static int PlayerNo = 0;
	private JTextField textFieldName;
	private JTextField textFieldNameServer;
	JButton btnConnect;
	String playerName;
	InetAddress serverIP;
	JTextField enterTxt;
	
	public TestGui() {
		this.gotoMain();
	}
	
	void gotoMain() {
		Main.setSize(new Dimension(230, 310));
		Main.getContentPane().setLayout(null);
		Main.setVisible(true);
		
		panel.setSize(new Dimension(170, 240));
		panel.setBounds(10, 11, 307, 249);
		Main.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnCreateServer = new JButton("Create Server");
		btnCreateServer.setBounds(38, 5, 126, 23);
		panel.add(btnCreateServer);
		btnCreateServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoCreateServer();
			}
		});
		
		JButton btnJoinServer = new JButton("Join Server");
		btnJoinServer.setBounds(38, 42, 126, 23);
		btnJoinServer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gotoJoin();
			}
		});
		panel.add(btnJoinServer);
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
			Player p = new Player(playerName, this, InetAddress.getLocalHost());
			p.connect();
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
		Player p = new Player(playerName, this, serverIP);
		p.connect();
	}
	
	
}
