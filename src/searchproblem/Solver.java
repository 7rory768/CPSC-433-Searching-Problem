package searchproblem;

import java.util.ArrayList;

public class Solver{
	
	private final Parser parser;
    private final Evaluator evaluator;
    private final Validator validator;
    private int minPenalty = 0;
	
	public Solver(Parser parser) {
		this.parser = parser;
        this.evaluator = new Evaluator(parser);
        this.validator = new Validator(parser);
	}

    // gonna have to take a lot of params that the parser produces
    public void solve(){
        Node root = new Node();
        Node currentNode = root;

        ArrayList<Node> bestSolution = new ArrayList<Node>();

        int solutionLength = parser.getCourses().size() + parser.getLabs().size();

        ArrayList<ScheduledItem> toBeScheduled = parser.getCourses().addAll(parser.getLabs());
        
        // TO-DO Need to decide the data structure that stores a partial assignment
        ArrayList<Pair> partialAssignments = parser.getPartialAssignments();
        ArrayList<Node> partialSolution;
        int currentPenalty = 0;
        
        for(Pair pa : partialAssignments){
            Node newNode = new Node();
            //assign newNodes Slot and Course/Lab
            // assign newNodes penalty with Eval, add to minPenalty
            // check if valid .. if not then hard constraints contradict each other .. return error msg
            partialSolution.add(newNode)
        }

	
	    bestSolution = depthFirstSolve(partialSolution, toBeScheduled, solutionLength);	// return an empty list if no solution exists, check if empty after calling this\
        if(bestSolution.size() == 0){
            System.out.println("No solution found for this problem.");
            System.exit(0);
        }
        minPenalty = evaluator.evaluate(bestSolution);  //probably more efficient ways to get this
		bestSolution = breadthFirstSolve(partialSolution, toBeScheduled, minPenalty, solutionLength);
        minPenalty = evaluator.evaluate(bestSolution);
        System.out.println("Found an assignment with penalty of: " + Integer.toString(minPenalty));
    }

    
    private ArrayList<Node> depthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledItem> toBeScheduled){
        // basically don't use eval for this one, just want any solution

        ScheduledItem current = toBeScheduled.get(0);
        toBeScheduled.remove(0);

        Node newNode = new Node(current, solution.get(solution.size()-1));

        ArrayList<Slot> slots = (newNode.course != null) ? parser.getCourseSlots() : parser.getLabSlots();
        for(Slot s : slots){
            newNode.setSlot(s)
            if (!validator.validate(solution, newNode)){
                continue;
            }
            if(toBeScheduled.size() == 0){
                System.out.println("Depth first search found a solution");
                return solution.add(n);
            } else {
                solution.add(n);
                ArrayList<Node> temp = depthFirstSolve(solution, toBeScheduled)
                if (temp.size() != 0){
                    return temp;
                }
            }
        }
        // if hasn't returned by this point no solution in this branch
        // return empty array list
        return new ArrayList<Node>;
    }

    private ArrayList<Node> breadthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledItem> toBeScheduled, currPenalty){
        // basically don't use eval for this one, just want any solution

        ScheduledItem current = toBeScheduled.get(0);
        toBeScheduled.remove(0);


        ArrayList<Slot> slots = (newNode.course != null) ? parser.getCourseSlots() : parser.getLabSlots();
        ArrayList<Node> orderedBestChildren = new ArrayList<Node>;
        int penalty;
        for(Slot s : slots){
            Node newNode = new Node(current, s, solution.get(solution.size()-1));
            if (!validator.validate(solution, newNode)){
                continue;
            }
            // will want to save this in the Node
            penalty = evaluator.evaluate(solution, newNode);
            if(currentPenalty + penalty >= minPenalty){
                continue;
            }
            // TO-DO insert s into the right place on orderedBestSlots
            // for now just add it
            orderedBestChildren.add(newNode);
        }

        // loop through orderedBestSlots to next level in tree
        for(Node n : orderedBestChildren){
            ArrayList<Node> temp = solution;
            temp.add(n)
            if(toBeScheduled.size() == 0){
                minPenalty = currentPenalty + evaluator.evaluate(solution, n);
                break; // want to break on the best leaf, since no other leaf will be as good
            } else {
                temp = breadthFirstSolve(temp, toBeScheduled, currentPenalty + penalty)
                return temp;
            }
        }
        return new ArrayList<Node>;
    }   
}
