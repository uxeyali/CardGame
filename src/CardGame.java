public class CardGame {

	
	public static void distribute(Player p1, Player p2, Player p3, NPSOrderedArrayList<Card> deck) {
		
//		Distributing the deck into hands for each player
//		Distributes 51 cards into 3 hands
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
	
	public static void checkForWin(int p1score, int p2score, int p3score) {
		if(p1score >= p2score) {
			if(p1score > p3score) {
				System.out.println("Player 1 is the winner!");
			} else if(p1score < p3score) {
				System.out.println("Player 3 is the winner!");
			} else if(p1score == p2score) {
				System.out.println("Player 1 and Player 2 tied!");
			} else if(p2score == p3score) {
				System.out.println("Player 2 and Player 3 tied!");
			} else if(p3score == p1score) {
				System.out.println("Player 3 and Player 1 tied!");
			}
		} else if(p1score < p2score) {
			if(p2score > p3score) {
				System.out.println("Player 2 is the winner!");
			} else if(p2score < p3score) {
				System.out.println("Player 3 is the winner!");
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameState gameState = new GameState();
		int round = 1;

//==========Initialize players====================================
		Player p1 = new Player();
		Player p2 = new Player();
		Player p3 = new Player();
		Player currentPlayer = p1;
		
//==========Create a new deck=====================================
		Deck maindeck = new Deck();
		NPSOrderedArrayList<Card> deck = maindeck.create();
//		Shuffle the deck
		deck = maindeck.shuffle(deck);
		
//		Distribute the cards amongst the players hands
		distribute(p1,p2,p3, deck);
		Card leadingCard = new Card();
		Card fstCard = new Card();
		Card sndCard = new Card();
		Card thrdCard = new Card();
		int maxValue;
		
//		We are testing so round is at 1 and not 17
		while(round<=17) {
			
			if(currentPlayer == p1) {

				fstCard = p1.play();
				sndCard = p2.play();
				thrdCard = p3.play();
				

//				These will be 
				leadingCard = fstCard;
				maxValue = fstCard.value;
				System.out.println("CurrentPlayer is p1");
				System.out.println("Leading "+leadingCard.suit+leadingCard.value );
				System.out.println("p1's Card "+fstCard.suit+fstCard.value );
				System.out.println("p2's Card "+sndCard.suit+sndCard.value );
				System.out.println("p3's Card "+thrdCard.suit+thrdCard.value );
				
				if(sndCard.suit == leadingCard.suit && sndCard.value > maxValue) {
					maxValue=sndCard.value;
					currentPlayer = p2;
					p2.score++;
				} else if(thrdCard.suit == leadingCard.suit && thrdCard.value > maxValue) {
					maxValue=thrdCard.value;
					currentPlayer = p3;
					p3.score++;
				} else {
					p1.score++;
				}

				System.out.println(p1.score+" "+p2.score+" "+p3.score);
				
			} else if(currentPlayer == p2){
				

				fstCard = p2.play();
				sndCard = p3.play();
				thrdCard = p1.play();

//				These will be 
				leadingCard = fstCard;
				maxValue = fstCard.value;
				System.out.println("CurrentPlayer is p2");
				System.out.println("Leading "+ leadingCard.suit + leadingCard.value );
				System.out.println("p2's Card "+ fstCard.suit + fstCard.value );
				System.out.println("p3's Card "+ sndCard.suit + sndCard.value );
				System.out.println("p1's Card "+ thrdCard.suit + thrdCard.value );
				
				if(sndCard.suit == leadingCard.suit && sndCard.value > maxValue) {
					maxValue=sndCard.value;
					currentPlayer = p3;
					p3.score++;
				} else if(thrdCard.suit == leadingCard.suit && thrdCard.value > maxValue) {
					System.out.println("Max Value is: " + maxValue +"  "+ thrdCard.value);
					maxValue=thrdCard.value;
					currentPlayer = p1;
					p1.score++;
				} else {
					p2.score++;
				}

				System.out.println(p1.score +" " +p2.score +" "+ p3.score);
				
			} else {

				fstCard = p3.play();
				sndCard = p1.play();
				thrdCard = p2.play();

				leadingCard = fstCard;
				maxValue = fstCard.value;

				System.out.println("CurrentPlayer is p3");
				System.out.println("Leading " + leadingCard.suit + leadingCard.value );
				System.out.println("p3's Card " + fstCard.suit + fstCard.value );
				System.out.println("p1's Card " + sndCard.suit + sndCard.value );
				System.out.println("p2's Card " + thrdCard.suit + thrdCard.value );
				
				if(sndCard.suit == leadingCard.suit && sndCard.value > maxValue) {
					maxValue=sndCard.value;
					currentPlayer = p1;
					p1.score++;
				} else if(thrdCard.suit == leadingCard.suit && thrdCard.value > maxValue) {
					maxValue=thrdCard.value;
					currentPlayer=p2;
					p2.score++;
				} else {
					p3.score++;
				}

				System.out.println(p1.score+" "+p2.score+" "+p3.score);
			}
			
			checkForWin(p1.score,p2.score,p3.score);
			
			round++;
//			currentPlayer = checkForWinner();
		}
}
	
}
