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

//******************This contains communications for the Player to the Server and the Player's info*****************
public class Player extends Thread{
	NPSOrderedArrayList<Card> hand;
	String name;
	int score;
	int sum;
	GameGui gameGui;
	private Card lastPlayed = null;
	
	//constructor
	public Player() {
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
	}
	public Player(String name, GameGui i) {
		this.name = name;
		this.hand = new NPSOrderedArrayList<Card>();
		this.score = 0;
		this.sum = 0;
		gameGui = i;
		gameGui.setPlayer(this);
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
		lastPlayed = cardPlayed;
		return cardPlayed;
	}
	
	public Card getLastPlayed() {
		while(lastPlayed == null) {
			//loops until a card is played
		}
		return lastPlayed;
	}
	
	public void setPlayerName(String name) {
		this.name = name;
	}
	
	public void setGameGui(GameGui g) {
		gameGui = g;
		gameGui.setPlayer(this);
	}
	
	
	//***********Stuff for Client****************************************
	InetAddress serverIP;
	TestGui gui;
	int sendPort = 30480;
	int receivePort;
	PlayerData myData;
	Boolean myTurn = false;
	Boolean mustPlayLead = false;
	String leadingSuit;
	DatagramSocket receiveSocket;
	String lastMessage = "";
	
	//sending port 30480
	//receiving port 5000-5002
	public Player(String name, TestGui gui, InetAddress serverIP, int port) {
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
	
	public void openGameGui() {
		gameGui = new GameGui();
		gameGui.setPlayer(this);
		gameGui.setTitle("Card Game: " + name);
		gui.Main.setVisible(false);
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
	
	//***********Various Listen methods. Most are unused****************8
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
			//compare strings to cause an effect
			if(message.equals("Start Game")) {
				//gui.gotoTestWindow();
				openGameGui();
				gameGui.displayMessage("Game started. Waiting...");
				gui.display("Game started. Waiting...");
			}
			else if(message.equals("Your Turn")) {
				gui.display("It's your turn, " + name);
				gui.btnPlay.setEnabled(true);
				myTurn = true;
			}
			else {
				gui.display(message);
				//gameGui.displayMessage(message);
				System.out.println(message);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			gameGui.displayMessage("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForAck() throws IOException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		gameGui.displayMessage("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			String message = (String) objectStream.readObject();
			gui.display(message);
			gameGui.displayMessage(message);
			System.out.println(message);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			gameGui.displayMessage("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerHands() throws IOException, ClassCastException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		gameGui.displayMessage("Waiting...");
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
			gameGui.CreateButtons(hand);
			gui.display("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
			gameGui.displayMessage("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			gameGui.displayMessage("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerCards() throws IOException, ClassCastException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		gameGui.displayMessage("Waiting...");
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
			gameGui.displayMessage(message.suit + message.value + " has been played");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			gameGui.displayMessage("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForServerCardArrays() throws IOException, ClassCastException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		gameGui.displayMessage("Waiting...");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			Card[] message = (Card[]) objectStream.readObject();
			System.out.println("Got a card array: " + message[0].suit + message[0].value);
			gameGui.setScores(message[0].suit + ": " + message[0].value + "\n" 
					+ message[1].suit + ": " + message[1].value + "\n"
					+ message[2].suit + ": " + message[2].value + "\n");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			gui.display("Not correct object. Moving on...");
			gameGui.displayMessage("Not correct object. Moving on...");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	//most important method. Listens for anything the server sends it and interpreters it
	boolean finished = true;
	public void listenForAnything() throws IOException, BindException{
		finished = false;
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		gui.display("Waiting...");
		//gameGui.displayMessage("Waiting...");
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
			//compare strings to cause an effect
			if(message.equals(lastMessage)) {
				System.out.println("Clone message");
			}
			else if(message.equals("Start Game")) {
				//gui.gotoTestWindow();
				lastMessage = message;
				openGameGui();
				gui.display("Game started. Waiting...");
				gameGui.displayMessage("Game started. Waiting...");
			}
			else if(message.equals("Your Turn")) {
				lastMessage = message;
				gui.display("It's your turn, " + name);
				//gui.btnPlay.setEnabled(true);
				gameGui.displayMessage("It's your turn, " + name);
				//disables buttons if I have a leading card
				if(mustPlayLead) {
					gameGui.enableSome(leadingSuit);
					mustPlayLead = false;
				}
				else {
					gameGui.enableAll();
				}
				myTurn = true;
			}
			else if(message.equals("Play Again?")) {
				lastMessage = message;
				gameGui.displayMessage("Play again?");
				gameGui.playAgain();
			}
			else if(message.equals("End Game")) {
				lastMessage = message;
				gameGui.displayMessage("The game is over.\nEnding...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			else if(message.equals("New Game")) {
				lastMessage = message;
				gameGui.textAreaGame.setText("New Game\n");
			}
			else if(gameGui == null) {
				gui.display(message);
				System.out.println(message);
			}
			else {
				lastMessage = message;
				gameGui.displayMessage(message);
				System.out.println(message);
			}
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		}
		//check if hand
		try {
			NPSOrderedArrayList<Card> message = (NPSOrderedArrayList<Card>) objectStream2.readObject();
			System.out.println("Got a hand " + message.get(0).value);
			hand = message;
			gameGui.CreateButtons(hand);
			gui.display("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
			gameGui.displayMessage("Cards have been delt. You got a Hand!\nYour First Card is " + hand.get(0).suit + hand.get(0).value);
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (StreamCorruptedException e) {
			System.out.println("Not correct object. Moving on...");
		}
		//check if card
		try {
			Card message = (Card) objectStream3.readObject();
			System.out.println("Got a card: " + message.suit + message.value);
			gui.display(message.suit + message.value + " has been played");
			gameGui.displayMessage(message.suit + message.value + " has been played");
			gameGui.displayCard(message);
			
			//this fixes an issue where the first player was stuck playing the same suit of their first card
			if(lastPlayed != null) {
				if(message.suit.equals(lastPlayed.suit) && message.value == lastPlayed.value) {
					mustPlayLead = false;
				}
				else if(message.isLead) {
					mustPlayLead = true;
					leadingSuit = message.suit;
				}
			}
			else if(message.isLead) {
				mustPlayLead = true;
				leadingSuit = message.suit;
			}
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		}catch (StreamCorruptedException e) {
			System.out.println("Not correct object. Moving on...");
		}
		//check if card array
		try {
			Card[] message = (Card[]) objectStream4.readObject();
			System.out.println("Got a card array: " + message[0].suit + message[0].value);
			gameGui.clearCards();
			gameGui.setScores("Scores:\n"
					+ message[0].suit + ": " + message[0].value + "\n" 
					+ message[1].suit + ": " + message[1].value + "\n"
					+ message[2].suit + ": " + message[2].value + "\n");
		}catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		}catch (StreamCorruptedException e) {
			System.out.println("Not correct object. Moving on...");
		}
		finished = true;
		receiveSocket.close();
	}
	
	public void connect() {
		try {
			sendToServer(myData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			listenForServerStrings();
			gui.btnConnect.setEnabled(false);
			start();
		} catch (BindException e) {
			gui.display("This port number is already in use by another player.\n Try another one");
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
				gameGui.displayMessage("Received wrong object");
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
	
	//unused
	public void sendCard() {
		gui.display("You played: " + hand.get(0).suit + hand.get(0).value);
		gameGui.displayMessage("You played: " + hand.get(0).suit + hand.get(0).value);
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
				gameGui.displayMessage("Received wrong object");
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
				gameGui.displayMessage("Received wrong object");
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
	
	//the players will listen infinitely for the Server's responses
	public void run() {
		//connect();
		//start game
//		waitForStrings();
//		receiveHand();
//		//wait for scores
//		waitForScores();
		while(finished) {
			waitForAnything();
		}
	}
	
}
