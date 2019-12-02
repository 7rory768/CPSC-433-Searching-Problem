package searchproblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import searchproblem.classes.*;

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

    public void solve(){
        Node root = new Node();
        ArrayList<Node> bestSolution = new ArrayList<Node>();

        ArrayList<ScheduledClass> toBeScheduled = new ArrayList<ScheduledClass>(parser.getCourses());
        toBeScheduled.addAll(parser.getLabs());
        
        // TO-DO Need to decide the data structure that stores a partial assignment
        HashMap<ScheduledClass, Slot> partialAssignments = parser.getPartialAssignments();
        ArrayList<Node> partialSolution = new ArrayList<Node>();
        partialSolution.add(root);
        int currentPenalty = 0;
        
        // iterate through partial assignments and make a node for each one
        ArrayList<ScheduledClass> copy = new ArrayList<ScheduledClass>(parser.getCourses());
        copy.addAll(parser.getLabs());
        for(ScheduledClass sc : copy){
        	Slot s = parser.getPartialAssignment(sc);
        	if (s == null) continue;

	    	System.out.println("See a partial assignmnet");
	        Node newNode = new Node(s, sc, partialSolution.get((partialSolution.size()-1)));
	        if (!validator.validate(newNode)){
	        	System.out.println("Input requires a partial assignment that is not valid. Exiting.");
	        	System.exit(1);
	        }
	        currentPenalty += evaluator.evaluate(newNode);
	        partialSolution.add(newNode);
	        toBeScheduled.remove(sc);
	        System.out.println("tick");
	     }

        // returns an empty list if no solution exists, check if empty after calling this
	    bestSolution = depthFirstSolve(partialSolution, toBeScheduled, currentPenalty);	
        if(bestSolution.isEmpty()){
            System.out.println("No solution found for this problem.");
            System.exit(0);
        }
        System.out.println("Found an assignment with penalty of: " + Integer.toString(minPenalty));
        if (minPenalty == 0) System.exit(0);
		bestSolution = breadthFirstSolve(partialSolution, toBeScheduled, currentPenalty);
        if(!bestSolution.isEmpty()){
            System.out.println("Found an assignment with penalty of: " + Integer.toString(minPenalty));
        }
    }

    private ArrayList<Node> depthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledClass> toBeScheduled, int penalty){
        // basically don't use eval for this one, just want any solution
    	// TO-DO .. think about looking at preferred slots first and unpreferred slots last if performance bad

    	ArrayList<ScheduledClass> toBeScheduled_copy = new ArrayList<ScheduledClass>(toBeScheduled);
        ScheduledClass current = toBeScheduled_copy.get(0);
        toBeScheduled_copy.remove(0);

        ArrayList<Slot> slots = (current instanceof Course) ? parser.getCourseSlots() : parser.getLabSlots();
        for(Slot s : slots){
            Node newNode = new Node(s, current, solution.get(solution.size()-1));
            if (!validator.validate(newNode)){
                continue;
            }
            
            
            
            System.out.println(current.getDepartment() + " " + current.getCourseNum() + " " + current.getLectureNum() + " --> " + s.getDay() + " : " + s.getSlotTime());
            if (current instanceof Course) {
            	System.out.println("Course");
            } else {
            	System.out.println("Lab");
            }
            System.out.println(Integer.toString(solution.size()));
            
            
            if(toBeScheduled_copy.isEmpty()){
            	minPenalty = penalty + evaluator.evaluate(newNode);
                System.out.println("Depth first search found a solution.");
                solution.add(newNode);
                return solution;
            } else {
                solution.add(newNode);
                penalty += evaluator.evaluate(newNode);
                // go down this branch
                ArrayList<Node> temp = depthFirstSolve(solution, toBeScheduled_copy, penalty);
                if (!temp.isEmpty()){
                // if solution found in this branch return it up the recursion
                    return temp;
                }
                //else remove this node from solution and check next slot
                solution.remove(newNode);	// optimization here is to remove last element
            }
        }
        // if hasn't returned by this point no solution in this branch
        // return empty array list
        System.out.println("depth search backtracking");
        return new ArrayList<Node>();
    }

    private ArrayList<Node> breadthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledClass> toBeScheduled, int currPenalty){
        ScheduledClass current = toBeScheduled.get(0);

        // so we don't mutate the list for nodes up higher in the tree
        ArrayList<ScheduledClass> toBeScheduled_copy = new ArrayList<ScheduledClass>(toBeScheduled);
        toBeScheduled_copy.remove(0);

        ArrayList<Slot> slots = (current instanceof Course) ? parser.getCourseSlots() : parser.getLabSlots();
        ArrayList<Node> orderedBestChildren = new ArrayList<Node>();
        int penalty;
        for(Slot s : slots){
            Node newNode = new Node(s, current, solution.get(solution.size()-1));
            if (!validator.validate(newNode)){
                continue;
            }
            newNode.setTotalPenalty(evaluator.evaluate(newNode));
            if(currPenalty + newNode.getTotalPenalty() >= minPenalty){
                continue;
            }
            orderedBestChildren.add(newNode);
        }
        Collections.sort(orderedBestChildren);

        // loop through orderedBestSlots to next level in tree
        ArrayList<Node> tentativeSolution = new ArrayList<Node>();
        for(Node n : orderedBestChildren){
            ArrayList<Node> temp = solution;
            penalty = n.getTotalPenalty();
            temp.add(n);
            if(toBeScheduled_copy.isEmpty()){
                // this is a leaf node, and has found a new best
                minPenalty = currPenalty + penalty;
                return temp;
            } else {
                temp = breadthFirstSolve(temp, toBeScheduled_copy, currPenalty + penalty);
                if(!temp.isEmpty()){
                    // will only be not empty if a better solution was found
                    tentativeSolution = temp;
                }
            }
        }
        return tentativeSolution;
    }   
}
