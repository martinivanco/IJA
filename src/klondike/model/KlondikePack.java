package klondike.model;

import klondike.klondikeInterface.Card;
import klondike.klondikeInterface.Pack;

import java.util.List;
import java.util.ArrayList;

/**
 * Card pack implementation.
 * @author xandri03
 */
public class KlondikePack implements Pack {

    //******************************************************************
    // Attributes

    /**
     * A list of cards.
     */
    private final List<Card> deck;

    //******************************************************************
    // Constructors

    /**
     * Create an empty deck.
     */
    public KlondikePack() {
        this.deck = new ArrayList<>();
    }

    //******************************************************************
    // Interface implementation
   
    @Override
    public int size() {
        return deck.size();
    }
    
    @Override
    public boolean empty() {
        return deck.isEmpty();
    }
    
    @Override
    public Card get() {
        return get(size()-1);
    }

    @Override
    public Card get(int index) {
        return index >= 0 && index < size() ? deck.get(index) : null;
    }

    @Override
    public int indexOf(Card card) {
        // Scan through the pack
        for(int i = 0; i < size(); i++) {
            if(get(i).equals(card))
                return i;
        }
        
        // Search failed
        return -1;
    }
    
    @Override
    public void push(Card card) {
        if(check(card))
            deck.add(card);
    }

    @Override
    public Card pop() {
        return empty() ? null : deck.remove(size()-1);
    }
    
    @Override
    public boolean check(Pack source, Card card) {
        // Check if source and destination are distinguish
        if(this == source)
            return false;
        
        // Check if the source card is indeed in the source pack
        int index = source.indexOf(card);
        if(index == -1)
            return false;
        
        // Save target pack
        String str = toString();
        
        // Try to push all cards
        for(int i = index; i < source.size(); i++) {
            // Check the card
            if(!check(source.get(i))) {
                // Abort
                fromString(str);
                return false;
            }
            push(source.get(i));
        }
        
        // Reload the pack
        fromString(str);
        
        // Move is allowed
        return true;
    }
    
    @Override
    public boolean move(Pack source, Card card) {
        // Check move validity
        if(!check(source, card))
            return false;
        
        // Copy from source
        int index = source.indexOf(card);
        for(int i = index; i < source.size(); i++)
            push(source.get(i));
        
        // Remove cards from the source
        while(source.size() > index)
            source.pop();
        
        // Success
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for(Card card: deck)
            buf.append(card);
        return buf.toString();
    }

    //******************************************************************
    // Auxiliary methods

    /**
     * Check if the card may be pushed on top.
     * @param card Incoming card.
     * @return True if the push is possible.
     */
    protected boolean check(Card card) {
        // Any card may be pushed on top
        return true;
    }

    /**
     * Rebuild a pack from its string representation.
     * @param str A string to load from.
     */
    protected void fromString(String str) {
        deck.clear();

        Card card;
        while((card = KlondikeCard.fromString(str)) != null) {
            deck.add(card);
            str = str.substring(3, str.length());
        }
    }
}
