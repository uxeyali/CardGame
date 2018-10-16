import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;


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
	DatagramSocket receiveSocket; //needs to be global so it can close
	//used for bind
	boolean portFailed = true;
	//used if player sends card before Ack is received
	Card badCard;
	boolean hasBadCard = false;
	
	public void listenAndAddClient() throws IOException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			PlayerData message = (PlayerData) objectStream.readObject();
			//This is to ensure the port is not in use
			int check = message.port;
			if(check == ports[0] || check == ports[1] || check == ports[2]) {
				portFailed = true;
				System.out.println("Port in use");
			}
			else {
				clientIP[IPcount] = packet.getAddress();
				IPcount++;
				players[playerCount] = new Player(message.name);
				playerCount++;
				ports[portCount] = message.port;
				portCount++;
				portFailed = false;
			}
			System.out.println(message.name + " " + message.port);
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForAck() throws IOException, SocketTimeoutException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		receiveSocket.setSoTimeout(1000);
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream1 = new ByteArrayInputStream(data);
		ObjectInputStream objectStream1 = new ObjectInputStream(inputStream1);
		//used if it gets a bad card
		ByteArrayInputStream inputStream2 = new ByteArrayInputStream(data);
		ObjectInputStream objectStream2 = new ObjectInputStream(inputStream2);
		try {
			String message = (String) objectStream1.readObject();
			System.out.println(message);
		} catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (StreamCorruptedException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		//listen for a badCard
		try {
			Card message = (Card) objectStream2.readObject();
			badCard = message;
			hasBadCard = true;
			System.out.println("Bad Card received: " + message.suit + message.value);
		} catch (ClassCastException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch (StreamCorruptedException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void listenForEnd() throws IOException{
		byte[] buffer = new byte[1024];
		receiveSocket = new DatagramSocket(receivePort);
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			String message = (String) objectStream.readObject();
			System.out.println(message);
			if(message.equals("Play Again")) {
				sendStringToAll("New Game");
				startOnlineGame();
			}
			else if(message.equals("Quit")) {
				sendEndToAll();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Not correct object. Moving on...");
		} catch(IOException e) {
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public Card listenForClientsCard() throws IOException{
		Card message = null;
		//if received a bad card, use that instead
		if(hasBadCard) {
			message = badCard;
			hasBadCard = false;
		}
		else {
			byte[] buffer = new byte[1024];
			receiveSocket = new DatagramSocket(receivePort);
			System.out.println("Waiting...");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			receiveSocket.receive(packet);
			byte[] data = packet.getData();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			ObjectInputStream objectStream = new ObjectInputStream(inputStream);
			try {
				message = (Card) objectStream.readObject();
				System.out.println("Card received: " + message.suit + message.value);
			} catch (ClassNotFoundException e) {
				System.out.println("Not correct object. Moving on...");
			} catch(IOException e) {
				System.out.println("Not correct object. Moving on...");
			}
			receiveSocket.close();
		}
		return message;
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
	
	//************************Sending and Listening methods for specific things*************************
	//connects three players
	public void findPlayers() {
		//fix port bind issue
			for(int i = 0; i < 3; i++) {
				portFailed = true;
				//this makes sure the port is valid
				while(portFailed) {
					try {
						listenAndAddClient();
					} catch (IOException e) {
						e.printStackTrace();
					}
					//if port did not fail, go forward
					if(!portFailed) {
						try {
							sendObjectTo(players[i].name + " connected", clientIP[i], ports[i]);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}//end of while
			}//end of loop
		}
	
	public void startGame() {
		for(int i = 0; i < 3; i++) {
			boolean failed = true;
			int count = 0;
			while(failed) {
				failed = false;
				try {
					sendObjectTo("Start Game", clientIP[i], ports[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					listenForAck();
				} catch (SocketTimeoutException e) {
					System.out.println("Timed out");
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					failed = true;
					count++;
					receiveSocket.close();
				}
				if(count > 9) {
					sendEndToAll();
				}
			}//end of while
		}
	}
	
	//send hand to player
	public void sendHand(NPSOrderedArrayList<Card> hand, InetAddress ip, int port) {
		boolean failed = true;
		int count = 0;
		while(failed) {
			failed = false;
			try {
				sendObjectTo(hand, ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (SocketTimeoutException e) {
				System.out.println("Timed out");
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			}
			if(count > 9) {
				sendEndToAll();
			}
		}
	}
	
	//tell everyone what round it is
	public void sendStringToAll(String m) {
		
		for(int i = 0; i < 3; i++) {
			boolean failed = true;
			int count = 0;
			while(failed) {
				failed = false;
				try {
					sendObjectTo(m, clientIP[i], ports[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					listenForAck();
				} catch (SocketTimeoutException e) {
					System.out.println("Timed out");
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				} catch (IOException e) {
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				}
				if(count > 9) {
					sendEndToAll();
				}
			}//end while
		}
	}
	
	public void sendString(String m, InetAddress ip, int port) {
		boolean failed = true;
		int count = 0;
		while(failed) {
			failed = false;
			try {
				sendObjectTo(m, ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (SocketTimeoutException e) {
				System.out.println("Timed out");
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			}
			if(count > 9) {
				sendEndToAll();
			}
		}
	}
	
	//tell everyone who's turn it is
	public void sendTurn(String m, InetAddress ip, int port) {
		boolean failed = true;
		int count = 0;
		while(failed) {
			failed = false;
			try {
				sendObjectTo(m, ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (SocketTimeoutException e) {
				System.out.println("Timed out");
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			}
			if(count > 9) {
				sendEndToAll();
			}
		}
	}
	
	//tell everyone what card was played
	public void sendCardToAll(Card c) {
		
		for(int i = 0; i < 3; i++) {
			boolean failed = true;
			int count = 0;
			while(failed) {
				failed = false;
				try {
					sendObjectTo(c, clientIP[i], ports[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					listenForAck();
				} catch (SocketTimeoutException e) {
					System.out.println("Timed out");
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				} catch (IOException e) {
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				}
				if(count > 9) {
					sendEndToAll();
				}
			}//end while
		}
	}
	
	public void sendCard(Card c, InetAddress ip, int port) {
		boolean failed = true;
		int count = 0;
		while(failed) {
			try {
				sendObjectTo(c, ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (SocketTimeoutException e) {
				System.out.println("Timed out");
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			}
			if(count > 9) {
				sendEndToAll();
			}
		}
	}
	
	//send everyone the scores
	public void sendScoresToAll(Card[] c) {
		for(int i = 0; i < 3; i++) {
			boolean failed = true;
			int count = 0;
			while(failed) {
				failed = false;
				try {
					sendObjectTo(c, clientIP[i], ports[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					listenForAck();
				} catch (SocketTimeoutException e) {
					System.out.println("Timed out");
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				} catch (IOException e) {
					failed = true;
					count++;
					receiveSocket.close();
					e.printStackTrace();
				}
				if(count > 9) {
					sendEndToAll();
				}
			}//end while
		}
	}
	
	public void sendScores(Card[] c, InetAddress ip, int port) {
		boolean failed = true;
		int count = 0;
		while(failed) {
			failed = false;
			try {
				sendObjectTo(c, ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (SocketTimeoutException e) {
				System.out.println("Timed out");
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			}
			if(count > 9) {
				sendEndToAll();
			}
		}//end while
	}
	
	public Card receiveCard(String ack, InetAddress ip, int port) {
		
		Card received = null;
		try {
			received = listenForClientsCard();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//maybe send an ack in the final program. Need threads to sleep and loop first

		return received;
	}
	
	public void askToEnd(InetAddress ip, int port) {
		boolean failed = true;
		int count = 0;
		while(failed) {
			failed = false;
			try {
				sendObjectTo("Play Again?", ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				listenForAck();
			} catch (SocketTimeoutException e) {
				System.out.println("Timed out");
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			} catch (IOException e) {
				failed = true;
				count++;
				receiveSocket.close();
				e.printStackTrace();
			}
			if(count > 9) {
				sendEndToAll();
			}
		}//end while
		//wait for answer
		try {
			listenForEnd();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendEndToAll() {
		for(int i = 0; i < 3; i++) {
			try {
				sendObjectTo("End Game", clientIP[i], ports[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendAck(String ack, InetAddress ip, int port) {
		try {
			sendObjectTo(ack, ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		findPlayers();
		startGame();
		startOnlineGame();
	}
	
	/* ************The Online Game Starts Here*********************** */
	//This starts the whole game
	public void startOnlineGame() {
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
		
		//Distribute the cards amongst the players hands
		distribute(p1,p2,p3, deck);
		sendHand(p1.hand, clientIP[0], ports[0]);
		sendHand(p2.hand, clientIP[1], ports[1]);
		sendHand(p3.hand, clientIP[2], ports[2]);
		
		Card leadingCard = new Card();
		Card fstCard = new Card();
		Card sndCard = new Card();
		Card thrdCard = new Card();
		int maxValue;
		Card highestCard;
		
		//set scores to 0
		p1.sum = 0;
		p2.sum = 0;
		p3.sum = 0;
		p1.score = 0;
		p2.score = 0;
		p3.score = 0;
		Card[] scores = new Card[3];
		scores[0] = new Card(p1.name, p1.score);
		scores[1] = new Card(p2.name, p2.score);
		scores[2] = new Card(p3.name, p3.score);
		sendScoresToAll(scores);
		
//		//We are testing so round is at 1 and not 17
		while(round<=17) {
			
			System.out.println("Round " + round + ": ");
			
			sendStringToAll("Round " + round + " Starts: ");
			if(currentPlayer == p1) {
				
				sendTurn(players[0].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn(players[0].name + "'s Turn", clientIP[2], ports[2]);
				sendTurn("Your Turn", clientIP[0], ports[0]);
				
				//gets card and sends ack
				fstCard = receiveCard("The card was send successfully", clientIP[0], ports[0]);
				fstCard.setOrder(1);
				fstCard.makeLead();
				sendCardToAll(fstCard);
				
				//second player's turn
				sendTurn(players[1].name + "'s Turn", clientIP[0], ports[0]);
				sendTurn(players[1].name + "'s Turn", clientIP[2], ports[2]);
				sendTurn("Your Turn", clientIP[1], ports[1]);
				sndCard =  receiveCard("The card was send successfully", clientIP[1], ports[1]);
				sndCard.setOrder(2);
				sendCardToAll(sndCard);
				
				//third player's turn
				sendTurn(players[2].name + "'s Turn", clientIP[0], ports[0]);
				sendTurn(players[2].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn("Your Turn", clientIP[2], ports[2]);
				thrdCard = receiveCard("The card was send successfully", clientIP[2], ports[2]);
				thrdCard.setOrder(3);
				sendCardToAll(thrdCard);
//				
//				//find winner
				leadingCard = fstCard;
				maxValue = fstCard.value;
				highestCard = fstCard;
				
				//check if a higher card was played
				if(sndCard.suit.equals(leadingCard.suit) && sndCard.value > maxValue) {
					maxValue=sndCard.value;
					highestCard = sndCard;
				} 
				if(thrdCard.suit.equals(leadingCard.suit) && thrdCard.value > maxValue) {
					maxValue=thrdCard.value;
					highestCard = thrdCard;
				}
				if(fstCard.suit.equals(leadingCard.suit) && fstCard.value >= maxValue){
					maxValue=fstCard.value;
					highestCard = fstCard;
				}
				//check won one the round
				if(sndCard == highestCard) {
					currentPlayer = p2;
					p2.score++;
					p2.sum += sndCard.value;
					sendStringToAll(p2.name + " wins Round " + round);
					System.out.println(p2.name + " wins Round " + round);
				}
				else if(thrdCard == highestCard) {
					currentPlayer=p3;
					p3.score++;
					p3.sum += thrdCard.value;
					sendStringToAll(p3.name + " wins Round " + round);
					System.out.println(p3.name + " wins Round " + round);
				}
				else if(fstCard == highestCard) {
					p1.score++;
					p1.sum += fstCard.value;
					sendStringToAll(p1.name + " wins Round " + round);
					System.out.println(p1.name + " wins Round " + round);
				}
				
				//need to send scores to players here
				System.out.println(p1.score+" "+p2.score+" "+p3.score);
				scores[0] = new Card(p1.name, p1.score);
				scores[1] = new Card(p2.name, p2.score);
				scores[2] = new Card(p3.name, p3.score);
				sendScoresToAll(scores);
//				
			} else if(currentPlayer == p2){
				sendTurn(players[1].name + "'s Turn", clientIP[0], ports[0]);
				sendTurn(players[1].name + "'s Turn", clientIP[2], ports[2]);
				sendTurn("Your Turn", clientIP[1], ports[1]);
				fstCard =  receiveCard("The card was send successfully", clientIP[1], ports[1]);
				fstCard.setOrder(1);
				fstCard.makeLead();
				sendCardToAll(fstCard);
				
				sendTurn(players[2].name + "'s Turn", clientIP[0], ports[0]);
				sendTurn(players[2].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn("Your Turn", clientIP[2], ports[2]);
				sndCard = receiveCard("The card was send successfully", clientIP[2], ports[2]);
				sndCard.setOrder(2);
				sendCardToAll(sndCard);
				
				sendTurn(players[0].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn(players[0].name + "'s Turn", clientIP[2], ports[2]);
				sendTurn("Your Turn", clientIP[0], ports[0]);
				thrdCard = receiveCard("The card was send successfully", clientIP[0], ports[0]);
				thrdCard.setOrder(3);
				sendCardToAll(thrdCard);

//				These will be 
				leadingCard = fstCard;
				maxValue = fstCard.value;
				highestCard = fstCard;
				
				//check if a higher card was played
				if(sndCard.suit.equals(leadingCard.suit) && sndCard.value > maxValue) {
					maxValue=sndCard.value;
					highestCard = sndCard;
				} 
				if(thrdCard.suit.equals(leadingCard.suit) && thrdCard.value > maxValue) {
					maxValue=thrdCard.value;
					highestCard = thrdCard;
				}
				if(fstCard.suit.equals(leadingCard.suit) && fstCard.value >= maxValue){
					maxValue=fstCard.value;
					highestCard = fstCard;
				}
				//check won one the round
				if(sndCard == highestCard) {
					currentPlayer = p3;
					p3.score++;
					p3.sum += sndCard.value;
					sendStringToAll(p3.name + " wins Round " + round);
					System.out.println(p3.name + " wins Round " + round);
				}
				else if(thrdCard == highestCard) {
					currentPlayer=p1;
					p1.score++;
					p1.sum += thrdCard.value;
					sendStringToAll(p1.name + " wins Round " + round);
					System.out.println(p1.name + " wins Round " + round);
				}
				else if(fstCard == highestCard) {
					p2.score++;
					p2.sum += fstCard.value;
					sendStringToAll(p2.name + " wins Round " + round);
					System.out.println(p2.name + " wins Round " + round);
				}
				
				System.out.println(p1.score+" "+p2.score+" "+p3.score);
				scores[0] = new Card(p1.name, p1.score);
				scores[1] = new Card(p2.name, p2.score);
				scores[2] = new Card(p3.name, p3.score);
				sendScoresToAll(scores);
				
			} else {
				sendTurn(players[2].name + "'s Turn", clientIP[0], ports[0]);
				sendTurn(players[2].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn("Your Turn", clientIP[2], ports[2]);
				fstCard = receiveCard("The card was send successfully", clientIP[2], ports[2]);
				fstCard.setOrder(1);
				fstCard.makeLead();
				sendCardToAll(fstCard);
				
				sendTurn(players[0].name + "'s Turn", clientIP[1], ports[1]);
				sendTurn(players[0].name + "'s Turn", clientIP[2], ports[2]);
				sendTurn("Your Turn", clientIP[0], ports[0]);
				sndCard = receiveCard("The card was send successfully", clientIP[0], ports[0]);
				sndCard.setOrder(2);
				sendCardToAll(sndCard);
				
				sendTurn(players[1].name + "'s Turn", clientIP[0], ports[0]);
				sendTurn(players[1].name + "'s Turn", clientIP[2], ports[2]);
				sendTurn("Your Turn", clientIP[1], ports[1]);
				thrdCard =  receiveCard("The card was send successfully", clientIP[1], ports[1]);
				thrdCard.setOrder(3);
				sendCardToAll(thrdCard);

				leadingCard = fstCard;
				maxValue = fstCard.value;
				highestCard = fstCard;
				
				//check if a higher card was played
				if(sndCard.suit.equals(leadingCard.suit) && sndCard.value > maxValue) {
					maxValue=sndCard.value;
					highestCard = sndCard;
				} 
				if(thrdCard.suit.equals(leadingCard.suit) && thrdCard.value > maxValue) {
					maxValue=thrdCard.value;
					highestCard = thrdCard;
				}
				if(fstCard.suit.equals(leadingCard.suit) && fstCard.value >= maxValue){
					maxValue=fstCard.value;
					highestCard = fstCard;
				}
				//check won one the round
				if(sndCard == highestCard) {
					currentPlayer = p1;
					p1.score++;
					p1.sum += sndCard.value;
					sendStringToAll(p1.name + " wins Round " + round);
					System.out.println(p1.name + " wins Round " + round);
				}
				else if(thrdCard == highestCard) {
					currentPlayer=p2;
					p2.score++;
					p2.sum += thrdCard.value;
					sendStringToAll(p2.name + " wins Round " + round);
					System.out.println(p2.name + " wins Round " + round);
				}
				else if(fstCard == highestCard) {
					p3.score++;
					p3.sum += fstCard.value;
					sendStringToAll(p3.name + " wins Round " + round);
					System.out.println(p3.name + " wins Round " + round);
				}
				
				System.out.println(p1.score+" "+p2.score+" "+p3.score);
				scores[0] = new Card(p1.name, p1.score);
				scores[1] = new Card(p2.name, p2.score);
				scores[2] = new Card(p3.name, p3.score);
				sendScoresToAll(scores);
			}
//			
			round++;
		}//end of loop
		sendStringToAll(checkForWin(p1,p2,p3));
		//asks Player 1 if they want to end the game
		askToEnd(clientIP[0], ports[0]);
	}//end of online game
}
