package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

/**
 * A working deck. All cards here are faced up and of specified suit.
 * The next card should of different color (red/black) and with the
 * value one less, the first card is the King (any suit).
 * @author xandri03
 */
public class WorkingPack extends KlondikePack {
	
	/**
	 * pop() override: if the card on top is faced down, flip it.
	 */
	@Override
	public Card pop() {
		Card card = super.pop();
		get().flipUp();
		return card;
	}
	
	/**
	 * push() override.
	 */
	@Override
	public boolean push(Card card) {
		// Check the color
		if(card.similarColorTo(get()))
			return false;
		
		// Check the value
		if(empty()) {
			if(card.value() != 13)
				return false;
		}
		else if(get().value() != card.value()+1) {
			return false;
		}
		
		// The card is OK
		return super.push(card);
	}
}
