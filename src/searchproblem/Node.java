package searchproblem;
import java.util.ArrayList;

import searchproblem.classes.*;

public class Node implements Comparable<Node>{
        private Slot assigned_slot;
        private Course course = null;	//NULL if a lab is scheduled
        private Lab lab = null;		//NULL if a course is scheduled
        public int penalty;	//Penalty accrued from this course/lab assigned to assigned_slot
        public  ArrayList<Node> children;
        public Node parent;    // needed to backtrack if all children violate hard constraints
        
        public Node(Slot assigned_slot, Course course, Node parent){
        	this.assigned_slot = assigned_slot;
        	this.course = course;
        	this.parent = parent;
        	this.children = new ArrayList<Node>();
        	this.penalty = 0;
        	}
        
        public Node(Slot assigned_slot, Lab lab, Node parent){
        	this.assigned_slot = assigned_slot;
        	this.lab = lab;
        	this.parent = parent;
        	this.children = new ArrayList<Node>();
        	this.penalty = 0;
        }
        
        /*
        	GETTERS BEGIN
        */
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
        /*
        	GETTERS END
        */
        
        public int compareTo(Node other_assignment)
        {
        	if(this.penalty == other_assignment.penalty)
        		return 0;
        	else if (this.penalty > other_assignment.penalty)
        		return 1;
        	else
        		return -1;
        }
        
        public void generateChildren(ArrayList<Course> unassigned_courses, ArrayList<Lab> unassigned_labs, ArrayList<Slot> slots)
        {
        	//Using parent, gather list of previous assignments
        	//For each course and lab in given lists, if it wasn't assigned before (in our lists we just generated, assign it to each slot of its type in a new node
        	//add these new nodes to the children
        	
        	
        }
    }