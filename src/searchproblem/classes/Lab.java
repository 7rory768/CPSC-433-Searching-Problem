package searchproblem.classes;

public class Lab extends ScheduledClass {
	
	private final int tutorialNum;
	
    public Lab(String department, int courseNum, int lectureNum, int tutorialNum) {
		super(department, courseNum, lectureNum);
		this.tutorialNum = tutorialNum;
	}
	
    // Some Labs dont have any specific lecture tied to them
    public Lab(String department, int courseNum, int tutorialNum) {
		super(department, courseNum, 0);
		this.tutorialNum = tutorialNum;
	}

	/**
	 * @return the tutorialNum
	 */
	public int getTutorialNum() {
		return tutorialNum;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		
		if (!(o instanceof Lab)) return false;
		
		Lab lab = (Lab) o;
		if (lab.getDepartment().equals(this.getDepartment()) && lab.getCourseNum() == this.getCourseNum() && lab.getLectureNum() == this.getLectureNum() && lab.getTutorialNum() == this.getTutorialNum()) return true;
		
		return false;
	}
    
}