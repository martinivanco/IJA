package klondike.model;

import klondike.klondikeInterface.Card;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A card deck. All cards here are faced down.
 * @author xandri03
 */
public class Deck extends KlondikePack {

    @Override
    public void push(Card card) {
        card.flip(false);
        super.push(card);
    }

    /**
     * Shuffle the deck.
     */
    public void shuffle() {
        // Pop all cards
        List<Card> list = new ArrayList<>();
        while(!empty())
            list.add(pop());

        // Shuffle
        Collections.shuffle(list);

        // Push shuffled deck back
        for(Card card: list)
            push(card);
    }
}
