
/* J. Kartje - Code Foo Challenge
 * May 31, 2011
 * 
 * This program is written in Java and encompasses my submission.  My goal is to present a application so that
 * you may easily view my solution to each problem.
 * 
 * I probably will end up over-commenting this code, but I'd rather err on the side of caution.
 * 
 * I will detail my general theory for each solution here and I will talk about the Square and Board methods/variables
 * within those classes.  The Driver class includes the main method.  I used the Swing Package to create a simple GUI.
 */

public class Square {

	int x; // the x position of the square on the board
	int y; // the y position of the square on the board
	boolean[] option = new boolean[8]; // array of booleans to hold whether or not each move is valid
	int z; // integer to hold the number of valid moves that exist
	
	// square constructor, takes in 2 integers and assigns those to x and y
	public Square(int p, int q){
		x = p;
		y = q;
	}
	
	// returns the x value
	public int getX() {
		return x;
	}
	
	// returns the y value
	public int getY() {
		return y;
	}
	
	// returns the number of available moves
	public int getNumMoves() {
		return z;
	}
	
	// the figureNumMoves method checks to see if each of the 8 moves listed in the Driver class comments
	// are valid moves (including checking to see if they have been visited yet), if they are, it increments z.  
	// It passes in the 2 dimensional array of squares that represents our current board and the x1, y1 which 
	// represent the knight's current position.
	
	public void figureNumMoves(Square[][] s, int x1, int y1) {
		z = 0;
		// initializes the array and sets all values to false
		for (int i = 0; i < 8; i ++) {
			option[i] = false;
		}
		// once a space has been visited, I mark it null, that is how we know if we have been there before
		if ((x1 < 7) && (y1 < 6)) {
			if (s[x1+1][y1+2] != null) { 
			z ++;
			option[0] = true;
			}
		}
		if ((x1 < 6) && (y1 < 7)) {
			if (s[x1+2][y1+1] != null) {
			z ++;
			option[1] = true;
			}
		}
		if ((x1 < 6) && (y1 > 0)) {
			if (s[x1+2][y1-1] != null) {
			option[2] = true;
			z ++;
			}
		}
		if ((x1 < 7) && (y1 > 1)) {
			if (s[x1+1][y1-2] != null) {
			option[3] = true;
			z ++;
			}
		}
		if ((x1 > 0) && (y1 > 1)) {
			if (s[x1-1][y1-2] != null) {
			option[4] = true;
			z ++;
			}
		}
		if ((x1 > 1) && (y1 > 0)) {
			if (s[x1-2][y1-1] != null) {
			option[5] = true;
			z ++;
			}
		}
		if ((x1 > 1) && (y1 < 7)) {
			if (s[x1-2][y1+1] != null) {
			option[6] = true;
			z ++;
			}
		}
		if ((x1 > 0) && (y1 < 6)) {
			if (s[x1-1][y1+2] != null) {
			option[7] = true;
			z ++;
			}
		}
	}
	
	// returns the option array
	public boolean[] getOption() {
		return option;
	}
}