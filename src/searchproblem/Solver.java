package searchproblem;

import java.util.ArrayList;

public class Solver{
	
	private final Parser parser;
	
	public Solver(Parser parser) {
		this.parser = parser;
	}

    // gonna have to take a lot of params that the parser produces
    public void solve(){
        Node root = new Node();
        Node currentNode = root;
        int minPenalty = 0;

        ArrayList<Node> solution = new ArrayList<Node>();
        ArrayList<Node> bestSolution = new ArrayList<Node>();
        /*  this will use Evaluator, and Evaluator to build out the solution
	
	solution = depthFirstSolve(solution);	//probably return an empty list if no solution exists, check if empty after calling this\
	bestSolution = solution; //copy into here
	
	//while bestSolution isn't empty:
		bestSolution = breadthFirstSolve(new ArrayList<Node>);


    ----- DEPTH FIRST SEARCH FIRST ---- find any solution, then do breadth
	----- Will also want to do any partial assignments, or course Preferreds first, give us 
	----- our best solutions first so Eval can eliminate as many branches as possible later
        for every course:
	    boolean validCombination = false
            for every slot:
                if solution + this course to this slot is Validated add to solution
                    should probably split off into another function and recurse, and have it return
                    a boolean of whether this path is valid if not continue to next slot

		    validCombination = true
	    if validCombination: break
	if !validCombination: need to backtrack to next slot for last course assigned.
        
        ---- BREADTH SEARCH ---- use Eval in here
        for every course:
            for every slot:
                push Eval onto ordered list of best assignments
            search down the lowest penalty branch first .. aka assign it in solution
            again recursion probs best for this

        */
    }

    /*
    private ArrayList<Node> depthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledItem> courseOrLab, int expectedPairsReturned){
    // basically don't use eval for this one, just want any solution
       for every slot
            if can assign
                Node newNode = new Node()
                solution.append(newNode);
                solution.append(depthFirstSolve(solution, nextCourse, expectedPairsReturned-1) //nextCourse probs just and index of some global ArrayList
                if solution.length == expectedPairsReturned:
                    return solution
                else
                    solution.remove(newNode)
                    continue
       return new ArrayList<Node>   // return empty list cause no valid solution down this branch
    }
    */
}
