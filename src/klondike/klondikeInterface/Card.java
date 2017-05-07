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
        SPADES ('S'), DIAMONDS ('D'), CLUBS ('C'), HEARTS ('H');

        /**
         * String representation of a color.
         */
        private final char symbol;

        /**
         * Default constructor.
         */
        Color(char symbol) {
            this.symbol = symbol;
        }

        /**
         * Check suit similarity.
         * @param color The color to compare to.
         * @return true if suits are both red or both black.
         */
        public boolean similarColorTo(Card.Color color) {
            if(color == CLUBS || color == SPADES)
                return this == CLUBS || this == SPADES;
            else
                return this == DIAMONDS || this == HEARTS;
        }

        /**
         * toString() override: return text symbol instead of a value.
         * @return String representation of a suit.
         */
        @Override
        public String toString() {
            return Character.toString(symbol);
        }
    }

    /**
     * Card color.
     * @return A card suit.
     */
    public Card.Color color();

    /**
     * Test the similarity of suits.
     * @param card The card to compare the suit to.
     * @return true if Cards are both red or both black.
     */
    public boolean similarColorTo(Card card);

    /**
     * Card value.
     * @return One of <1,13> according to card rules.
     */
    public int value();

    /**
     * Test if the card is faced up.
     * @return true if the card is faced up.
     */
    public boolean isFacedUp();

    /**
     * Change the card orientation.
     * @param isfacedUp true for the card to be faced up.
     */
    public void flip(boolean isfacedUp);
}
