package klondike.model;

import klondike.klondikeInterface.Card;

/**
 * A source deck. All cards here are faced up. Move is not allowed here.
 * @author xandri03
 */
public class SourcePack extends KlondikePack {
    
    @Override
    public void push(Card card) {
        card.flip(true);
        super.push(card);
    }
}
