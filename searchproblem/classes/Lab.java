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
    
}