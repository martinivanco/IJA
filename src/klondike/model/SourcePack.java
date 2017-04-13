package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

/**
 * A source deck. All cards here are faced up.
 * @author xandri03
 */
public class SourcePack extends KlondikePack {
	
	/**
	 * push() override.
	 */
	@Override
	public boolean push(Card card) {
		card.flipUp();
		return super.push(card);
	}
}
