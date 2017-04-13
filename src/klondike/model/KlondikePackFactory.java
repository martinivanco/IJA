package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;
import klondike.klondikeInterface.PackFactory;

/**
 * Concrete pack factory.
 * @author xandri03
 */
public class KlondikePackFactory extends PackFactory {
	
	//******************************************************************
	// Abstract methods
	
	public Pack createDeck(String str) {
		// TODO
		
		if(str != null)
			return KlondikePack.fromString(str);
		
		// Create a standard deck of 52 different cards	
		Pack pack = new KlondikePack();
		for(Card.Color color: Card.Color.values())
			for(int value = 1; value <= 13; value++)
				pack.push(new KlondikeCard(color, value));

		// Success
		return pack;
	}

	public Pack createSourcePack(String str) {	
		//TODO
		return createDeck(str);
	}

	public Pack createTargetPack(Card.Color suit) {
		//TODO
		return createDeck(null);
	}

	public Pack createTargetPack(String str) {
		//TODO
		return createDeck(str);
	}
	
	public Pack createWorkingPack(String str) {
		//TODO
		return createDeck(str);
	}
}
