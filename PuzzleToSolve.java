
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class PuzzleToSolve {
 
	public final static int DIMS=4;	                // Input DIMS=3 for 8Puzzle, Input DIMS=4 for 15Puzzle
	private int[][] puzzle_array;		            // We use a 2D array 
	private int display_of_puzzle;
	private Position Space_Position;

		
	// Create class for position
	 private class Position {
			public int x;
			public int y;
			
			public Position(int x, int y) {
				this.x=x;
				this.y=y;
			}	
		}
	
	
	// Creation of our puzzle
	public PuzzleToSolve() {
		puzzle_array = new int[DIMS][DIMS];
		int cnt=1;
		for(int i=0; i<DIMS; i++) {
			for(int j=0; j<DIMS; j++) {
				puzzle_array[i][j]=cnt;
				cnt++;
			}
		}
		
		display_of_puzzle=Integer.toString(cnt).length();
				
		// Here we put the space position which is number 0
		Space_Position = new Position(DIMS-1,DIMS-1);
		puzzle_array[Space_Position.x][Space_Position.y]=0;
	}
	
	
	//initialiaze the goal state
	public final static PuzzleToSolve SOLVED=new PuzzleToSolve();
	
	//copy the goal state to a clone puzzle
	public PuzzleToSolve(PuzzleToSolve toClone) {
		this(); 
		for(Position p: allPositions()) { 
			puzzle_array[p.x][p.y] = toClone.number(p);
		}
		Space_Position = toClone.getSpace_Position();
	}
	
	
	//returns positions from all elements
	public List<Position> allPositions() {
		ArrayList<Position> out = new ArrayList<Position>();
		for(int i=0; i<DIMS; i++) {
			for(int j=0; j<DIMS; j++) {
				out.add(new Position(i,j));
			}
		}
		return out;
	}
	
	
	public int number(Position p) {
		return puzzle_array[p.x][p.y];
	}
	
	
	public Position getSpace_Position() {
		return Space_Position;
	}
	
	
	public Position whereIs(int x) {
		for(Position p: allPositions()) { 
			if( number(p) == x ) {
				return p;
			}
		}
		return null;
	}
	

			
	public boolean isValidMove(Position p) {
		if( ( p.x < 0) || (p.x >= DIMS) ) {
			return false;
		}
		if( ( p.y < 0) || (p.y >= DIMS) ) {
			return false;
		}
		int dx = Space_Position.x - p.x;
		int dy = Space_Position.y - p.y;
		if( (Math.abs(dx) + Math.abs(dy) != 1 ) || (dx*dy != 0) ) {  //The Math.abs returns the absolute value of a given argument
			return false;
		}
		return true;
	}
	
	
	
	//returns all valid moves
	public List<Position> allValidMoves() {
		ArrayList<Position> out = new ArrayList<Position>();
		for(int dx=-1; dx<2; dx++) {
			for(int dy=-1; dy<2; dy++) {
				Position tp = new Position(Space_Position.x + dx, Space_Position.y + dy);
				if( isValidMove(tp) ) {
					out.add(tp);
				}
			}
		}
		return out;
	}
	
		
	public void move(Position p) {
		if( !isValidMove(p) ) {
			throw new RuntimeException("Invalid move");
		}
		assert puzzle_array[Space_Position.x][Space_Position.y]==0;
		puzzle_array[Space_Position.x][Space_Position.y] = puzzle_array[p.x][p.y];
		puzzle_array[p.x][p.y]=0;
		Space_Position = p;
	}
	
	//returns a new puzzle with the move applied	
	public PuzzleToSolve moveClone(Position p) {
		PuzzleToSolve out = new PuzzleToSolve(this);
		out.move(p);
		return out;
	}

	//returns all puzzles with a valid move applied 
	public List<PuzzleToSolve> allAdjacentPuzzles() {
		ArrayList<PuzzleToSolve> out = new ArrayList<PuzzleToSolve>();
		for( Position move: allValidMoves() ) {
			out.add( moveClone(move) );
		}
		return out;
	}
	
	
	//returns 0 if the candidate puzzle is equal to goal state, else returns number of mismatches
	public int numberMisplacedPositions() {
		int wrong=0;
		for(int i=0; i<DIMS; i++) {
			for(int j=0; j<DIMS; j++) {
				if( (puzzle_array[i][j] >0) && ( puzzle_array[i][j] != SOLVED.puzzle_array[i][j] ) ){
					wrong++;
				}
			}
		}
		return wrong;
	}
	
	
	public boolean isSolved() {
		return numberMisplacedPositions() == 0;
	}
	
	
	//shuffle functions:
	public void shuffle(int howmany) {
		for(int i=0; i<howmany; i++) {
			List<Position> possible = allValidMoves();
			int which =  (int) (Math.random() * possible.size());
			Position move = possible.get(which);
			this.move(move);
		}
	}

	
	public void shuffle() {
		shuffle(DIMS*DIMS*DIMS*DIMS*DIMS);
	}
	
	
	
	
	
	
	
	// Print function
		public void print() {
			System.out.println("-----------------");
			for(int i=0; i<DIMS; i++) {
				System.out.print("| ");
				for(int j=0; j<DIMS; j++) {
					int n = puzzle_array[i][j];
					String s;
					if( n>0) {
						s = Integer.toString(n);	
					} else {
						s = "";
					}
					while( s.length() < display_of_puzzle ) {
						s += " ";
					}
					System.out.print(s + "| ");
				}
				System.out.print("\n");
			}
			System.out.print("-----------------\n\n");
		}
	
	
	//Print solution
	private static void showSolution(List<PuzzleToSolve> solution) {
		if (solution != null ) {
			System.out.printf("Solution succesfull with %d moves:\n", solution.size());
			for( PuzzleToSolve sp: solution) {
				sp.print();
			}
		} 
	}
	
	
	
	
	
	
	public int manhattanDistance() {
		int sum=0;
		for(Position p: allPositions()) {
			int val = number(p);
			if( val > 0 ) {
				Position correct = SOLVED.whereIs(val);
				sum += Math.abs( correct.x = p.x );
				sum += Math.abs( correct.y = p.y );
			}
		}
		return sum;
	}

	
	// returns manhattanDistance for A*
	public int estimateError() {
		return this.manhattanDistance();
	}
	
	
	
	// Overriding equals() to compare two FifteenPuzzle objects
	@Override
	public boolean equals(Object o) {
		if(o instanceof PuzzleToSolve) {
			for(Position p: allPositions()) { 
				if( this.number(p) != ((PuzzleToSolve) o).number(p)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	//We must override hashCode() in every class that overrides equals()
	@Override 
	public int hashCode() {
		int out=0;
		for(Position p: allPositions()) {
			out= (out*DIMS*DIMS) + this.number(p);
		}
		return out;
	}
	
	
	//IDS Algorithm
	public List<PuzzleToSolve> IDSSolve() {
		HashMap<PuzzleToSolve,PuzzleToSolve> predecessor = new HashMap<PuzzleToSolve,PuzzleToSolve>();
		HashMap<PuzzleToSolve,Integer> depth = new HashMap<PuzzleToSolve,Integer>();
		
		Queue<PuzzleToSolve> toVisit = new LinkedList<PuzzleToSolve>();
		predecessor.put(this, null);
		depth.put(this,0);
		toVisit.add(this);
		int cnt=0;
		
		
		while( toVisit.size() > 0) {
			PuzzleToSolve candidate_puzzle = toVisit.remove();
			cnt++;
					
			  				
			if( candidate_puzzle.isSolved() ) {
	  			System.out.printf("Solution considered %d boards, ", cnt);
	  			System.out.printf("At Depth %d\n", depth.get(candidate_puzzle));
	  			LinkedList<PuzzleToSolve> solution = new LinkedList<PuzzleToSolve>();
	  			PuzzleToSolve backtrace_puzzle=candidate_puzzle;
	  			while( backtrace_puzzle != null ) {
	  				solution.addFirst(backtrace_puzzle);
	  				backtrace_puzzle = predecessor.get(backtrace_puzzle);
	  			}
	  			return solution;
	  		}
						
			for(PuzzleToSolve fp: candidate_puzzle.allAdjacentPuzzles()) {				  														  				
			  	if( !predecessor.containsKey(fp) ) {
			  		 predecessor.put(fp,candidate_puzzle);
				  	 depth.put(fp, depth.get(candidate_puzzle)+1);
				  	 toVisit.add(fp);
				  	 System.out.printf("Possible path for Depth %d\n", depth.get(candidate_puzzle));
					 fp.print();				  				
					 System.out.println("Goal didnt found at this depth");
					 System.out.println("_________________________________________\n\n");			
				  	}			
				}			  								
		}
		
		return null;
	}
	
	
	
	//A* Algorithm
	public List<PuzzleToSolve> aStarSolve() {
	  	HashMap<PuzzleToSolve,PuzzleToSolve> predecessor = new HashMap<PuzzleToSolve,PuzzleToSolve>();
	  	HashMap<PuzzleToSolve,Integer> depth = new HashMap<PuzzleToSolve,Integer>();
	  	final HashMap<PuzzleToSolve,Integer> heuristic = new HashMap<PuzzleToSolve,Integer>();
	  	Comparator<PuzzleToSolve> comparator = new Comparator<PuzzleToSolve>() {
	  		@Override
	  		public int compare(PuzzleToSolve a, PuzzleToSolve b) {
	  			return heuristic.get(a) - heuristic.get(b);
	  		}
	  	};
	  	PriorityQueue<PuzzleToSolve> toVisit = new PriorityQueue<PuzzleToSolve>(1000,comparator);

	  	predecessor.put(this, null);	
	  	depth.put(this,0);
	  	heuristic.put(this, this.estimateError());
	  	toVisit.add(this);
	  	int cnt=0;
	  	while( toVisit.size() > 0) {
	  		PuzzleToSolve candidate_puzzle = toVisit.remove();
	  		cnt++;
	  	
	  		if( candidate_puzzle.isSolved() ) {
	  			System.out.printf("Solution considered %d boards\n", cnt);
	  			LinkedList<PuzzleToSolve> solution = new LinkedList<PuzzleToSolve>();
	  			PuzzleToSolve backtrace_puzzle=candidate_puzzle;
	  			while( backtrace_puzzle != null ) {
	  				solution.addFirst(backtrace_puzzle);
	  				backtrace_puzzle = predecessor.get(backtrace_puzzle);
	  			}
	  			return solution;
	  		}
	  		for(PuzzleToSolve fp: candidate_puzzle.allAdjacentPuzzles()) {
	  			if( !predecessor.containsKey(fp) ) {
	  				predecessor.put(fp,candidate_puzzle);
	  				depth.put(fp, depth.get(candidate_puzzle)+1);
	  				int estimate = fp.estimateError();
	  				heuristic.put(fp, depth.get(candidate_puzzle)+1 + estimate);
	  				toVisit.add(fp);
	  			}
	  		}
	  	}
	  	return null;
	}
	
	
	
	
	public static void main(String[] args) {
		PuzzleToSolve p = new PuzzleToSolve();
		p.shuffle(25);  //if you increase the number, the algorithms will take longer
		System.out.println("Shuffled board:");
		p.print();
		
		List<PuzzleToSolve> solution;
		
				
		System.out.println("Solving with IDS");
		solution = p.IDSSolve();
		showSolution(solution);
		
		
		System.out.println("Solving with A*");
		solution = p.aStarSolve();
		showSolution(solution);
		
	}
}