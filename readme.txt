IJA project: Klondike - a patience game.

Authors: Martin Ivančo (xivanc03), Roman Andriushchenko (xandri03).

Basic description and controls
This application implements basic Klondike rules, see https://en.wikipedia.org/wiki/Klondike_(solitaire). Application control is perfomed via a mouse. By clicking a card with a left button it becomes active, which is indicated by a yellow frame. Active card may be moved on top of another pile or foundation while checking the card value and/or suit. If an active card is underneath other cards, the whole sequence becomes active and may be moved to another pile (not foundation). By clicking the right button, one can disselect the active card. Clicking the left button upon a deck of cards results in dealing the next card to the pile of source cards or, in case when all cards were dealt, flipping all the cards from the source pile back to the deck.
By clicking a board name in the upper left, one can choose to start a new game, save the current game, load existing one or close the board. It is also possible to get a hint (an allowed move).
Furthermore, it is possible (via the same board names) to open/load a new game in a next board; the maximum of 4 boards is allowed. One can close these boards one by one, closing the last board will result in finishing the application.

Compilation procedure:
1) Go to lib/ and run '$bash get-libs.sh' to download the textures.
2) From the root folder run '$ant compile' to compile the .jar archive and generate documentation.
3) Finally, to start the application, execute '$ant run'.

Implementation features
The structure of the project corresponds to the architecture from the 3rd assignment up to a Hint class in klondike.board package. The implementation of respective packages was also performed respecting the architecture in the 3rd assignment, namely, the frontend (gui) was designed by xivanc03 and the backend (model) was implemented by xandri03 with some intermediate packages (klondikeInterface, board) being designed by both of the authors.