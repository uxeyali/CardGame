import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class CardGameServer extends CardGame{
	//sending port 5000-5002
	//receiving port 30480
	int sendPort;
	int receivePort = 30480;
	Player players[] = new Player[3];
	int playerCount = 0;
	InetAddress clientIP[] = new InetAddress[3];
	int IPcount = 0;
	int ports[] = new int[3];
	int portCount = 0;
	
	public void listenAndAddClient() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		clientIP[IPcount] = packet.getAddress();
		IPcount++;
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			PlayerData message = (PlayerData) objectStream.readObject();
			//in the final program, we'll check if the object is a String and card
			players[playerCount] = new Player(message.name);
			playerCount++;
			ports[portCount] = message.port;
			portCount++;
			System.out.println(message.name + " " + message.port);
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForAck() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			String message = (String) objectStream.readObject();
			System.out.println(message);
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForClientsCard() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(receivePort);
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			Card message = (Card) objectStream.readObject();
			System.out.println("Card received: " + message.suit + message.value);
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void sendObjectTo(Object object, InetAddress ip, int port) throws IOException{
		DatagramSocket sendSocket = new DatagramSocket();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(object);
		byte[] data = outputStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		sendSocket.send(packet);
		sendSocket.close();
	}
	
	//connects three players
	public void findPlayers() {
			for(int i = 0; i < 3; i++) {
				try {
					listenAndAddClient();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sendObjectTo(players[i].name + " connected", clientIP[i], ports[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//end of loop
		}
	
	public void startGame() {
		for(int i = 0; i < 3; i++) {
			try {
				sendObjectTo("Start Game", clientIP[i], ports[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//send hand to player
	public void sendHand(NPSOrderedArrayList<Card> hand, InetAddress ip, int port) {
		try {
			sendObjectTo(hand, ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			listenForAck();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//tell everyone what round it is
	public void sendRound(String m) {
		for(int i = 0; i < 3; i++) {
			try {
				sendObjectTo(m, clientIP[i], ports[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//tell everyone who's turn it is
	public void sendTurn(String m, InetAddress ip, int port) {
		try {
			sendObjectTo(m, ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			listenForAck();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveCard(String ack, InetAddress ip, int port) {
		try {
			listenForClientsCard();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sendObjectTo(ack, ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		findPlayers();
		startGame();
		startOnlineGame();
	}
	
	/* ************The Online Game Starts Here*********************** */
	//not finished yet
	public void startOnlineGame() {
		// TODO Auto-generated method stub
		GameState gameState = new GameState();
		int round = 1;

//==========Initialize players====================================
		Player p1 = players[0];
		Player p2 = players[1];
		Player p3 = players[2];
		Player currentPlayer = p1;
		
//==========Create a new deck=====================================
		Deck maindeck = new Deck();
		NPSOrderedArrayList<Card> deck = maindeck.create();
//		Shuffle the deck
		deck = maindeck.shuffle(deck);
		
//		Distribute the cards amongst the players hands
		distribute(p1,p2,p3, deck);
		sendHand(p1.hand, clientIP[0], ports[0]);
		sendHand(p2.hand, clientIP[1], ports[1]);
		sendHand(p3.hand, clientIP[2], ports[2]);
		
		Card leadingCard = new Card();
		Card fstCard = new Card();
		Card sndCard = new Card();
		Card thrdCard = new Card();
		int maxValue;
		
		//We are testing so round is at 1 and not 17
		while(round<=1) {
			
			System.out.println("Round " + round + ": ");
			sendRound("Round " + round + " Starts: ");
			if(currentPlayer == p1) {
				
				sendTurn("Your Turn", clientIP[0], ports[0]);
				sendTurn(players[0].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn(players[0].name + "'s Turn", clientIP[2], ports[2]);
				
				//gets card and sends ack
				receiveCard("Ack", clientIP[0], ports[0]);
				
//				fstCard = p1.play(0);
//				sndCard = p2.play(0);
//				thrdCard = p3.play(0);
//				
//
////				These will be 
//				leadingCard = fstCard;
//				maxValue = fstCard.value;
//				System.out.println("CurrentPlayer is p1");
//				System.out.println("Leading "+leadingCard.suit+leadingCard.value );
//				System.out.println("p1's Card "+fstCard.suit+fstCard.value );
//				System.out.println("p2's Card "+sndCard.suit+sndCard.value );
//				System.out.println("p3's Card "+thrdCard.suit+thrdCard.value );
//				
//				if(sndCard.suit == leadingCard.suit && sndCard.value > maxValue) {
//					maxValue=sndCard.value;
//					currentPlayer = p2;
//					p2.score++;
//					p2.sum += sndCard.value;
//					System.out.println(p2.name + " wins Round " + round);
//				} else if(thrdCard.suit == leadingCard.suit && thrdCard.value > maxValue) {
//					maxValue=thrdCard.value;
//					currentPlayer=p3;
//					p3.score++;
//					p3.sum += thrdCard.value;
//					System.out.println(p3.name + " wins Round " + round);
//				} else {
//					p1.score++;
//					p1.sum += fstCard.value;
//					System.out.println(p1.name + " wins Round " + round);
//				}
//
//				System.out.println(p1.score+" "+p2.score+" "+p3.score);
//				
//			} else if(currentPlayer == p2){
//				
//
//				fstCard = p2.play(0);
//				sndCard = p3.play(0);
//				thrdCard = p1.play(0);
//
////				These will be 
//				leadingCard = fstCard;
//				maxValue = fstCard.value;
//				System.out.println("CurrentPlayer is p2");
//				System.out.println("Leading "+leadingCard.suit+leadingCard.value );
//				System.out.println("p2's Card "+fstCard.suit+fstCard.value );
//				System.out.println("p3's Card "+sndCard.suit+sndCard.value );
//				System.out.println("p1's Card "+thrdCard.suit+thrdCard.value );
//				
//				if(sndCard.suit == leadingCard.suit && sndCard.value > maxValue) {
//					maxValue=sndCard.value;
//					currentPlayer = p3;
//					p3.score++;
//					p3.sum += sndCard.value;
//					System.out.println(p3.name + " wins Round " + round);
//				} else if(thrdCard.suit == leadingCard.suit && thrdCard.value > maxValue) {
//					System.out.println("Max Value is: "+maxValue +"  "+ thrdCard.value);
//					maxValue=thrdCard.value;
//					currentPlayer=p1;
//					p1.score++;
//					p1.sum += thrdCard.value;
//					System.out.println(p1.name + " wins Round " + round);
//				} else {
//					p2.score++;
//					p2.sum += fstCard.value;
//					System.out.println(p2.name + " wins Round " + round);
//				}
//
//				System.out.println(p1.score+" "+p2.score+" "+p3.score);
//				
//			} else {
//
//				fstCard = p3.play(0);
//				sndCard = p1.play(0);
//				thrdCard = p2.play(0);
//
//				leadingCard = fstCard;
//				maxValue = fstCard.value;
//
//				System.out.println("CurrentPlayer is p3");
//				System.out.println("Leading "+leadingCard.suit+leadingCard.value );
//				System.out.println("p3's Card "+fstCard.suit+fstCard.value );
//				System.out.println("p1's Card "+sndCard.suit+sndCard.value );
//				System.out.println("p2's Card "+thrdCard.suit+thrdCard.value );
//				
//				if(sndCard.suit == leadingCard.suit && sndCard.value > maxValue) {
//					maxValue=sndCard.value;
//					currentPlayer = p1;
//					p1.score++;
//					p1.sum += sndCard.value;
//					System.out.println(p1.name + " wins Round " + round);
//				} else if(thrdCard.suit == leadingCard.suit && thrdCard.value > maxValue) {
//					maxValue=thrdCard.value;
//					currentPlayer=p2;
//					p2.score++;
//					p2.sum += thrdCard.value;
//					System.out.println(p2.name + " wins Round " + round);
//				} else {
//					p3.score++;
//					p3.sum += fstCard.value;
//					System.out.println(p3.name + " wins Round " + round);
//				}
//
//				System.out.println(p1.score+" "+p2.score+" "+p3.score);
			}
//			System.out.println(p1.sum+" "+p2.sum+" "+p3.sum);
//			
			round++;
////			currentPlayer = checkForWinner();
		}//end of loop
//		checkForWin(p1,p2,p3);
	}//end of online game
}
