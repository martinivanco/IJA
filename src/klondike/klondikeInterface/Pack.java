package klondike.klondikeInterface;

/**
 * Card pack interface.
 * 
 * String representation is a sequence of {@see Card.toString()}.
 * 
 * @author xandri03
 * @author xivanc03
 */
 public interface Pack {
    /**
     * Size pack.
     */
    public int size();

    /**
     * Check if the pack is empty.
     */
    public boolean empty();
    
    /**
     * Read a card from a pack top
     * @return The top card or null if the pack is empty.
     */
    public Card get();
    
    /**
     * Read a card from a specific index.
     * @param index 0 for the deepest card.
     * @return Specified card or null if {@code index} is out of bounds.
     */
    public Card get(int index);

    /**
     * Find the card in this pack.
     * @param card
     * @return An index of a {@code card} or -1 if one was not found.
     */
    public int indexOf(Card card);
    
    /**
     * Push a card onto top.
     * @param card A card to push.
     */
    public void push(Card card);

    /**
     * Pop one card from a pack top.
     * @return Popped card or null if the pack is empty.
     */
    public Card pop();

    /**
     * Check if the move may be performed.
     * @param source A source pack.
     * @param card A source card.
     * @return true if the move is allowed.
     */
    public boolean check(Pack source, Card card);
    
    /**
     * Move card sequence.
     * @param source A source pack.
     * @param card A source card.
     */
    public void move(Pack source, Card card);
}

