package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

/**
 * A target deck. All cards here are faced up and of specified suit. The
 * next card should be of the same suit and value one more. The first
 * card is the Ace.
 * @author xandri03
 */
public class TargetPack extends KlondikePack {
	/**
	 * Pack suit.
	 */
	private Card.Color color;
	
	/**
	 * Default constructor.
	 */
	public TargetPack(Card.Color color) {
		this.color = color;
	}
	
	/**
	 * push() override.
	 */
	@Override
	public boolean push(Card card) {
		// Check the suit
		if(color != card.color())
			return false;
		
		// Check the value
		if(empty()) {
			if(card.value() != 1)
				return false;
		}
		else if(get().value()+1 != card.value()) {
			return false;
		}
		
		// Card is OK
		return super.push(card);
	}
}
