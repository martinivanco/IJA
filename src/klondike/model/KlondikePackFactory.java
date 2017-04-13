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
		KlondikePack pack = new Deck();
		
		if(str != null) {
			// Load from string
			pack.fromString(str);
		}
		else {
			// Create a standard deck of 52 different cards	
			for(Card.Color color: Card.Color.values())
				for(int value = 1; value <= 13; value++)
					pack.push(new KlondikeCard(color, value));
		}
		
		// Success
		return pack;
	}

	public Pack createSourcePack(String str) {
		KlondikePack pack = new SourcePack();
		
		if(str != null)
			pack.fromString(str);
		
		return pack;
	}

	public Pack createTargetPack(Card.Color suit, String str) {
		KlondikePack pack = new TargetPack(suit);
		
		if(str != null)
			pack.fromString(str);
		
		return pack;
	}
	
	public Pack createWorkingPack(String str) {
		// TODO
		KlondikePack pack = new Deck();
		
		if(str != null)
			pack.fromString(str);
		
		return pack;
	}
}
