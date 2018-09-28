public class Player {
	NPSOrderedArrayList<Card> hand;
	String name;
	int score;
	int sum;
	
	//constructor
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
		
}
