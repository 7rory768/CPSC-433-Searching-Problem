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
    private float basePenalty = 0;
    private float subtractingPenalties;
    
    private float originalSubPen;
    private float originalBasePen;
    
    private boolean zeroFound = false;
	
	public Solver(Parser parser, Evaluator evaluator) {
		this.parser = parser;
        this.evaluator = evaluator;
        this.validator = new Validator(parser);
	}

    public void solve(){
        Node root = new Node();
        initializePenalties();
        ArrayList<Node> bestSolution = new ArrayList<Node>();

        ArrayList<ScheduledClass> toBeScheduled = sortCourses(new ArrayList<ScheduledClass>(parser.getCourses()), new ArrayList<ScheduledClass>(parser.getLabs()));
        //new ArrayList<ScheduledClass>(parser.getCourses());
        //toBeScheduled.addAll(parser.getLabs());
        
        ArrayList<Node> partialSolution = new ArrayList<Node>();
        partialSolution.add(root);
        
          
        // iterate through partial assignments and make a node for each one
        ArrayList<ScheduledClass> copy = new ArrayList<ScheduledClass>(parser.getCourses());
        copy.addAll(parser.getLabs());
        for(ScheduledClass sc : copy){
        	Slot s = parser.getPartialAssignment(sc);
        	if (s == null) continue;

	        Node newNode = new Node(s, sc, partialSolution.get((partialSolution.size()-1)));
	        if (!validator.validate(newNode)){
	        	System.out.println("Input requires a partial assignment that is not valid. Exiting.");
	        	System.exit(1);
	        }
	        partialSolution.add(newNode);
	        s.scheduled.add(sc);
	        originalSubPen -= calcSubtractingPenalties(newNode);
	        originalBasePen += evaluator.evaluateSectionOverlap(newNode);
	        
	        toBeScheduled.remove(sc);
	     }
        subtractingPenalties = originalSubPen;
        basePenalty = originalBasePen;
        
        System.out.println("LEFT TO SCHED: " + toBeScheduled.size());
        
        
        
	    bestSolution = depthFirstSolve(partialSolution, toBeScheduled);	
               
        // Clear slots
        for (Slot s : parser.getCourseSlots()) {
        	s.scheduled.clear();
        }
        for (Slot s : parser.getLabSlots()) {
        	s.scheduled.clear();
        }
        
        if (minPenalty == 0) output(bestSolution);
        
        basePenalty = originalBasePen;
        subtractingPenalties = originalSubPen;
        
		partialSolution = breadthFirstSolve(partialSolution, toBeScheduled);
        if(!partialSolution.isEmpty()){
            bestSolution = partialSolution;            
        }
        output(bestSolution);
    }
    
    
    private void output(ArrayList<Node> bestSolution) {
    	if (bestSolution.isEmpty()) {
    		System.out.println("No Solution found for this input");
    		System.exit(1);
    	}
    	bestSolution.remove(0); // remove root
        for(Node n : bestSolution) {
        	ScheduledClass current = (n.getCourse() == null) ? n.getLab() : n.getCourse();
        	Slot s = n.getSlot();
        	
        	 System.out.println(current.getDepartment() + " " + current.getCourseNum() + " " + current.getLectureNum() + " --> " + s.getDay() + " : " + s.getSlotTime());
             if (current instanceof Course) {
             	System.out.println("Course");
             } else {
            	Lab curr = (Lab) current;
             	System.out.println("Lab " + curr.getTutorialNum());
             }
        }
        System.out.println("MINPENALTY: " + minPenalty);
        System.exit(0);
    }

    
    
    
    private ArrayList<Node> depthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledClass> toBeScheduled){
        // basically don't use eval for this one, just want any solution
    	// TO-DO .. think about looking at preferred slots first and unpreferred slots last if performance bad
    	
    	// IF THIS COURSE HAS A PAIR ENTRY, NEED TO MAKE SURE IT'S PREFERRED SLOTS ARE FIRST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    	// SHOULD SORT SLOTS SO ONES WITH HIGHER MINCOURSES/LABS COME FIRST
    	
    	
    	
    	ArrayList<ScheduledClass> toBeScheduled_copy = new ArrayList<ScheduledClass>(toBeScheduled);
    	ArrayList<Node> solution_copy = new ArrayList<Node>(solution);
        ScheduledClass current = toBeScheduled_copy.get(0);
        toBeScheduled_copy.remove(0);
        float subPenReduction = 0.0f;
        float basePenAddition = 0.0f;
        
        ArrayList<Slot> sls = (current instanceof Course) ? parser.getCourseSlots() : parser.getLabSlots();
        ArrayList<Slot> slots = new ArrayList<>(sls);
        
        // need to custom sort
        Collections.sort(slots);
        slots = sortSlots(slots, current);
        
        for(Slot s : slots){
            Node newNode = new Node(s, current, solution_copy.get(solution_copy.size()-1));
            if (!validator.validate(newNode)){
                continue;
            }
            
            solution_copy.add(newNode);
            s.scheduled.add(current);
            basePenAddition = evaluator.evaluateSectionOverlap(newNode);
            subPenReduction = calcSubtractingPenalties(newNode);
            basePenalty += basePenAddition;
            subtractingPenalties -= subPenReduction;
   
            if(toBeScheduled_copy.isEmpty()){
                System.out.println("Depth first search found a solution.");
                minPenalty = (int) (basePenalty + subtractingPenalties);
                return solution_copy;
            } else {
                // go down this branch
                ArrayList<Node> temp = depthFirstSolve(solution_copy, toBeScheduled_copy);
                if (!temp.isEmpty()){
                // if solution found in this branch return it up the recursion
                    return temp;
                }
                //else remove this node from solution and check next slot
            }
            basePenalty -= basePenAddition;
            subtractingPenalties += subPenReduction;
            s.scheduled.remove(s.scheduled.size()-1);
            solution_copy.remove(solution_copy.size()-1);	// optimization here is to remove last element
        }
        // if hasn't returned by this point no solution in this branch
        // return empty array list
        return new ArrayList<Node>();
    }

    
    
    
    
    private ArrayList<Node> breadthFirstSolve(ArrayList<Node> solution, ArrayList<ScheduledClass> toBeScheduled){
        ScheduledClass current = toBeScheduled.get(0);

        // so we don't mutate the list for nodes up higher in the tree
        ArrayList<ScheduledClass> toBeScheduled_copy = new ArrayList<ScheduledClass>(toBeScheduled);
        toBeScheduled_copy.remove(0);

        ArrayList<Slot> slots = (current instanceof Course) ? parser.getCourseSlots() : parser.getLabSlots();
        ArrayList<Node> orderedBestChildren = new ArrayList<Node>();
        float penalty = basePenalty + subtractingPenalties;
        float subPenReduction = 0;
        float basePenAddition = 0;
        
        for(Slot s : slots){
            Node newNode = new Node(s, current, solution.get(solution.size()-1));
            if (!validator.validate(newNode)){
                continue;
            }
            
            s.scheduled.add(current);
            subPenReduction = calcSubtractingPenalties(newNode);
            basePenAddition = evaluator.evaluateSectionOverlap(newNode);
            s.scheduled.remove(s.scheduled.size()-1);

            if(basePenalty + basePenAddition >= minPenalty && minPenalty != 0){
                continue;
            }
            newNode.subPen = subPenReduction;
            newNode.basePen = basePenAddition;
            newNode.setTotalPenalty((int) (subPenReduction * -1 + basePenAddition));
            orderedBestChildren.add(newNode);
        }
        Collections.sort(orderedBestChildren);

        // loop through orderedBestSlots to next level in tree
        ArrayList<Node> tentativeSolution = new ArrayList<Node>();
        for(Node n : orderedBestChildren){
            ArrayList<Node> temp = new ArrayList<>(solution);
            basePenalty += n.basePen;
            Slot s = n.getSlot();
            s.scheduled.add(n.getAssigned());
            subtractingPenalties -= n.subPen;
            temp.add(n);


            if(toBeScheduled_copy.isEmpty()){
                // this is a leaf node, check if it's solution is better
            	if (basePenalty + subtractingPenalties < minPenalty || minPenalty == 0) {
            		minPenalty = (int) (basePenalty + subtractingPenalties);
            		if (minPenalty == 0) zeroFound = true;
            		basePenalty -= n.basePen;
                    subtractingPenalties += n.subPen;
                    s.scheduled.remove(s.scheduled.size()-1);
            		System.out.println("Found Solution with: " + minPenalty);
            		return temp;
            	} else {
            		s.scheduled.remove(s.scheduled.size()-1);
            		basePenalty -= n.basePen;
                    subtractingPenalties += n.subPen;
            		continue;
            	}
            } else {
                temp = breadthFirstSolve(temp, toBeScheduled_copy);
                if(!temp.isEmpty()){
                    // will only be not empty if a better solution was found
                    tentativeSolution = temp;
                    if(zeroFound) {
                    	return tentativeSolution;
                    }
                }
            }
            
            s.scheduled.remove(s.scheduled.size()-1);
            basePenalty -= n.basePen;
            subtractingPenalties += n.subPen;
        }
        return tentativeSolution;
    }
    
    
    private ArrayList<Slot> sortSlots(ArrayList<Slot> slots, ScheduledClass sc) {
    	// already sorted by which ones have higher gaps between current and minimum assigned
    	// order so most preferred ones are first
    	ArrayList<Slot> order = new ArrayList<>();
    	ArrayList<ClassPreference> classPrefs = parser.getClassPreferences();
    	
    	// can order by weight for further improvements
    	for (ClassPreference cp : classPrefs) {
    		if(cp.getScheduledClass() == sc) {
    			order.add(cp.getSlot());
    			slots.remove(cp.getSlot());
    		}
    	}
    	
    	for(Slot s : slots) {
    		order.add(s);
    	}
    	return order;
    	
    }
    
    private ArrayList<ScheduledClass> sortCourses(ArrayList<ScheduledClass> courses, ArrayList<ScheduledClass> labs) {
    	ArrayList<ScheduledClass> order = new ArrayList<>();
    	ArrayList<ScheduledClass> toRemove = new ArrayList<>();
    	
    	for (ScheduledClass course : courses) {
    		order.add(course);
    		for (ScheduledClass lab : labs) {
    			if (lab.getDepartment().contentEquals(course.getDepartment()) && lab.getCourseNum() == course.getCourseNum()) {
    				order.add(lab);
    				toRemove.add(lab);
    			}
    		}
    		
    		for(ScheduledClass r : toRemove) {
    			labs.remove(r);
    		}
    	}
    	return order;
    }
    
    private void initializePenalties() {
    	subtractingPenalties = evaluator.initializeMinFillPenalty() + evaluator.initializeNotPairedPenalty()
    			+ evaluator.initializePreferrancePenalty();
    	originalSubPen = subtractingPenalties;
    }

    private float calcSubtractingPenalties(Node newNode) {
    	return evaluator.evaluateMinFill(newNode) + evaluator.evaluatePaired(newNode) + evaluator.evaluatePreferrance(newNode);
    }
}


