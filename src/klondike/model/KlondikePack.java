package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Card pack implementation.
 * @author xandri03
 */
public class KlondikePack implements Pack {
	
	//******************************************************************
	// Attributes
	
	/**
	 * List of cards.
	 */
	private List<Card> deck;
	
	//******************************************************************
	// Constructors
	
	/**
	 * Create an empty deck.
	 */
	public KlondikePack() {
		this.deck = new ArrayList<>();
	}
	
	//******************************************************************
	// Interface implementation
	
	public int size() {
		return deck.size();
	}

	public Card get(int index) {
		if(index < 0 || index >= deck.size())
			return null;
		else
			return deck.get(index);
	}
	
	public boolean push(Card card) {
		return deck.add(card);
	}
	
	public Card pop() {
		if(deck.size() == 0)
			return null;
			
		Card card = get();
		deck.remove(card);
		return card;
	}
	
	public boolean move(Pack source, Card card) {
		Stack<Card> stack = new Stack<>();
		
		// Stack the cards from the source
		do
			stack.push(source.pop());
		while (stack.peek() != card);
		
		// Push all cards from the stack
		while(!stack.empty())
			if(!push(stack.pop()))
				return false;
		
		// Success
		return true;		
	}
	
	/**
	 * toString() override: use {@see Card.toString()}.
	 */
	@Override
	public String toString() {
		String str = "";
		
		for(Card card: deck)
			str += card;
		
		return str;
	}
	
	//******************************************************************
	// Auxiliary methods
	
	/**
	 * Read the card from the top.
	 */
	protected Card get() {
		return get(deck.size()-1);
	}
	
	/**
	 * Create a pack from its string representation.
	 */
	protected static Pack fromString(String str) {
		Pack pack = new KlondikePack(); // output pack
		
		Card card;
		while((card = KlondikeCard.fromString(str)) != null) {
			pack.push(card);
			str = str.substring(3, str.length());
		}
		
		return pack;
	}
}
