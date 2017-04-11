/* File: KlondikeInterface/Card.java        *
 * Project: IJA                             *
 * Authors: Roman Andriushchenko (xandri03) *
 *          Martin Ivanco (xivanc03)        */

package KlondikeInterface;

public interface Card {

    enum Color {
        SPADES ("S"),
        DIAMONDS ("D"),
        HEARTS ("H"),
        CLUBS ("C");

        private String value;

        Color(String colorLetter) {
            value = colorLetter;
        }

        public boolean similarColorTo(Card.Color c) {
            if ((this.equals(Color.SPADES)) || (this.equals(Color.CLUBS))) {
               return ((c.equals(Color.SPADES)) || (c.equals(Color.CLUBS)));
            }
            else {
                return ((c.equals(Color.DIAMONDS)) || (c.equals(Color.HEARTS)));
            }
        }

        @Override
        public String toString() {
            return value;
        }
    }

    int value();

    Card.Color color();

    boolean isTurnedFaceUp();

    boolean faceUp();

    boolean faceDown();

    boolean similarColorTo(Card c);

    String toString();

    boolean equals(Object obj);

    public static Card fromString(String str);
}
