package klondike.board;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

/**
 * A valid move description.
 * 
 * @author xandri03
 */
public class Hint {
    public Pack source;
    public Card card;
    public Pack target;
    
    public Hint(Pack source, Card card, Pack target) {
        this.source = source;
        this.card = card;
        this.target = target;
    }
}
