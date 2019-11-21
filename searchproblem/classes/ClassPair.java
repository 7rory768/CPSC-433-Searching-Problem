package searchproblem.classes;

public class ClassPair {
	
	private final ScheduledClass scheduledClass1, scheduledClass2;
	
	public ClassPair(ScheduledClass scheduledClass1, ScheduledClass scheduledClass2) {
		this.scheduledClass1 = scheduledClass1;
		this.scheduledClass2 = scheduledClass2;
	}

	/**
	 * @return the scheduledClass1
	 */
	public ScheduledClass getScheduledClass1() {
		return scheduledClass1;
	}

	/**
	 * @return the scheduledClass2
	 */
	public ScheduledClass getScheduledClass2() {
		return scheduledClass2;
	}

}
