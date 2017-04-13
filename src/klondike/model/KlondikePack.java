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
		if(empty())
			return null;
			
		Card card = get();
		deck.remove(card);
		return card;
	}
	
	public boolean move(Pack source, Card card) {
		// Check if source and destination are distinguish
		if(this == source)
			return false;
			
		// Get a copy of a card sequence
		Stack<Card> stack = new Stack<>();
		int i = source.size()-1;
		do {
			stack.push(source.get(i--));
		} while (stack.peek() != card);
		
		// Push all cards from the stack
		while(!stack.empty()) {
			if(!push(stack.peek())) {
				// Abort
				while(stack.peek() != card) {
					stack.push(pop());
				}
				return false;
			}
			stack.pop();
		}
		
		// Move was succesful: remove the cards from source
		Card c;
		while((c = source.pop()) != card);
		
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
	 * Check if the pack is empty.
	 * @return true if the pack is empty.
	 */
	protected boolean empty() {
		return deck.size() == 0;
	}
	
	/**
	 * Read the card from the top.
	 */
	protected Card get() {
		return get(deck.size()-1);
	}
	
	/**
	 * Rebuild a pack from its string representation.
	 */
	protected void fromString(String str) {
		deck.clear();
		
		Card card;
		while((card = KlondikeCard.fromString(str)) != null) {
			push(card);
			str = str.substring(3, str.length());
		}
	}
}
