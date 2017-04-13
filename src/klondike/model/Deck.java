package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

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
		card.flipDown();
		return super.push(card);
	}
}
