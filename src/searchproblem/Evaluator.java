package searchproblem;

import java.util.*;

import searchproblem.classes.*;

public class Evaluator {
	Parser parser;
	int minFillCoursePen;
	int minFillTutPen;
	int secOverlapPen;
	int notPairedPen;

	float weightMinFill;
	float weightPref;
	float weightPair;
	float weightSecOverlap;
	
	public Evaluator(Parser p, int pen_labsmin, int pen_coursemin, int pen_notpaired, int pen_section, float wMF, float wPref, float wPair, float wSO) {
		this.parser = p;
		
		this.minFillTutPen = pen_labsmin;
		this.minFillCoursePen = pen_coursemin;
		this.notPairedPen = pen_notpaired;
		this.secOverlapPen = pen_section;	
		
		this.weightMinFill = wMF;
		this.weightPref = wPref;
		this.weightPair = wPair;
		this.weightSecOverlap = wSO;		
	}
	
	public float evaluateMinFill(Node assignment) {
		if(weightMinFill == 0.0) return 0;
		if(assignment.getCourse() == null && minFillCoursePen == 0) return 0;
		if(assignment.getLab() == null && minFillTutPen == 0) return 0;

		Slot s = assignment.getSlot();
		// return the amount the penalty has been reduced, and subtract it from minfill penalty in Solver
		if (s.scheduled.size() > s.getMinCourses()) return 0;
		
		if(assignment.getCourse() == null) return weightMinFill*minFillTutPen;
		return weightMinFill * minFillCoursePen;		
	}
	
	public float evaluateSectionOverlap(Node assignment) {
		if (this.weightSecOverlap == 0.0) return 0.0f;
		ArrayList<ScheduledClass> classesInSameSlot = assignment.getSlot().scheduled;
		int addedPenalty = 0;
		ScheduledClass assignedClass = (assignment.getCourse() == null) ? assignment.getLab() : assignment.getCourse();
		if (assignedClass instanceof Lab) return 0.0f;
		
		//System.out.println("Num classes in same slot: " + classesInSameSlot.size());
		for(ScheduledClass sc : classesInSameSlot) {
			if (sc instanceof Lab) continue; // not sure if this is needed
			if (assignedClass.getDepartment().equals(sc.getDepartment()) && assignedClass.getCourseNum() == sc.getCourseNum() && assignedClass.getLectureNum() != sc.getLectureNum()) {
				addedPenalty += this.secOverlapPen;
			}
		}
		return addedPenalty * this.weightSecOverlap;
	}
	
	public float evaluatePaired(Node assignment) {
		ScheduledClass assignedClass = (assignment.getCourse() == null) ? assignment.getLab() : assignment.getCourse();
		if(this.weightPair == 0.0) return 0.0f;
		if(!parser.getClassPairs().containsKey(assignedClass)) return 0.0f;
		ArrayList<ScheduledClass> paired = parser.getClassPairs().get(assignedClass);
		ArrayList<ScheduledClass> classesInSameSlot = assignment.getSlot().scheduled;
		int penaltyReduction = 0;
		
		for(ScheduledClass sc : classesInSameSlot) {
			if (sc == assignedClass) continue;
			if (paired.contains(sc)) {
				penaltyReduction += this.notPairedPen;
			}
		}
		return penaltyReduction * this.weightPair;
	}
	
	public float evaluatePreferrance(Node assignment) {
		if(this.weightPref == 0.0) return 0.0f;
		ArrayList<ClassPreference> classPrefs = parser.getClassPreferences();
		
		for(ClassPreference cp : classPrefs) {
			if ((cp.getScheduledClass() == assignment.getCourse() || cp.getScheduledClass() == assignment.getLab()) && cp.getSlot() == assignment.getSlot()) {
				return cp.getWeight() * this.weightPref;
			}
		}
		return 0.0f;
	}
	
	
	public float initializeMinFillPenalty() {
		ArrayList<Slot> courseSlots = parser.getCourseSlots();
		ArrayList<Slot> labSlots = parser.getLabSlots();
		int penalty = 0;
		
		for (Slot s : courseSlots) {
			penalty += s.getMinCourses() * this.minFillCoursePen;
		}
		for (Slot s : labSlots) {
			penalty += s.getMinCourses() * this.minFillTutPen;
		}
		System.out.println("MINFILL PEN : " + Float.toString(penalty*weightMinFill));
		return penalty * weightMinFill;
	}
	
	public float initializeNotPairedPenalty() {
		return parser.getNumPairs() * weightPair;
	}
	
	public float initializePreferrancePenalty() {
		ArrayList<ClassPreference> classPrefs = parser.getClassPreferences();
		int penalty = 0; 
		
		for (ClassPreference cp : classPrefs) {
			penalty += cp.getWeight();
		}
		
		return penalty * weightPref;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public int evaluate(Node assignment) {
		// return the penalty for this assignment

		int penalty = 0;
		
		Node current = assignment.getParent();
		Node current2 = assignment.getParent();
		Slot assignedSlot = assignment.getSlot();
		ScheduledClass item = (assignment.getCourse() != null) ? assignment.getCourse() : assignment.getLab();
		//Course assignedCourse = assignment.getCourse();
		//Lab assignedLab = assignment.getLab();
	
// finding penalty for min courses assignment
// We putting all slots from assignment until root into ArrayList AssignedSlots  
		
		ArrayList<Slot> AssignedSlots = new ArrayList<>();
		ArrayList<Slot> currAssignedSlot = new ArrayList<>();
		
		current = assignment;
		
		while(current != null && (current.getCourse() != null || current.getLab() != null))
		{
			AssignedSlots.add(current.getSlot());
			current = current.getParent();
		}
		
// next thing we iterating through each slot and see how many times it occurs if less then min we add a penalty
// if it occurs we placing it into ArrayList currAssignedSlot and ignore it so we would not calculate sl multiple time  		
		
		for(Slot sl : AssignedSlots)
		{
			if(!currAssignedSlot.contains(sl))
			{	
				if (sl.getMinCourses() > Collections.frequency(AssignedSlots, sl)) 
				{
				   penalty = penalty + 100; 	
				   currAssignedSlot.add(sl);
				}
		
			}
		}
		// pairs first we start with assignment and compare it with every node up until root if there is a pair with different slots
		//we add penalty then we take it's parrent and again compare it with every node up until root if there is a pair with different slots
		// again we add penalty. we do it up until current is at the top.
		
		current = assignment;
		current2 = assignment.getParent();
		
		
		
		while(current != null && (current.getCourse() != null || current.getLab() != null))
		{
			
			while(current2 != null && (current2.getCourse() != null || current2.getLab() != null))   
			{
				if ( current.getCourse()!= null && current2.getCourse()!=null && parser.isAPreferredPair(current.getCourse(), current2.getCourse()) && !current.getSlot().equals(current2.getSlot()) )
				{
					penalty = penalty + 100;
				}	                           
				if ( current.getCourse()!= null && current2.getLab()!=null && parser.isAPreferredPair(current.getCourse(), current2.getLab()) && !atTheSameTime(current, current2))
				{
					penalty = penalty + 100;
					
				}
				if (current.getLab()!= null && current2.getCourse()!=null && parser.isAPreferredPair(current.getLab(), current2.getCourse()) && !atTheSameTime(current, current2))
				{
					penalty = penalty + 100;
					
				}
				if (current.getLab()!= null && current2.getLab()!=null && parser.isAPreferredPair(current.getLab(), current2.getLab()) && !current.getSlot().equals(current2.getSlot()))
				{
					penalty = penalty + 100;
				
				}
				current2 = current2.getParent();  
			}
		   
			current.getParent();
		 }
		
		//now doing preferences here we just take assignment and check if it is in preferences if it is we compare preference slot
		//with actual assignment if not same we add preference weight to assignment. then we do it with parent
		// and once again we iterating up until root.
		
	    current = assignment;
	    
	    while(current != null && (current.getCourse() != null || current.getLab() != null))
	    {
	         if(current.getCourse() != null) 
	         {
	        	ClassPreference currentClassPreference = parser.getClassPreference(current.getCourse());
	            if(currentClassPreference != null && !currentClassPreference.getSlot().equals(current.getSlot()))
	            {
	            	penalty = penalty + currentClassPreference.getWeight(); 
	            }
	         
	         }
	         else if(current.getLab() != null)	 
	         {
	            ClassPreference currentClassPreference = parser.getClassPreference(current.getLab());
	            if(currentClassPreference != null && !currentClassPreference.getSlot().equals(current.getSlot()))
	            {
	            	penalty = penalty + currentClassPreference.getWeight();
	            }
	         }
	    current = current.getParent();
	    }
	    
		// now doing secdiff here Idea is same as with pairs first we comparing assignment with every node up until root
        // and see if there department and course is the same if it is and then check there slots if they are the same ad penalty.
	    // then we do it with parent of initial node up until end. 
	    current = assignment;
		current2 = assignment.getParent(); 
		
		while(current != null && (current.getCourse() != null || current.getLab() != null))
		{
			current2 = current.getParent();
			while(current2 != null && (current2.getCourse() != null || current2.getLab() != null))   
			{
				if(current.getCourse() != null && current2.getCourse() != null)
				{
					if (current.getCourse().getDepartment().equals(current2.getCourse().getDepartment()) && current.getCourse().getCourseNum() == current2.getCourse().getCourseNum() && current.getSlot().equals(current2.getSlot()))
					{
						penalty = penalty + 100;
                        break;	// once we found pair of sections of the same course at the same time we stop and interate current 1 node up and current2 to be it's parent this should help to avoid repetitions   				
					}
				}
				else if(current.getCourse() != null && current2.getLab() != null) 			
				{
					if (current.getCourse().getDepartment().equals(current2.getLab().getDepartment()) && current.getCourse().getCourseNum() == current2.getLab().getCourseNum() && atTheSameTime(current, current2))
					{
						penalty = penalty + 100;
					     break;
					}
				}
				else if(current.getLab() != null && current2.getCourse() != null) 			
				{
					if (current.getLab().getDepartment().equals(current2.getCourse().getDepartment()) && current.getLab().getCourseNum() == current2.getCourse().getCourseNum() && atTheSameTime(current, current2))
					{
						penalty = penalty + 100;
					    break;
					}
				} 
				else if(current.getLab() != null && current2.getLab() != null) 			
				{
					if (current.getLab().getDepartment().equals(current2.getLab().getDepartment()) && current.getLab().getCourseNum() == current2.getLab().getCourseNum() && current.getSlot().equals( current2.getSlot()))
					{
						penalty = penalty + 100;
			            break;
					}
				}   
			     
			}
		    current = current.getParent();
		}	
		
		return penalty;
	}
	// this function should check if lab and course are in the same time, not sure how to do his need to do something like
	// 
	private boolean atTheSameTime(Node a, Node b)
	{
		if(a.getCourse() != null && b.getLab() != null)
		{
			if(a.getSlot().getDay() == Day.MONDAY && b.getSlot().getDay() == Day.MONDAY)
			{
				return (a.getSlot().equals(b.getSlot()));
			}
			else if(a.getSlot().getDay() == Day.TUESDAY && b.getSlot().getDay() ==Day.TUESDAY)
			{
				if(a.getSlot().getSlotTime() == b.getSlot().getSlotTime())
				{
					return true;
				}
			    if(a.getSlot().getSlotTime() + 100 == b .getSlot().getSlotTime())
			    {
			    	return true;
			    }
			    if(a.getSlot().getSlotTime() + 70 == b.getSlot().getSlotTime()) 
			    {
			    	return true;
			    }
			}
		
			else if(a.getSlot().getDay() == Day.MONDAY && b.getSlot().getDay() == Day.FRIDAY)
			{
				if(a.getSlot().getSlotTime() == b.getSlot().getSlotTime())
				{
				  return true;	
			    }
				if(a.getSlot().getSlotTime() - 100 == b.getSlot().getSlotTime())
				{
					return true;
				}
			}
		}
		else if(a.getLab()!= null &&b.getCourse()!= null)
		{
			if(a.getSlot().getDay() == Day.MONDAY && b.getSlot().getDay() == Day.MONDAY)
			{
				return (a.getSlot().equals(b.getSlot()));
			}
			else if(a.getSlot().getDay() == Day.TUESDAY && b.getSlot().getDay() ==Day.TUESDAY)
			{
				if(a.getSlot().getSlotTime() == b.getSlot().getSlotTime())
				{
					return true;
				}	
				if(a.getSlot().getSlotTime() - 100 == b .getSlot().getSlotTime())
				{
				    	return true;
				}
				if(a.getSlot().getSlotTime() - 70  == b.getSlot().getSlotTime()) 
				{
				    	return true;
				}
			
			}
			else if(a.getSlot().getDay() == Day.FRIDAY && b.getSlot().getDay() == Day.MONDAY)
			{
				if(a.getSlot().getSlotTime() == b.getSlot().getSlotTime())
				{
				  return true;	
			    }
				if(a.getSlot().getSlotTime() + 100 == b.getSlot().getSlotTime())
				{
					return true;
				}
			}
		
		}
		
		return false;
		
	}


	// don't worry about implementing this unless we have a bad runtime
	private int fBound(Node assignment) {
		// probably keep track of an average penalty per course/lab assignment
		// then just add (avg_penalty * num_courses/labs_unassigned)
		return 0;
	}
}