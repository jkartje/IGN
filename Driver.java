import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
 * 
 * Solution 1
 * To find how many pennies can fit on the Golden Gate Bridge, I first found the measurements for the Penny.  Then
 * I found the measurements for the length and width of the bridge.  I found how many pennies would fit length and 
 * width-wise on the bridge, then I multiplied those numbers together.  I used meters as a default unit of measurement.
 * 
 * Solution 2
 * To find 3 valid image sizes that maintain a 16:9 ratio with the width being evenly divisible by 12, I simply multiplied
 * 16 and 9 by 12, 24, and 36 (multiples of 12) and listed those image sizes.
 * 
 * Solution 3
 * My first thought was that it is possible for a knight to move to each space on the chess board once without overlapping.
 * To test that idea, I first thought that the knight should cover the outside spaces first.  I toyed with the idea of 
 * giving each square a point value, with the outside squares having the highest, and programming my knight to move to the 
 * square with the highest point value.  Thinking that might be a little odd to program, I moved to the idea that I wanted
 * the knight to move to the square with the least valid moves afterwards.  That seemed much more practical.  So after testing
 * a few spaces manually on my own graph paper board I proceeded to coding.
 * The pseudo-code went like this
 * ---from current position, check to see which moves are available and how many are available
 * ---move to the space with the least valid moves afterwards, if there is a tie, simply take the last move discovered
 * ---repeat until all spaces are covered, or until no moves available
 * The first snag was when I realized there were going to be many times that the 'multiple moves with the same number of moves
 * after them' situation was going on.  I decided that if there was a tie, I would just accept the last offered move.
 * Upon testing the knight made it around the board perfectly starting from a corner or (0, 0).  I decided then to test my 
 * program with different starting spots.  My program randomly generates a starting spot, and it worked 99% of the time.
 * Since I was getting an error message every once in a while I decided to write a nested for loop to test all of the possible
 * starting locations.  Turns out the knight made it around the board flawlessly every time except for one starting position.
 * I tried to think of something in my algorithm that I could change to successfully apply it to all cases.  
 * Then I looked at what my program did with the situation when there was multiple ideal moves.  I decided to do the opposite,
 * I coded it so that it would accept the last ideal move instead of the first one.  This, when tested, proved to have
 * the same flaw, just a different starting position didn't work.  Both failed at the same number of moves also.  But the good
 * news was that it was a different spot.  So I now had to add a little into my move() method.  The new pseudo-code went like 
 * this
 * ---from current position, check to see which moves are available and how many are available
 * ---move to the space with the least valid moves afterwards, if there is a tie, simply take the last move discovered
 * ---repeat until all spaces are covered, or until no moves available
 * ------if no moves available, return to starting position and implement algorithm only now accepting the first move discovered
 * The program will now follow this logic and find a path for the knight to traverse every square on the board in 63 moves
 * and print out spots that it chooses.  This is successful regardless of starting position.
 * 
 * The proof that my code is sound, is done with a nested for loop testing every possible starting space, instead I wrote the 
 * program to generate a new starting space every time and display the knight's moves so that there was a more visual 
 * representation of my work.
 * 
 * I have created a list of the 8 possible moves that the knight can make on the board.
 * These will be referred to in the Square and Board classes
	 1. x + 1, y + 2
	 2. x + 2, y + 1
	 3. x + 2, y - 1
	 4. x + 1, y - 2
	 5. x - 1, y - 2
	 6. x - 2, y - 1
	 7. x - 2, y + 1
	 8. x - 1, y + 2
 */

public class Driver {
	
	static JButton calc1 = new JButton("Calculate!"); // Button used in Solution 1
	static JLabel answerField1 = new JLabel(""); // Text field to display the answer for Solution 1
	static int total1 = 0; // Integer variable to store answer for Solution 1
	static JButton calc3 = new JButton("Make it happen"); // Button used in Solution 3
	static JEditorPane list3 = new JEditorPane(); // Field that will display the knight's moves for Solution 3
	static int counter = 1; // Counter variable used to help with text formatting in Solution 3
	
	// This method is called when the calc1 button is pressed, It will add this text to answerField1
	public static void action1() {
		answerField1.setText(" " + total1 + " total pennies");
	}
	
	// This method is called when the user presses calc3
	public static void action3() {
		list3.setText(" ");
		String moves = "\n"; // variable that stores the list of moves that the knight makes
		int sx = (int)((Math.random()*7)); // generates a random number between 0 and 7 inclusive and truncates it and converts it to an integer
		int sy = (int)((Math.random()*7));
		Board b = new Board(sx, sy); // instantiates a new Board object with the randomly generated starting spaces
		while(b.move(b)) { // loops the move() method until it returns false
			if (counter%8 == 0){ // if this counter is 8 or a multiple of 8 it adds a new line, for formatting.
				moves = moves + "\n"; 
			}
			moves = moves + b.place(); // each time this code is executed it adds the knight's current location to the String
			counter ++; // increments counter
		}
		if(b.numCovered < 64) { // if the board is not completely covered, then we are going to call the reset() method,
			// which returns us to the starting location, and invokes the move2() method.
			moves = "\n";
			b.reset();
			while(b.move2(b)) {
				if (counter%8 == 0){
					moves = moves + "\n";
				}
				moves = moves + b.place();
				counter ++;
			}
		}
		// sets the text of list3 to include the starting space and a list of moves.
		list3.setText("\nHere is a program-generated list of the moves" + "\nin order that our knight makes (starting at (" + sx + ", " + sy + ")):" + moves);
	}
	
	public static void main(String[] args) {
		// start panelIntro
		JPanel panelIntro = new JPanel(new BorderLayout(1, 1));
		JLabel title = new JLabel("<html> <h4> J. Kartje </h4> <h1> <b> Code-Foo Challenge </b> </h1> </html>", JLabel.CENTER);
		JEditorPane introText = new JEditorPane();
		introText.setText("\n     Hello, my name is J. Kartje and I wrote this program to showcase my submission for the Code-Foo Challenge." +
				"\nThere are tabs for each solution.  I invite you to explore them and the code under the hood.  This program is written in Java " +
				"\nand implements the Swing Package.  Nothing is too complicated or pretty here, but I wanted to give you an idea of some of " +
				"\nmy programming experience.  The 'Solution 4' tab has details about me and why I think I am perfect for this position.  I have" +
				"\nalso included a summary of my qualifications and work history in the IGNresume file in my repository." +
				"\n\n     Welcome to my submission and I invite you take a look around.");
		// adds the title and the text to panelIntro and adds some blank JLabels to give us a little space on the borders
		panelIntro.add(title, BorderLayout.NORTH);
		panelIntro.add(introText, BorderLayout.CENTER);
		panelIntro.add(new JLabel("      "), BorderLayout.WEST );
		panelIntro.add(new JLabel("      "), BorderLayout.EAST);
		panelIntro.add(new JLabel("      "), BorderLayout.SOUTH);
		// end panelIntro
		
		// start panel1
		double pDiameter = .01905; // penny diameter
		double bLength = 2737; // bridge length
		double bWidth = 27; // bridge width
		int wholeLength = 0; // whole number of pennies that fit on the bridge length-wise
		int wholeWidth = 0; // whole number of pennies that fit on the bridge width-wise
		
		
		JPanel panel1 = new JPanel(new BorderLayout(1, 1));
		JLabel label1 = new JLabel("<html> <h2> Solution 1 </h2> </html>", JLabel.CENTER);
		panel1.add(label1, BorderLayout.NORTH);
		JEditorPane text1 = new JEditorPane();
		
		// We have to truncate these calculations and convert them to integers because we don't want pennies hanging off the ledges
		wholeLength = (int)(bLength/pDiameter);
		wholeWidth = (int)(bWidth/pDiameter);
		total1 = wholeLength*wholeWidth;
		
		text1.setText("How many pennies can fit on the Golden Gate Bridge without any of them overlapping?" +
				"\n\nFirst we will get the measurements of the Penny and the Golden Gate Bridge.  My sources were " +
				"\nwww.usmint.gov and www.goldengatebridge.org" +
				"\n\nPenny diameter: .01905 meters (we will use meters as the default unit of measurement)" +
				"\nBridge length: 2737 meters (not including distance to toll plaza)" +
				"\nBridge width: 27 meters" +
				"\n\nNext on our list is to find out how many pennies will fit on the bridge length-wise and width-wise. " +
				"\nLength: " + bLength + " / " + pDiameter + " = " + bLength/pDiameter +
				"\nBut we want that to be a whole number so no pennies are hanging off the edge, so we'll say " + wholeLength +
				"\nWidth: " + bWidth + " = " + " / " + pDiameter + " = " + bWidth/pDiameter +
				"\nFollowing the same logic, the number of pennies fitting width-wise will be " + wholeWidth +
				"\n\nOur last step is multiplying our two whole numbers together...");
		// add the text to panel1
		panel1.add(text1, BorderLayout.CENTER);
		
		JPanel bottom1 = new JPanel();
		JLabel numbers1 = new JLabel("  " + wholeLength + " x " + wholeWidth + "  ");
		bottom1.add(numbers1);
		bottom1.add(calc1);
		// adds a listener to the button, calc1 and assigns the action1() method to this listener
		calc1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				action1();
			}
		});
		// add the answer fields and buttons to panel1
		bottom1.add(answerField1);
		panel1.add(bottom1, BorderLayout.SOUTH);
		// end panel1
		
		// start panel2
		JPanel panel2 = new JPanel(new BorderLayout(1,1));
		JLabel label2 = new JLabel("<html> <h2> Solution 2 </h2> </html>", JLabel.CENTER);
		panel2.add(label2, BorderLayout.NORTH);
		JEditorPane text2 = new JEditorPane();
		// creating length and width variables for each of the proposed images
		int length1 = 16*12;
		int height1 = 9*12;
		int length2 = 16*24;
		int height2 = 9*24;
		int length3 = 16*36;
		int height3 = 9*36;
		text2.setText("Our images have a ratio of 16:9, and our design layouts have 12 pixel wide increments (there " +
				"\nare no limits on height). Give examples of three image sizes that would have the correct ratio " +
				"\nand would fit the design layout?" +
				"\n\nOne thing to think about it is that we want our image sizes to be whole numbers. To get these" +
				"\nimage sizes we will multiply 16 and 9 both by multiples of 12, 3 times (we will use 12, 24, and 36)." +
				"\n\n 16 x 12 = " + length1 + " and 9 x 12 = " + height1 + " so our first image size would be " + length1 + " by " + height1 + " pixels " +
				"\n 16 x 24 = " + length2 + " and 9 x 24 = " + height2 + " so our second image size would be " + length2 + " by " + height2 + " pixels " +
				"\n 16 x 36 = " + length3 + " and 9 x 36 = " + height3 + " so our third image size would be " + length3 + " by " + height3 + " pixels ");
		// adding the text field to panel2
		panel2.add(text2);
		// end panel2
		
		// start panel3
		JPanel panel3 = new JPanel(new BorderLayout(1,1));
		JLabel label3 = new JLabel("<html> <h2> Solution 3 </h2> </html>", JLabel.CENTER);
		panel3.add(label3, BorderLayout.NORTH);
		// creating a panel with a grid layout.  I am going to insert 3 panels into this panel: info3, logic3Box, filler, and list3
		JPanel iPanel3 = new JPanel(new GridLayout(2, 2));
		JEditorPane info3 = new JEditorPane();
		info3.setText("What is the minimum number of moves for a knight to cover " +
				"\nthe entire chess board?" +
				"\n\n     First lets make a few clarifications: ideally we want the knight to " +
				"\nhit each space on the board once without hitting any twice.  With my " +
				"\nprogram, the knight's starting position does not matter, it can be (0, 0) " +
				"\nor an arbitrary square such as (4, 4).  I have created an 8x8 chess board " +
				"\nby initializing a 2 dimensional array.  The array goes from (0, 0) to (7, 7), " +
				"\nso I will be referring to spaces as such.  Also we are assuming the " +
				"\nknight has already 'covered' the space he is starting on.");
		JEditorPane logic3 = new JEditorPane();
		JPanel logic3Box = new JPanel(new BorderLayout());
		logic3.setText("\n\n\n     My code is well commented and I encourage you to explore it.  " +
				"\nThe way to make sure that the knight can cover all the squares without " +
				"\nbacktracking is to always move to the square with the least number of " +
				"\nmoves available after it.  Pressing the button will generate a random starting " +
				"\nspace for the knight.  It will also move the knight around the board and " +
				"\ngenerate a list of moves that the knight has made.  The graph requested is" +
				"\nincluded in my repository titled KNIGHTGRAPH.  The button can be pressed, " +
				"\nrepeatedly, it will generate a new random space every time.  But the answer " +
				"\nis always 63.\n");
		// adding the text to logic3Box
		logic3Box.add(logic3, BorderLayout.CENTER);
		// adding a listener to the calc3 button and assigning the action3() method to this action
		calc3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				action3();
			}
		});
		// add the 4 panels to iPanel3
		logic3Box.add(calc3, BorderLayout.SOUTH);
		iPanel3.add(info3);
		iPanel3.add(logic3Box);
		JEditorPane filler = new JEditorPane();
		// created a filler text field to keep everything aesthetically pleasing.
		filler.setText(" ");
		iPanel3.add(filler);
		iPanel3.add(list3);
		// add iPanel3 to panel3 and add some blank JLabels to add some spacing around the edges.
		panel3.add(iPanel3, BorderLayout.CENTER);
		panel3.add(new JLabel("  "), BorderLayout.WEST);
		panel3.add(new JLabel("  "), BorderLayout.EAST);
		// end panel3
		
		// start panel4
		// panel4 has no buttons and only text, similar to panel2, but no calculations are needed
		JPanel panel4 = new JPanel(new BorderLayout(1,1));
		JLabel label4 = new JLabel("<html> <h2> Solution 4, About me </h2> </html>", JLabel.CENTER);
		panel4.add(label4, BorderLayout.NORTH);
		JEditorPane text4 = new JEditorPane();
		text4.setText("Creatively prove to us that you meet our value - Fire - that this would be more than just a " +
				"\njob to you, and that you are passionate about us. " +
				"\n\nI have detailed my work experience, technical qualifications, and education with my resume." +
				"\nThere I highlight why you would want to choose me, but here I will discuss why I want to work with you." +
				"\n\nI have been an IGN fan ever since I would be able to sneak online and start up that dial-up " +
				"\nconnection in that magic moment when my mom wouldn't realize I was hogging the phone line.  I was hooked" +
				"\nas soon as IGN: Imagine Games Network was the first hit in a HotBot search when I typed in 'cheat codes'.  " +
				"\nI remember reading about the mysterious Mega Man X - Hadouken cheat.  I remember the anticipation from using " +
				"\nall the ink in our printer to print out the 9 page preview for Final Fantasy 9 so that I could read it " +
				"\nin bed at night." +
				"\n\nWhen someone asks me 'What is your dream job?', the answer has consistently been that I wanted to work with" +
				"\na major website.  I also want to be involved in the video game industry.  I always knew that working at IGN " +
				"\nwould be a dream come true.  I frequently read the job postings for fun, knowing that I wouldn't have a " +
				"\nchance for at least a few years.  That is where I found the Code-Foo Challenge." +
				"\n\nI am intelligent, motivated, professional, and I learn quickly.  Having the summer off from my work down" +
				"\nin Texas and reading your description of this opportunity leads me to believe I am a perfect candidate.  I " +
				"\nwill have a chance to see first-hand the job that I have glorified over the years and, if I you can find a " +
				"\nplace for me, prove to you how much I want this.  I bring beginner to intermediate experience in HTML/CSS, " +
				"\nJava, C++, and JavaScript to the table.  I also bring a serious passion to learn and contribute.");
		
		panel4.add(text4);
		// end panel4
		
		// start final assembly
		// I used the included JTabbedPane class to put all of these panels together
		// framex will be the outermost container
		JPanel panelx = new JPanel(new GridLayout(1, 1));
		JTabbedPane tabbed = new JTabbedPane();
		
		// add each of the panels into its respective tab
		JPanel tabIntro = new JPanel(new GridLayout(1, 1));
		tabIntro.add(panelIntro);
		tabbed.addTab("Intro", tabIntro);
		
		JPanel tab1 = new JPanel(new GridLayout(1, 1));
		tab1.add(panel1);
		tabbed.addTab("Solution 1", tab1);
		
		JPanel tab2 = new JPanel(new GridLayout(1, 1));
		tab2.add(panel2);
		tabbed.addTab("Solution 2", tab2);
		
		JPanel tab3 = new JPanel(new GridLayout(1, 1));
		tab3.add(panel3);
		tabbed.addTab("Solution 3", tab3);
		
		JPanel tab4 = new JPanel(new GridLayout(1, 1));
		tab4.add(panel4);
		tabbed.addTab("Solution 4", tab4);
		
		// create the outermost container framex and add tabbed to framex
		JFrame framex = new JFrame("Code-Foo Challenge | J. Kartje");
		panelx.add(tabbed);
		// set size for the frame
		framex.setSize(950, 550);
		framex.add(panelx, BorderLayout.CENTER);
		framex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framex.setVisible(true);
		// end final assembly
		
	} // end main

} // end driver
