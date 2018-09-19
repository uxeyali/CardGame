import java.util.Random;

public class Deck {
	
	public NPSOrderedArrayList<Card> deck;
	
	public Deck() {
		deck = new NPSOrderedArrayList<Card>();
	}
	
	public NPSOrderedArrayList<Card> create() {
			for (int i=0;i<=51;i++) {
				if(i>=0 && i<=12) {
					deck.add(new Card("c", i+1));
				} else if(i>12 && i<=25) {
					deck.add(new Card("d", i-12));
				}else if(i>25 && i<=38) {
					deck.add(new Card("h", i-25));
				}else if(i>38 && i<=51) {
					deck.add(new Card("s", i-38));
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
