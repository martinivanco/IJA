package klondike.model;

import klondike.klondikeInterface.Card;

/**
 * Single card implementation.
 * @author xandri03
 */
public class KlondikeCard implements Card {
	
	//******************************************************************
	// Attributes
	
	/**
	 * Card suit.
	 */
	private Card.Color color;
	
	/**
	 * Card value, one of <1,13>
	 */
	private int value;
	
	/**
	 * Card orientation, true when the front is up.
	 */
	private boolean facedUp;
	
	//******************************************************************
	// Constructors
	
	/**
	 * Card constructor. The card is faced down.
	 * @param color Card color.
	 * @param value Card value, one of <1,13> (1 implicitly).
	 */
	public KlondikeCard(Card.Color color, int value) {
		this(color, value, false);
	}
	
	/**
	 * Card constructor.
	 * @param color Card color.
	 * @param value Card value, one of <1,13> (1 implicitly).
	 * @param facedUp Card orientation.
	 */
    public KlondikeCard(Card.Color color, int value, boolean facedUp) {
		this.color = color;
		this.value = value >= 1 && value <= 13 ? value : 1;
		this.facedUp = facedUp;
	}
	
	//******************************************************************
	// Interface implementation
	
	public Card.Color color() {
		return color;
	}
	
	public boolean similarColorTo(Card c) {
		return color.similarColorTo(c.color());
	}
	
	public int value() {
		return value;
	}
	
	public boolean isFacedUp() {
		return facedUp;
	}
	
	public void flip(boolean facedUp) {
		this.facedUp = facedUp;
	}
	
	/**
	 * toString() override.
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(0);
		
		// Extract suit
		buf.append(color);
		
		// Extract value
		switch(value) {
			case 1:		buf.append('A'); break;
			case 10:	buf.append('0'); break;
			case 11:	buf.append('J'); break;
			case 12:	buf.append('Q'); break;
			case 13:	buf.append('K'); break;
			default:	buf.append(value); break;
		}
		
		// Extract orientation
		buf.append(facedUp ? 'U' : 'D');

		// Success
		return buf.toString();
	}

	/**
	 * equals() override.
	 */
	public boolean equals(Object o) {
		if(o instanceof Card) {
			Card card = (Card) o;
			return color == card.color() && value == card.value() &&
				facedUp == card.isFacedUp();
		}
		return false;
	}
	
	/**
	 * hashCode() override.
	 */
	public int hashCode() {
		// Use card value
		return value;
	}
	
	//******************************************************************
	// Auxiliary methods 
	
	/**
	 * Create a card from its string representation.
	 */
	protected static Card fromString(String str) {
		// Check string length
		if(str.length() < 3)
			return null;

		// Card attributes
		Card.Color color;
		int value;
		boolean facedUp;
		
		// Extract color
		switch(str.charAt(0)) {
			case 'C':	color = Card.Color.CLUBS; break;
			case 'D':	color = Card.Color.DIAMONDS; break;
			case 'H':	color = Card.Color.HEARTS; break;
			default:	color = Card.Color.SPADES; break;
		}
		
		// Extract value
		switch(str.charAt(1)) {
			case 'A':	value = 1; break;
			case '0':	value = 10; break;
			case 'J':	value = 11; break;
			case 'Q':	value = 12; break;
			case 'K':	value = 13; break;
			default:	value = Character.getNumericValue(str.charAt(1));
		}
		
		// Extract orientation
		facedUp = str.charAt(2) == 'U';
		
		// Create and return a new card
		return new KlondikeCard(color, value, facedUp);
	}
}
