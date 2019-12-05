package searchproblem.classes;

import java.util.ArrayList;

import searchproblem.Node;

public class Slot implements Comparable<Slot> {

    private final Day day;
    private final int slotTime; // Time represented by removing the : from the string (ex. 13:30 = 1330);
    private final int minCourses;
    public int maxCourses;
    public ArrayList<ScheduledClass> scheduled = new ArrayList<>();
    
    public Slot(Day day, int slotTime, int maxCourses, int minCourses) {
    	this.day = day;
    	this.slotTime = slotTime;
    	this.minCourses = minCourses;
    	this.maxCourses = maxCourses;
    }
	
	public Slot(Day day, int slotTime) {
		this(day, slotTime, 0, Integer.MAX_VALUE);
	}
	
	public Day getDay() {
		return day;
	}

	public int getSlotTime() {
		return slotTime;
	}

	public int getMinCourses() {
		return minCourses;
	}


	public int getMaxCourses() {
		return maxCourses;
	}
	
	public int compareTo(Slot other_slot) {
		if(this.scheduled.size() - this.minCourses == other_slot.scheduled.size() - other_slot.minCourses)
			return 0;
		else if (this.scheduled.size() - this.minCourses > other_slot.scheduled.size() - other_slot.minCourses)
			return 1;
		else
			return -1;
	}
    
    
}