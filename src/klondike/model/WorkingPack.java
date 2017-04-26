package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

/**
 * A working deck. The next card should of different color (red/black)
 * and with the value one less, the first card is the King (any suit).
 * @author xandri03
 */
public class WorkingPack extends KlondikePack {
	
	/**
	 * pop() override: if the card on top is faced down, flip it.
	 */
	@Override
	public Card pop() {
		Card card = super.pop();
		if(!empty())
			get().flip(true);
		return card;
	}
	
	/**
	 * push() override.
	 */
	@Override
	public boolean push(Card card) {
		// Do checking only if card is turned face up
		if (card.isFacedUp()) {
			// Check the color if there is a card faced up underneath
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
		}
		
		// The card is OK
		return super.push(card);
	}

	/**
	 * fromString() override.
	 */
	@Override
	protected void fromString(String str) {
		// Clear deck not needed

		Card card;
		while((card = KlondikeCard.fromString(str)) != null) {
			super.push(card);
			str = str.substring(3, str.length());
		}
	}
}
