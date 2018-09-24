public class Player {
	NPSOrderedArrayList<Card> hand;
	int score;
	
	//constructor
		public Player() {
			this.hand = new NPSOrderedArrayList<Card>();
			this.score = 0;
		}
		
	public Card play() {
		Card cardPlayed = this.hand.get(0);
		hand.remove(0);
		return cardPlayed;
	}
		
}
