package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A card deck. All cards here are faced down.
 * @author xandri03
 */
public class Deck extends KlondikePack {
	
	/**
	 * push() override.
	 */
	@Override
	public boolean push(Card card) {
		card.flip(false);
		return super.push(card);
	}
	
	/**
	 * Shuffle the deck.
	 */
	public void shuffle() {
		// Pop all cards
		List<Card> list = new ArrayList<>();
		while(!empty())
			list.add(pop());
		
		// Shuffle
		Collections.shuffle(list);
		
		// Push shuffled deck back
		for(Card card: list)
			push(card);
	}
}
