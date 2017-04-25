package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

/**
 * A source deck. All cards here are faced up. Move is not allowed here.
 * @author xandri03
 */
public class SourcePack extends KlondikePack {
	
	/**
	 * push() override.
	 */
	@Override
	public boolean push(Card card) {
		card.flip(true);
		return super.push(card);
	}
	
	/**
	 * move() override.
	 */
	@Override
	public boolean move(Pack source, Card card) {
		return false;
	}
}
