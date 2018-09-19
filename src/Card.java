public class Card {

	String suit;
	int value;
	
	//constructor
		public Card(String suit, int value) {
			this.suit = suit;
			this.value = value;
		}
		
		public Card() {
			this.suit = null;
			this.value = 0;
		}
}
