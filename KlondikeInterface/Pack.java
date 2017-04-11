/* File: KlondikeInterface/Pack.java        *
 * Project: IJA                             *
 * Authors: Roman Andriushchenko (xandri03) *
 *          Martin Ivanco (xivanc03)        */

package KlondikeInterface;

public interface Pack {
	int size();

	Card read(int i);

	void move(Pack source, Card card);

	String toString();

	static Pack fromString(String str);
}