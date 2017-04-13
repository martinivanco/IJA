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
	 * Card orientation, true if the front is up.
	 */
	private boolean facedUp;
	
	//******************************************************************
	// Constructors
	
	/**
	 * Card constructor.
	 * @param color Card color.
	 * @param value Card value, one of <1,13>.
	 */
	public KlondikeCard(Card.Color color, int value) {
		this(color, value, false);
	}
	
	/**
	 * Card constructor.
	 * @param color Card color.
	 * @param value Card value, one of <1,13>.
	 * @param facedUp Card orientation.
	 */
    public KlondikeCard(Card.Color color, int value, boolean facedUp) {
		this.color = color;
		if(value < 1 || value > 13)
			value = 1;
		this.value = value;
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
	
	public void flipUp() {
		facedUp = true;
	}
	
	public void flipDown() {
		facedUp = false;
	}
	
	/**
	 * toString() override.
	 */
	@Override
	public String toString() {
		String str;
		
		// Extract suit
		str = color.toString();
		
		// Extract value
		switch(value) {
			case 1:		str += "A"; break;
			case 10:	str += "0"; break;
			case 11:	str += "J"; break;
			case 12:	str += "Q"; break;
			case 13:	str += "K"; break;
			default:	str += "" + value; break;
		}
		
		// Extract orientation
		if(facedUp)
			str += 'U';
		else
			str += 'D';
		
		// Success
		return str;
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
		// Use the suit
		return color.hashCode();
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
		if(str.charAt(2) == 'U')
			facedUp = true;
		else
			facedUp = false;
		
		// Create and return a new card
		return new KlondikeCard(color, value, facedUp);
	}
}
