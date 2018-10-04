import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Player extends Thread{
	NPSOrderedArrayList<Card> hand;
	String name;
	int score;
	int sum;
	Interface View;
	//constructor
	public Player() {
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
	}
	public Player(String name, Interface i) {
		this.name = name;
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
		View = i;
	}
	
	public Player(String name) {
		this.name = name;
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
	}
		
	public Card play(int index) {
		Card cardPlayed = this.hand.get(index);
		hand.remove(index);
		return cardPlayed;
	}
	
	public void setPlayerName(String name) {
		this.name = name;
	}
	
	
	//***********Stuff for Client****************************************
	InetAddress serverIP;
	TestGui gui;
	int sendPort = 30480;
	int receivePort;
	PlayerData myData;
	Boolean myTurn = false;
	
	//sending port 30480
	//receiving port 5000-5002
	public Player(String name, TestGui gui,InetAddress serverIP, int port) {
		this.name = name;
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
		this.serverIP = serverIP;
		this.gui = gui;
		receivePort = port;
		myData = new PlayerData(name, port);
	}
	
	public void setServerIP(InetAddress serverIP) {
		this.serverIP = serverIP;
	}
	
	public void sendToServer(Object object) throws IOException{
		DatagramSocket sendSocket = new DatagramSocket();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(object);
		byte[] data = outputStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, sendPort);
		sendSocket.send(packet);
		sendSocket.close();
	}
	
	public void listenForServerStrings() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			String message = (String) objectStream.readObject();
			//in the final program, we'll check if the object is a String, card, or hand
			gui.display(message);
			System.out.println(message);
			//compare strings to cause an effect
			if(message.equals("Start Game")) {
				gui.gotoTestWindow();
				gui.display("Game started. Waiting...");
			}
			if(message.equals("Your Turn")) {
				gui.display("It's your turn, " + name);
				gui.btnPlay.setEnabled(true);
				myTurn = true;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForAck() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			String message = (String) objectStream.readObject();
			gui.display(message);
			System.out.println(message);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerHands() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			NPSOrderedArrayList<Card> message = (NPSOrderedArrayList<Card>) objectStream.readObject();
			System.out.println("It worked " + message.get(0).value);
			hand = message;
			gui.display("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerCards() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			Card message = (Card) objectStream.readObject();
			System.out.println("It worked " + message.value);
			gui.display(message.suit + message.value + " has been played");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	//add listenForServerScores
	
	public void connect() {
		try {
			sendToServer(myData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			listenForServerStrings();
			gui.btnConnect.setEnabled(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForStrings() {
		//wait to start game
		try {
			listenForServerStrings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//send ack
		try {
			sendToServer("Ack");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveHand() {
		//wait to get hand
		try {
			listenForServerHands();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//send ack
		try {
			sendToServer("Ack");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void run() {
		connect();
		//start game
		waitForStrings();
		receiveHand();
		int round = 1;
		//while(round <= 17) {
			//start round
			waitForStrings();
			//wait for turns
			waitForStrings();
			//receive ack if it's my turn
			if(myTurn) {
				try {
					listenForAck();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				myTurn = false;
			}
			
		//}
	}
	
}
