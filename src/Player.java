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
	
	//constructor
	public Player() {
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
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
	//sending port 30480
	//receiving port 5000
	public Player(String name, TestGui gui,InetAddress serverIP) {
		this.name = name;
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
		this.serverIP = serverIP;
		this.gui = gui;
	}
	
	public void setServerIP(InetAddress serverIP) {
		this.serverIP = serverIP;
	}
	
	public void sendObjectToServer(Object object) throws IOException{
		DatagramSocket sendSocket = new DatagramSocket();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(object);
		byte[] data = outputStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, 30480);
		sendSocket.send(packet);
		sendSocket.close();
	}
	
	public void listenForServerStrings() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(5000);
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerHands() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(5000);
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
			//needs more code to work correctly...
			
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
			sendObjectToServer(name);
//			Card c = new Card("c",12);
//			NPSOrderedArrayList<Card> n = new NPSOrderedArrayList<Card>();
//			n.add(c);
//			sendObjectToServer(n);
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
	
}
