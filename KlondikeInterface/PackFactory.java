/* File: KlondikeInterface/PackFactory.java *
 * Project: IJA                             *
 * Authors: Roman Andriushchenko (xandri03) *
 *          Martin Ivanco (xivanc03)        */

package KlondikeInterface;

public interface PackFactory {
	Pack createDeck();

	Pack createSourcePack();

	Pack createTargetPack();

	Pack createWorkingPack();
}