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
	 * A list of cards.
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
		return index >= 0 && index < size() ? deck.get(index) : null;
	}
	
	public boolean push(Card card) {
		return deck.add(card);
	}
	
	public Card pop() {
		return empty() ? null : deck.remove(size()-1);
	}
	
	public boolean move(Pack source, Card card) {
		// Check if source and destination are distinguish
		if(this == source)
			return false;
			
		// Find the index of a card in a source pack
		int i;
		for(i = 0; source.get(i) != card; i++);

		// Push all cards from the stack
		int initSize = size();	// initial destination size
		while(i != source.size()) {
			if(!push(source.get(i++))) {
				// Abort: restore initial size
				while(size() != initSize)
					pop();
				return false;
			}
		}
		
		// Move was succesful: remove the cards from the source
		while(source.pop() != card);
		
		// Success
		return true;		
	}
	
	/**
	 * toString() override: use {@see Card.toString()}.
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for(Card card: deck)
			buf.append(card);
		
		return buf.toString();
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
		return get(size()-1);
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
