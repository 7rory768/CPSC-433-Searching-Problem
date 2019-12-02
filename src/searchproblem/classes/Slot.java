package searchproblem.classes;

import java.util.ArrayList;

public class Slot {

    private final Day day;
    private final int slotTime; // Time represented by removing the : from the string (ex. 13:30 = 1330);
    private int currentAssigned;
    private final int minCourses;
    private final int maxCourses;
    public ArrayList<ScheduledClass> scheduled = new ArrayList<>();
    
    public Slot(Day day, int slotTime, int maxCourses, int minCourses) {
    	this.day = day;
    	this.slotTime = slotTime;
    	this.minCourses = minCourses;
    	this.maxCourses = maxCourses;
    	this.currentAssigned = 0;
    }
	
	public Slot(Day day, int slotTime) {
		this(day, slotTime, 0, Integer.MAX_VALUE);
	}
	
	public int getCurrentAssigned() {
		return currentAssigned;
	}
	
	public void incCurrAssigned() {
		currentAssigned++;
	}
	
	public void decCurrAssigned() {
		currentAssigned--;
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
    
    
}