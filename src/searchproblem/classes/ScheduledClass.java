package searchproblem.classes;

import java.util.ArrayList;

public abstract class ScheduledClass {

	private final String department;
	private final int courseNum;
	private final int lectureNum;
	private final ArrayList<Slot> unwantedSlots = new ArrayList<Slot>();

	public ScheduledClass(String department, int courseNum, int lectureNum) {
		this.department = department;
		this.courseNum = courseNum;
		this.lectureNum = lectureNum;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @return the courseNum
	 */
	public int getCourseNum() {
		return courseNum;
	}

	/**
	 * @return the lectureNum
	 */
	public int getLectureNum() {
		return lectureNum;
	}

	/**
	 * @return the unwantedSlots
	 */
	public ArrayList<Slot> getUnwantedSlots() {
		return unwantedSlots;
	}
	
	public void addUnwantedSlot(Slot slot) {
		this.unwantedSlots.add(slot);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		
		if (!(o instanceof ScheduledClass)) return false;
		
		ScheduledClass scheduledClass = (ScheduledClass) o;
		if (scheduledClass.getDepartment().equals(this.getDepartment()) && scheduledClass.getCourseNum() == this.getCourseNum() && scheduledClass.getLectureNum() == this.getLectureNum()) return true;
		
		return false;
	}

}
