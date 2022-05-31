/*
	Ariana Alvarez 
	CS101 - 005
	Project 1: Game of Life
	Part 2: Hexagonal game of life
*/

public class hexGOL {
	public static void init(boolean[][] alive, double alivePerc) {
		// Initialize the grid : we start with a random seed
		// no. of alive cells is proportional to alivePerc

		int n = (alive.length); // the dimensions of the square grid

		// dimensions of hex grid
		//double n = (Math.sqrt(3)*((alive.length)/2));

		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				alive[i][j] = Math.random() < alivePerc;
			}
		}
	}

	public static void print(boolean[][] alive) {
		// This method prints the hexagonal grid, we use • to represent a live cell
		// and two spaces for a dead cell.

		// hex grid
		int n = alive.length;
		
		for (int i = 0; i < n; i++){
			if (i % 2 == 1){ // even rows
				for (int x = 0; x < n; x++) {
					System.out.print(" / \\"); 
				}
				System.out.println("");
				for (int j = 0; j < n; j++) {
					if (j == 0)
						System.out.print("|");
					if (alive[i][j])
						System.out.print(" • |");
					else 
						System.out.print("   |");
				}
			}
			else { // odd rows
				for (int x = 0; x < n; x++) {
					System.out.print(" \\ /");
				}
				System.out.println("");
				for (int j = 0; j < n; j++) {
					if (j == 0)
						System.out.print("  |");
					if (alive[i][j])
						System.out.print(" • |");
					else
						System.out.print("   |");
					}
				}

			System.out.println("");
		}
	}
		
	public static boolean isAlive(boolean[][] alive, int i, int j) {

		// This method returns the state(alive or dead) of the cell i,j in the grid, it
		// also takes care
		// of the edges

		int n = alive.length;

		// assume that i,j >= -n
		// If we get an index >= n, then we need
		// If we get a negative index, then the smallest it can be is
		// -1, in which case we need to transform it to n-1
		// On the other hand, if we get an index greater than n-1, then we need to roll
		// it back to the 0
		// for example n becomes 0
		// if the index was initially negative, then adding n to it would clearly make
		// it positive
		// also, note that if the index was positive, then adding n wouldn't change the
		// modulo operation
		// a % n = (a+kn) % n

		int x = (i + n) % n;
		int y = (j + n) % n;

		return alive[x][y];
	}

	public static int[][] getNeighs(boolean[][] alive, int i, int j) {
		// This retrieves the x,y coordinates of the 6 neighbors

		// assume the grid is square
		int n = alive.length;

		// even-row offset coordinates for 6 neighs

        int [][] coordi =  {      {i-1, j-1},  {i, j-1},
		 					  {i-1, j}, /*{i, j},*/{i+1, j},
		  					     {i-1, j+1}, {i, j+1}        };

		return coordi;
	}

	public static int countAliveNeighs(boolean[][] alive, int i, int j) {
		// This counts all the(immediate) neighbours of the cell i,j, and returns the
		// number of all the
		// alive neighbours
		int nAlive = 0;
		int [][] neighs = getNeighs(alive,i,j);

		for (int x = 0; x < neighs.length; x++) {
				
				int [] coor = neighs[x];

				if (isAlive(alive, coor[0], coor[1]))
					nAlive++;
		}
		return nAlive;
	}

	public static boolean containsNum(int[] nums, int n) {
		// this method checks whether born and surviving arr contain num

		for (int i = 0; i < nums.length; i++){
			if (nums[i] == n)
				return true;
		}
		return false; 
	}

	public static void update(boolean[][] alive, int[] born, int[] surviving) {
		// This method is responsible to update the grid based on the rules

		// Rules : If an alive cell has :-

		// 1. < 2 alive neighbours : it dies
		// 2. 2 or 3 alive neighbours : it survives
		// 3. > 3 alive neighbours : it dies

		// If a dead cell has :-
		// 1. exactly 3 alive neighbours : it becomes alive

		// assume the grid is squared
		int n = alive.length; // len(alive)

		// We create a new grid called newAlive, we make all the changes in newAlive
		// since, if we read from the same grid that we write to, it would hinder our
		// simulation.
		// Hence, we read from the older grid, and apply the changes to the newer grid
		// and overwrite the older grid with the newer grid

		boolean[][] newAlive = new boolean[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int nAlive = countAliveNeighs(alive, i, j);
				if (isAlive(alive, i, j)) {
					boolean stayAlive = containsNum(surviving,nAlive);
						newAlive[i][j] = stayAlive;
					
				} else { // The cell is dead
					boolean reBorn = containsNum(born,nAlive);
						newAlive[i][j] = reBorn;
				}
			}
		}

		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				alive[i][j] = newAlive[i][j]; // overwrite the older grid with the newer grid
			}
		}
	}

	public static int[] parseSubstring(char initialChar, String s) {
		// This method splits the command line string into substrings

		int[] intArr = new int[s.length()-1];

		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if (i == 0){
				if (initialChar != c)
					break;
				else 
					continue;
			} else{
				try {
					int n = Integer.parseInt(String.valueOf(c));
					intArr[i-1] = n;

				} catch (Exception e) {
					continue;
				}
			}

		}
		return intArr;	
	}

	public static int[][] parseString(String s) {
		// this method converts strings into an int array

		String[] strings = s.split("/");
		
		int[][] arr = {
			parseSubstring('B',strings[0]), parseSubstring('S',strings[1])
		};

		return arr;
	}

	public static void main(String[] args) {

		//int n = 100;
		
		String grid = args[0];
		int n = Integer.parseInt(grid); // the size of our grid, we create a a grid of n rows and n columns
		
		int [][] bornSurv = parseString(args[1]);

		int[] born = bornSurv[0];
		int[] surviving = bornSurv[1];

		boolean[][] alive = new boolean[n][n];
		init(alive, 0.2); // we start with 20% alive cells


		while (true) { // This is our main loop, we update and print the grid each iteration
			print(alive);
			update(alive, born, surviving);
			//update(alive);

			// Wait 200 ms between each iteration to slow down the animation
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// this part is executed when an exception (in this example
				// InterruptedException) occurs
			}

			// Clear out the screen each iteration and flush the terminal,
			// This prints the grid on the same place every time, hence allowing the
			// animation
			System.out.print("\033[H\033[2J");
			System.out.flush();
		}
	}
}