package klondike.model;

import klondike.klondikeInterface.Card;

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
    private final Card.Color color;

    /**
     * Default constructor.
     * @param color A suit of all cards in a pack.
     */
    public TargetPack(Card.Color color) {
        this.color = color;
    }

    @Override
    protected boolean check(Card card) {
        // Check the suit and orientation
        if(!card.isFacedUp() || card.color() != color)
            return false;

        // Ace is allowed here for an empty pack
        if(empty())  
            return card.value() == 1;
        
        // Some card goes on top of another one: check the value
        return get().value()+1 == card.value();
    }
}
