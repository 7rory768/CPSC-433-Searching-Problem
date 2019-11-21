package searchproblem.classes;

public class Slot {

    private final Day day;
    private final int slotTime; // Time represented by removing the : from the string (ex. 13:30 = 1330);
    private final int minCourses;
    private final int maxCourses;
    
    public Slot(Day day, int slotTime, int minCourses, int maxCourses) {
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
    
    
}