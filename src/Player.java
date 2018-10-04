import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.BindException;
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
	int sendPort = 30480;
	int receivePort;
	PlayerData myData;
	Boolean myTurn = false;
	DatagramSocket receiveSocket;
	
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
	
	public void listenForServerStrings() throws IOException, BindException, ClassCastException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
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
		receiveSocket = new DatagramSocket(receivePort);
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
	
	public void listenForServerHands() throws IOException, ClassCastException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			NPSOrderedArrayList<Card> message = (NPSOrderedArrayList<Card>) objectStream.readObject();
			System.out.println("Got a hand " + message.get(0).value);
			hand = message;
			gui.display("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerCards() throws IOException, ClassCastException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			Card message = (Card) objectStream.readObject();
			System.out.println("Got a card: " + message.suit + message.value);
			gui.display(message.suit + message.value + " has been played");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerCardArrays() throws IOException, ClassCastException{
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
			Card[] message = (Card[]) objectStream.readObject();
			System.out.println("Got a card array: " + message[0].suit + message[0].value);
			gui.displayScores(message[0].suit + ": " + message[0].value + "\n" 
								+ message[1].suit + ": " + message[1].value + "\n"
								+ message[2].suit + ": " + message[2].value + "\n");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	boolean finished = true;
	public void listenForAnything() throws IOException, BindException{
		finished = false;
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		//gui.display("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream1 = new ByteArrayInputStream(data);
		ByteArrayInputStream inputStream2 = new ByteArrayInputStream(data);
		ByteArrayInputStream inputStream3 = new ByteArrayInputStream(data);
		ByteArrayInputStream inputStream4 = new ByteArrayInputStream(data);
		ObjectInputStream objectStream1 = new ObjectInputStream(inputStream1);
		ObjectInputStream objectStream2 = new ObjectInputStream(inputStream2);
		ObjectInputStream objectStream3 = new ObjectInputStream(inputStream3);
		ObjectInputStream objectStream4 = new ObjectInputStream(inputStream4);
		//check if a String
		try {
			String message = (String) objectStream1.readObject();
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
		}catch (ClassCastException e) {
				System.out.println("Not correct object. Moving on...");
				//gui.display("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		//check if hand
		try {
			NPSOrderedArrayList<Card> message = (NPSOrderedArrayList<Card>) objectStream2.readObject();
			System.out.println("Got a hand " + message.get(0).value);
			hand = message;
			gui.display("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
			//gui.display("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		//check if card
		try {
			Card message = (Card) objectStream3.readObject();
			System.out.println("Got a card: " + message.suit + message.value);
			gui.display(message.suit + message.value + " has been played");
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
			//gui.display("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		//check if card array
		try {
			Card[] message = (Card[]) objectStream4.readObject();
			System.out.println("Got a card array: " + message[0].suit + message[0].value);
			gui.displayScores(message[0].suit + ": " + message[0].value + "\n" 
								+ message[1].suit + ": " + message[1].value + "\n"
								+ message[2].suit + ": " + message[2].value + "\n");
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
			//gui.display("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			//gui.display("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		finished = true;
		receiveSocket.close();
	}
	
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
		} catch (BindException e) {
			gui.display("This port number is already in use. Try another one");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForStrings() {
		//wait to start game
		boolean failed = true;
		//while(failed) {
			failed = false;
			try {
				listenForServerStrings();
			} catch (ClassCastException e) {
				System.out.println("Received wrong object");
				gui.display("Received wrong object");
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			}
		//}
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
		boolean failed = true;
		//while(failed) {
			failed = false;
			try {
				listenForServerHands();
			} catch (ClassCastException e) {
				System.out.println("Received wrong object");
				gui.display("Received wrong object");
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			}
		//}
		//send ack
		try {
			sendToServer("Ack");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendCard() {
		gui.display("You played: " + hand.get(0).suit + hand.get(0).value);
		try {
			sendToServer(hand.get(0));
			gui.btnPlay.setEnabled(false);
		} catch (IndexOutOfBoundsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//removes card from hand
//		try {
//			listenForAck();
//			play(0);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void waitForCards() {
		//wait to get a card
		boolean failed = true;
	//	while(failed) {
			failed = false;
			try {
				listenForServerCards();
			} catch (ClassCastException e) {
				System.out.println("Received wrong object");
				gui.display("Received wrong object");
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			}
		//}
		//send ack
		try {
			sendToServer("Ack");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForScores() {
		//wait to get scores
		boolean failed = true;
		//while(failed) {
			failed = false;
			try {
				listenForServerCardArrays();
			} catch (ClassCastException e) {
				System.out.println("Received wrong object");
				gui.display("Received wrong object");
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				receiveSocket.close();
				e.printStackTrace();
			}
		//}
		//send ack
		try {
			sendToServer("Ack");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForAck() {
		try {
			listenForAck();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForAnything() {
		try {
			listenForAnything();
		} catch (BindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		//wait for scores
		waitForScores();
		while(finished) {
			waitForAnything();
		}
	}
	
//	int round = 1;
//	while(round <= 17) {
//		//start round
//		waitForStrings();
//		//wait for 1st player's turns
//		waitForStrings();
//		waitForCards();		//happens here too...
//		//wait for 2nd player's turn
//		waitForStrings();
//		waitForCards();		
//		//wait for 3rd player's turn
//		waitForStrings();
//		waitForCards();		//one of the clients is receiving a String instead of a Card...
//		//wait for winner
//		waitForStrings();	//one of the clients is receiving a Card here instead of a String...
//		//wait for scores
//		waitForScores();
//		round++;
//	}
	
}
