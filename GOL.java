/*
	Ariana Alvarez 
	CS101 - 005
	Project 1: Game of Life
	Part 2 - Q3: Calling different grids
*/


// This class allows user to decide which grid to run
public class GOL {		
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++){			
			if (i >= 2) 
				if (args[2].equals("hex"))
					hexGOL.main(args); 
			else 
				break;		
		} 
		squareGOL.main(args);
	}
}


