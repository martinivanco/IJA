package klondike.klondikeInterface;

/**
 * Abstract pack factory.
 * @author xandri03
 * @author xivanc03
 */
public abstract class PackFactory {
	/**
	 * Create a standard deck.
	 * @param str A string to load pack from. If null, a standard
	 * shuffled deck of 52 different cards is created.
         * @return A deck with all the cards being faced down.
	 */
	public abstract Pack createDeck(String str);

	/**
	 * Create a source pack of cards.
	 * @param str A string to load pack from. If null, an empty pack
	 * is created.
         * @return A source pack with all the cards being faced up.
	 */
	public abstract Pack createSourcePack(String str);

	/**
	 * Create an empty target pack of cards. All cards here are faced up
	 * and of specific suit. Only the card with the value one more may
	 * be pushed on top, the starting card is the Ace.
	 * @param suit A color of all cards in the pack.
	 * @param str A string to load pack from. If null, an empty pack
	 * is created.
         * @return A target pack.
	 */
	public abstract Pack createTargetPack(Card.Color suit, String str);
	
	/**
	 * A working pack of cards. Only the card with the value one less
	 * and of different color (red/black) may be pushed on top, the
	 * starting card is the King (any suit).
	 * @param str A string to load pack from. If null, an empty pack
	 * is created.
         * @return A working pack.
	 */
	public abstract Pack createWorkingPack(String str);
}
