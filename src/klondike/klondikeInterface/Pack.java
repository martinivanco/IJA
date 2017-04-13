package klondike.klondikeInterface;

/**
 * Card pack interface.
 * 
 * For a string representation, see {@see Card.toString()}.
 * 
 * @author xandri03
 * @author xivanc03
 */
 public interface Pack {
	/**
	 * Actual number of cards in a pack.
	 */
	public int size();

	/**
	 * Read a card from a specific index.
	 * @return Specified card or null if {@code index} is out of bounds.
	 */
	public Card get(int index);

	/**
	 * Push a {@code card} onto top.
	 * @return true if the push was succesful.
	 */
	public boolean push(Card card);
	
	/**
	 * Pop one card from a pack top.
	 * @return Popped card or null if the pack is empty.
	 */
	public Card pop();
	
	/**
	 * Get all cards from {@code source} starting from {@code card}.
	 * @return true if the move was succesful.
	 */
	public boolean move(Pack source, Card card);
}

