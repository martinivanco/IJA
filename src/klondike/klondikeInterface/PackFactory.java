package klondike.klondikeInterface;

/**
 * Pack abstract factory interface.
 * 
 * @author Roman Andriushchenko (xandri03)
 * @author Martin Ivanco (xivanc03)
 */
public interface PackFactory {
	/**
	 * A deck of cards. All cards here a faced down.
	 */
	public Pack createDeck();

	/**
	 * A source pack of cards. All cards here are faced up.
	 */
	public Pack createSourcePack();

	/**
	 * A target pack of cards. All cards here are faced up and of
	 * specific suit. Only the card with the value one more may be
	 * pushed on top, the starting card is the Ace.
	 * @param suit A color of all cards in the pack.
	 */
	public Pack createTargetPack(Card.Color suit);

	/**
	 * A working pack of cards. Only the card with the value one less
	 * and of different color (red/black) may be pushed on top, the
	 * starting card is the King (any suit).
	 */
	public Pack createWorkingPack();
}
