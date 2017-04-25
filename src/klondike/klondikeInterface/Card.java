package klondike.klondikeInterface;

/**
 * Single card interface.
 * 
 * String representation of such card is of the form 'SVF' where
 * - S is the suit (see {@see Card.Color.toString()});
 * - V is the value with '0','J','Q','K','A' representing 10, Jack,
 * Queen, King and Ace respectively;
 * - F is 'U' when the card is faced up, 'D' otherwise.
 *
 * Note: provides equal() and hashCode() override.
 * 
 * @author xandri03
 * @author xivanc03
 */
public interface Card {
	/**
	 * Card color enumerator.
	 */
	public static enum Color {
		CLUBS ('C'), DIAMONDS ('D'), HEARTS ('H'), SPADES ('S');
	
		/**
		 * String representation of a color.
		 */
		private char symbol;
		
		/**
		 * Default constructor.
		 */
		Color(char symbol) {
			this.symbol = symbol;
		}
		
		/**
		 * Check if {@code c} is of the same color (black or red).
		 * @return true if suits are both red or both black.
		 */
		public boolean similarColorTo(Card.Color c) {
			if(c == CLUBS || c == SPADES)
				return this == CLUBS || this == SPADES;
			else
				return this == DIAMONDS || this == HEARTS;
		}
		
		/**
		 * toString() override: return text symbol instead of a value.
		 */
		@Override
		public String toString() {
			return Character.toString(symbol);
		}
	}

	/**
	 * Card color.
	 */
	public Card.Color color();
	
	/**
	 * Test if {@code c} has the same color (black or red).
	 * @return true if Cards are both red or both black.
	 */
	public boolean similarColorTo(Card c);
	
	/**
	 * Card value.
	 */
	public int value();
	
	/**
	 * Test if the card is faced up.
	 * @return true if the card is faced up.
	 */
	public boolean isFacedUp();
   
	/**
	 * Change the card orientation.
	 * @param facedUp true for the card to be faced up.
	 */
    public void flip(boolean facedUp);
}
