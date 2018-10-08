import java.io.Serializable;

public class Card implements Serializable{

	String suit;
	int value;
	int order;
	boolean isLead = false;
	
	//constructor
		public Card(String suit, int value) {
			this.suit = suit;
			this.value = value;
		}
		
		public Card() {
			this.suit = null;
			this.value = 0;
		}
		
		public void setOrder(int order) {
			this.order = order;
		}
		
		public void makeLead() {
			isLead = true;
		}
}
