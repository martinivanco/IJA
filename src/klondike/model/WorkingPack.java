package klondike.model;

import klondike.klondikeInterface.Card;

/**
 * A working deck. The next card should of different color (red/black)
 * and with the value one less, the first card is the King (any suit).
 * @author xandri03
 */
public class WorkingPack extends KlondikePack {

    @Override
    public Card pop() {
        Card card = super.pop();
        if(!empty())
            get().flip(true);
        return card;
    }

    @Override
    protected boolean check(Card card) {
        // Always push faced down cards
        if(!card.isFacedUp())
            return true;

        // King is expected for an empty pack
        if(empty())
            return card.value() == 13;
            
        // Always push ontop of a faced down card
        if(!get().isFacedUp())
            return true;

        // Regular card ontop of a regular one: check color and value
        return !card.similarColorTo(get()) && get().value() == card.value()+1;
    }
}
