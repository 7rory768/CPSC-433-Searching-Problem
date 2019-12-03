package searchproblem;
import java.util.ArrayList;

import searchproblem.classes.*;

public class Node implements Comparable<Node>{
	private Slot assigned_slot;
	private Course course;	//NULL if a lab is scheduled
	private Lab lab;		//NULL if a course is scheduled
	private int totalPenalty;	//total penalty for the schedule at this point
	private int preferencePenalty; //penalty from the course in this node assigned to the slot in this node
	private ArrayList<Node> children;
	private Node parent;    // needed to backtrack if all children violate hard constraints
	
	// public for now just for easier prototyping
	public float subPen;
	public float basePen;

	public Node(){
		this.assigned_slot = null;
		this.course = null;
		this.lab = null;
		this.children = new ArrayList<Node>();
		this.parent = null;
		this.totalPenalty = 0;
		this.preferencePenalty = 0;
	}
	
	public Node(Slot assigned_slot, ScheduledClass sc, Node parent){
		this.assigned_slot = assigned_slot;
		if (sc instanceof Course) {
			this.course = (Course) sc;
			this.lab =  null;
		} else {
			this.lab = (Lab) sc;
			this.course = null;
		}
		this.parent = parent;
		this.children = new ArrayList<Node>();
		this.totalPenalty = 0;
		this.preferencePenalty = 0;
	}

	public Node(Slot assigned_slot, Course course, Node parent){
		this.assigned_slot = assigned_slot;
		this.course = course;
		this.lab = null;
		this.parent = parent;
		this.children = new ArrayList<Node>();
		this.totalPenalty = 0;
		this.preferencePenalty = 0;
	}

	public Node(Slot assigned_slot, Lab lab, Node parent){
		this.assigned_slot = assigned_slot;
		this.course = null;
		this.lab = lab;
		this.parent = parent;
		this.children = new ArrayList<Node>();
		this.totalPenalty = 0;
		this.preferencePenalty = 0;
	}
	
	public ScheduledClass getAssigned() {
		if(this.course == null) {
			return this.getLab();
		} else {
			return this.getCourse();
		}
	}

	//   	GETTERS BEGIN
	public Slot getSlot() {
		return assigned_slot;
	}

	//Will return null if a course was assigned
	public Lab getLab() {
		return lab;
	}

	//Will return null if a lab was assigned
	public Course getCourse() {
		return course;
	}
	public ArrayList<Node> getChildren(){
		return children;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public int getTotalPenalty() {
		return totalPenalty;
	}
	
	public int getPreferencePenalty() {
		return preferencePenalty;
	}


	//		GETTERS END

	
	//			SETTERS BEGIN
	public void setPreferencePenalty(int preferencePenalty) {
		this.preferencePenalty = preferencePenalty;
	}

	public void setTotalPenalty(int totalPenalty) {
		this.totalPenalty = totalPenalty;
	}
	//			SETTERS END

	/**
	 * 	@author Kyle Perry
	 * 	
	 * 	Compares 2 nodes by the penalty on them:
	 * 		1 if the penalty of the current node is greater than the penalty of the argument
	 * 		0 if the penalty of the two nodes is equal
	 * 	   -1 if the penalty of the current node is lower than the penalty of the argument
	 *  	(Allows us to use Collections.sort() for Nodes to sort them by penalty)
	 */
	public int compareTo(Node other_assignment)
	{
		if(this.totalPenalty == other_assignment.totalPenalty)
			return 0;
		else if (this.totalPenalty > other_assignment.totalPenalty)
			return 1;
		else
			return -1;
	}


	/**
	 *  @author Kyle Perry
	 * 
	 * 	@param courses - A list of all courses to be scheduled
	 * 	@param labs	- A list of all labs to be scheduled
	 * 	@param course_slots	-	A list of all possible course time slots
	 * 	@param lab_slots	-	A list of all possible lab time slots
	 * 	
	 * 	Begin by creating 2 lists for unassigned Courses and labs, then remove previously assigned courses and labs from the newly created lists 
	 * 	(look through this node and all it's parents and remove the course or lab from the corresponding list)
	 * 
	 * 	Add each possible assignment of a lab to a lab slot to the list of children, the do the same for each course
	 *
	 *	NOTE: This function does not ensure valid Course and Lab assignments, but it will only assign Labs to Lab Slots and Courses to Course Slots!
	 *		  Other hard constraints (being over max number of assigned Courses/Labs to a Slot, for example) will still need to be checked!
	 *	
	 *	ADDITIONALLY: This function does not evaluate any generated nodes, that will also have to be done after
	 */
	public void generateChildren(ArrayList<Course> courses, ArrayList<Lab> labs, ArrayList<Slot> course_slots, ArrayList<Slot> lab_slots)
	{
		ArrayList<Lab> unassigned_labs = new ArrayList<Lab>(labs);
		ArrayList<Course> unassigned_courses = new ArrayList<Course>(courses);

		Node current = this;

		while(current != null && (current.getCourse() != null || current.getLab() != null))
		{
			if(current.getCourse() == null) {
				unassigned_labs.remove(current.getLab());
			}
			else {
				unassigned_courses.remove(current.getCourse());
			}
			current = current.parent;
			
		}

		
		for(Course uC: unassigned_courses)
		{
			for(Slot cS: course_slots)
			{

				this.children.add(new Node(cS, uC, this));
			}
		}
		
		for(Lab uL: unassigned_labs)
		{
			for(Slot lS: lab_slots)
			{

				this.children.add(new Node(lS, uL, this));
			}
		}


	}

	public String toString() {
		String s1 = "";
		String s2 = "";
		String s3 = "";
		if(this.course != null) {
			s1 += this.course.getDepartment() + " " + this.course.getCourseNum() + " LEC " + this.course.getLectureNum();
			s2 += ": " + this.assigned_slot.getDay().getDayID() + ", ";
			s3 += this.assigned_slot.getSlotTime()/100 + ":" + this.assigned_slot.getSlotTime()%100/10 + 0;
		} else if (this.lab != null) {
			s1 += this.lab.getDepartment() + " " + this.lab.getCourseNum() + " LEC " + this.lab.getLectureNum() + " TUT " + this.lab.getTutorialNum();
			s2 += ": " + this.assigned_slot.getDay().getDayID() + ", ";
			s3 += this.assigned_slot.getSlotTime()/100 + ":" + this.assigned_slot.getSlotTime()%100/10 + 0;
		}
		return String.format("%-30s %-4s %6s", s1, s2, s3);
	}
}