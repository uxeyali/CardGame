import java.util.Random;

public class Deck {
	
	public NPSOrderedArrayList<Card> deck;
	
	public Deck() {
		deck = new NPSOrderedArrayList<Card>();
	}
	
	public NPSOrderedArrayList<Card> create() {
			for (int i=1;i<=52;i++) {
				if(i>=1 && i<=13) {
					deck.add(new Card("C", i+1));
				} else if(i>13 && i<=26) {
					deck.add(new Card("D", i-12));
				}else if(i>26 && i<=39) {
					deck.add(new Card("H", i-25));
				}else if(i>39 && i<=52) {
					deck.add(new Card("S", i-38));
			}

//				System.out.println(deck.get(i).suit +" "+deck.get(i).value);
		}
			return deck;
	}

//	Shuffles the cards in the deck
	public NPSOrderedArrayList<Card> shuffle(NPSOrderedArrayList list) {

		NPSOrderedArrayList<Card> deck = new NPSOrderedArrayList<Card>();
		
//		for each card in the deck, take a random card from the deck and copy it to a new deck. 
//		Do this randomly until you have all the cards from one deck to the next.
		for (int i=0;i<=51;i++) {
			Random random = new Random();
//			setting the bounds of the random number. Random number will only be as big as the deck size.
			int rand = random.nextInt(list.size());
//			System.out.println("Random number is: " +rand+" and size of list is: "+list.size() );
			Card card = new Card();
			card = (Card) list.get(rand);
			deck.add(card);
//			removes card from the old deck, thereby reducing the size of the array and hence the random number bounds 
			list.remove(rand);
//			System.out.println(deck.get(i).suit+" and "+deck.get(i).value);
		}
		
		return deck;
		
	}
	


}
