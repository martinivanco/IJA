package klondike.klondikeInterface;

/**
 * Card pack interface.
 * 
 * For a string representation, see {@see Card.toString()}.
 * 
 * @author Roman Andriushchenko (xandri03)
 * @author Martin Ivanco (xivanc03)
 */
 public interface Pack {
	/**
	 * Actual number of cards in a deck.
	 */
	public int size();

	/**
	 * Read a card from a specific index.
	 * @return Card on {@code index} or null if such does not exist.
	 */
	public Card read(int index);

	/**
	 * Get all cards from {@code source} starting from {@code card}.
	 */
	public void move(Pack source, Card card);

	/**
	 * Rebuild the pack using {@code str} as its text representation.
	 */
	public void fromString(String str);
}

