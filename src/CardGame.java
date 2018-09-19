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
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameState gameState = new GameState();
		int round = 1;
		
		while(round<=17) {
			Card leadingCard = new Card();
			String leadingSuit = leadingCard.suit;
			
			
//==========Create a new deck=====================================
			Deck maindeck = new Deck();
			NPSOrderedArrayList<Card> deck = maindeck.create();
//			Shuffle the deck
			deck = maindeck.shuffle(deck);
	
//==========Initialize players====================================
			Player p1 = new Player();
			Player p2 = new Player();
			Player p3 = new Player();
		
//			Distribute the cards amongst the players hands
			distribute(p1,p2,p3, deck);
			
//			Begin the actual playing of the game
//			If a players played card is greater than the leading suit card value
			
			round++;
		}
}
	
}
