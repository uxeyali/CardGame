public class CardGame extends Thread{

	
	public void distribute(Player p1, Player p2, Player p3, NPSOrderedArrayList<Card> deck) {
		
//		Distributing the deck into hands for each player
//		Distributes 51 cards into 3 hands
		p1.hand.clear();
		p2.hand.clear();
		p3.hand.clear();
		for(int i =0; i<=50;i++) {
			
//			Each player gets one card each.
			if(i%3 == 0) {
				p1.hand.add(deck.get(i));
			} else if (i%3 == 1){
				p2.hand.add(deck.get(i));
			}else if (i%3==2){
				p3.hand.add(deck.get(i));
			}
		}
	}
	
	//changed checkForWin to take Player parameters 9/28/18
	public String checkForWin(Player p1, Player p2, Player p3) {
		String message = "";
		if(p1.score > p2.score) {
			if(p1.score > p3.score) {
				message = p1.name + " is the winner!";
			}
			else if(p1.score == p3.score) {
				message += p3.name + " and " +p1.name +" are tied!";
				if(p3.sum > p1.sum) {
					message += "\nHowever, " + p3.name + "'s winning cards had the highest sum of values.\n" + p3.name + " Wins!";
				}
				else if(p3.sum == p1.sum) {
					message += "\nBoth " + p3.name + " and " + p1.name + "'s winning cards have same value, so they both tie.";
				}
				else {
					message += "\nHowever, " + p1.name + "'s winning cards had the highest sum of values.\n" + p1.name + " Wins!";
				}
			}
			else if(p3.score > p1.score) {
				message = p3.name + " is the winner!";
			}
		}
		else if(p2.score > p1.score) {
			if(p2.score > p3.score) {
				message = p2.name + " is the winner!";
			}
			else if(p2.score == p3.score) {
				message += p2.name + " and " +p3.name +" are tied!";
				if(p2.sum > p3.sum) {
					message += "\nHowever, " + p2.name + "'s winning cards had the highest sum of values.\n" + p2.name + " Wins!";
				}
				else if(p2.sum == p3.sum) {
					message += "\nBoth " + p2.name + " and " + p3.name + "'s winning cards have same value, so they both tie.";
				}
				else {
					message += "\nHowever, " + p3.name + "'s winning cards had the highest sum of values.\n" + p3.name + " Wins!";
				}
			}
			else if(p3.score > p2.score) {
				message = p3.name + " is the winner!";
			}
		}
		else if(p1.score == p2.score) {
			message += p1.name + " and " +p2.name +" are tied!";
			if(p1.sum > p2.sum) {
				message += "\nHowever, " + p1.name + "'s winning cards had the highest sum of values.\n" + p1.name + " Wins!";
			}
			else if(p1.sum == p2.sum) {
				message += "\nBoth " + p1.name + " and " + p2.name + "'s winning cards have same value, so they both tie.";
			}
			else {
				message += "\nHowever, " + p2.name + "'s winning cards had the highest sum of values.\n" + p2.name + " Wins!";
			}
		}
		
		return message;
	}
	
	public void startLocalGame() {
		// TODO Auto-generated method stub
		GameState gameState = new GameState();
		int round = 1;
		GameGui G1 = new GameGui();
		GameGui G2 = new GameGui();
		GameGui G3 = new GameGui();
//==========Initialize players====================================
		Player p1 = new Player("Player 1", G1);
		Player p2 = new Player("Player 2", G2);
		Player p3 = new Player("Player 3", G3);
		
		p1.gameGui.displayMessage("The Game Starts");
		p2.gameGui.displayMessage("The Game Starts");
		p3.gameGui.displayMessage("The Game Starts");
		
		Player currentPlayer = p1;
		
//==========Create a new deck=====================================
		Deck maindeck = new Deck();
		NPSOrderedArrayList<Card> deck = maindeck.create();
//		Shuffle the deck
		deck = maindeck.shuffle(deck);
		
//		Distribute the cards amongst the players hands
		distribute(p1,p2,p3, deck);
		
		p1.gameGui.displayMessage("You received your cards");
		p1.gameGui.CreateButtons(p1.hand);
		p2.gameGui.displayMessage("You received your cards");
		p2.gameGui.CreateButtons(p2.hand);
		p3.gameGui.displayMessage("You received your cards");
		p3.gameGui.CreateButtons(p3.hand);
		
		
		Card leadingCard = new Card();
		Card fstCard = new Card();
		Card sndCard = new Card();
		Card thrdCard = new Card();
		int maxValue;
		
//		We are testing so round is at 1 and not 17
//		while(round<=17) {
			
			System.out.println("Round " + round + ": ");
			p1.gameGui.displayMessage("Round " + round + ":");
			p2.gameGui.displayMessage("Round " + round + ":");
			p3.gameGui.displayMessage("Round " + round + ":");
//			if(currentPlayer == p1) {
				
				p1.gameGui.setTurn("Your Turn");
				p2.gameGui.setTurn("It is " + p1.name + "'s turn");
				p3.gameGui.setTurn("It is " + p1.name + "'s turn");
//				fstCard = p1.play(0);
				System.out.println("Got it " + fstCard.suit);
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
//			}
//			System.out.println(p1.sum+" "+p2.sum+" "+p3.sum);
//			
//			round++;
//		//}//end of loop
//		System.out.println(checkForWin(p1,p2,p3));
}
	
}
