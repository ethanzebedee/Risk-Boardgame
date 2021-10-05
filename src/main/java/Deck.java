import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * Author: Ethan Hammond
 * 
 * Creates the deck of risk cards
 * 
 **/
public class Deck {
	
	private String[] types;
	
	private ArrayList<Card> deck;

	public Deck () {		
		
		//type
		types = new String[]{ "Infantry", "Cavalry", "Artillery" };
		
		deck = new ArrayList<Card>();
		
		for (int i = 0; i < GameData.NUM_COUNTRIES; i++) {
		// Add new cards to deck
			deck.add(new Card(types[i / 14], GameData.COUNTRY_NAMES[i], false));
		
		}

		shuffle();

	}

	//draws a card
	public Card draw() {
		
		return deck.remove(0);

	}

	//Adds card
	public void add(Card card) {

		deck.add(card);

	}

	public Card get(int i){

		return deck.get(i);
	}

	//Shuffles deck
	public void shuffle() {
	
		Collections.shuffle(deck);
	}

	//We live in a society
	//Adds Joker cards to the deck
	public void addJoker(){

		for(int j = 0; j < 1; j++){

			deck.add(new Card("Joker", "Joker", true));

		}

	}

}
