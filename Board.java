
public class Board {

	Square[][] s = new Square[8][8]; // instantiates a new 2 dimensional array of 64 squares, this will serve as our board
	int numCovered; // keeps track of how many spaces have been covered on the board
	int currX; // current x position of the knight
	int currY; // current y position of the knight
	int startX; // starting x position of the knight
	int startY; // starting y position of the knight
	
	// constructor for Board, takes 2 integers which represent the starting position for the knight
	public Board(int x, int y) {
		// nested for loop that creates our board
		for (int i = 0; i < 8; i ++) {
			for (int j = 0; j < 8; j ++) {
				s[i][j] = new Square(i, j);
			}
		}
		numCovered = 1; // we are assuming that the square where the knight starts is covered
		currX = x; // assigns the starting and current x and y positions
		currY = y;
		startX = x;
		startY = y;
	}
	
	// this method will be invoked if the knight does not make it all the way around the board,
	// we will re-instantiate the board, and reset currX and currY variables to the starting locations
	public void reset() {
		for (int i = 0; i < 8; i ++) {
			for (int j = 0; j < 8; j ++) {
				s[i][j] = new Square(i, j);
			}
		}
		currX = startX;
		currY = startY;
		numCovered = 1;
	}
	
	// returns number of spaces covered
	public int getNumCovered() {
		return numCovered;
	}
	
	// returns a String with the current x and y position of the knight
	public String place() {
		return (" (" + currX + ", " + currY + ") ");
	}
	
	// i have a feeling this method could be written/run cleaner and lighter if written
	// recursively, but as the variables/methods/classes/objects all came together it just 
	// fell together non-recursively.
	
	// the move() method takes the current position and runs the figureNumMoves() method. 
	// if there are not any valid moves, it returns false
	// for each move that is available it runs the figureNumMoves() method on that location
	// the method then invokes the changePosition() method and moves the knight to the location
	// with the least possible moves afterwards.  If there is a tie, the knight will move to the
	// last ideal position that was offered
	public boolean move(Board b) {
		// if the board has been covered false is returned
		if (numCovered == 64) {
			return false;
		}
		int j = 10; // stores the lowest number of moves that are available
		int hold = 0; // this variable holds the index of the ideal move
		int oldX = currX; // 2 new integers are created to store the current position of the knight
		int oldY = currY;
		int u = 0; //a variable to hold the number of moves that are available from the temporary positions
		
		for (int i = 0; i < 8; i ++) {
			currX = oldX;
			currY = oldY;
			// checks to see how many moves are available
			s[currX][currY].figureNumMoves(s, currX, currY);
			// returns false if none are available
			if (s[currX][currY].getNumMoves() == 0) {
				return false;
			}
			// if this move if available, the knight temporarily moves there and checks to see how many moves are available from there
			if (s[currX][currY].getOption()[i]) {
				b.changePosition(i);
				s[currX][currY].figureNumMoves(s, currX, currY);
				u = s[currX][currY].getNumMoves();
				// if this number is LESS THAN OR EQUAL TO our lowest number, then it remembered with the hold variable
				if (u <= j) {
					j = u;
					hold = i;
				}
			}
		}
		currX = oldX;
		currY = oldY;
		// once we have our best possible move, then we officially move our knight there by invoking the changePosition() method
		b.changePosition(hold);
		// increments the number of spaces covered
		numCovered ++;
		// marks the old spot where the knight came from as null
		s[oldX][oldY] = null;
		// returns true, so that the loop can continue
		return true;
	}
	
	// identical to move() except if there is a tie between ideal moves, it accepts the first one
	public boolean move2(Board b) {
		if (numCovered == 64) {
			return false;
		}
		int j = 10;
		int hold = 0;
		int oldX = currX;
		int oldY = currY;
		int u = 0;
		
		for (int i = 0; i < 8; i ++) {
			currX = oldX;
			currY = oldY;
			s[currX][currY].figureNumMoves(s, currX, currY);
			if (s[currX][currY].getNumMoves() == 0) {
				System.out.println("failure at " + currX + "" + currY);
				return false;
			}
			if (s[currX][currY].getOption()[i]) {
				b.changePosition(i);
				s[currX][currY].figureNumMoves(s, currX, currY);
				u = s[currX][currY].getNumMoves();
				// if the number of moves is LESS THAN our previous lowest it is saved with the hold variable
				if (u < j) {
					j = u;
					hold = i;
				}
			}
		}
		currX = oldX;
		currY = oldY;
		b.changePosition(hold);
		numCovered ++;
		s[oldX][oldY] = null;
		return true;
	}
	
	// this method actually moves our knight permanently to a new space.  this utilizes the list of possible moves that
	// I supplied in the Driver class.  If t == 0, then it will make the first move, t == 1 the second move and so on.
	public void changePosition(int t) {
		if (t == 0) {
			currX += 1;
			currY += 2;
		}
		
		else if (t == 1) {
			currX += 2;
			currY += 1;
		}
		
		else if (t == 2) {
			currX += 2;
			currY -= 1;
		}
		
		else if (t == 3) {
			currX += 1;
			currY -= 2;
		}
		
		else if (t == 4) {
			currX -= 1;
			currY -= 2;
		}
		
		else if (t == 5) {
			currX -= 2;
			currY -= 1;
		}
		
		else if (t == 6) {
			currX -= 2;
			currY += 1;
		}
		
		else if (t == 7) {
			currX -= 1;
			currY += 2;
		}
		
		else 
			// for testing only, should be unreachable code
			System.out.println("error, move option was > 7");
	}
	
}
