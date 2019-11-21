package searchproblem.classes;

public class ClassPreference {
	
	private final ScheduledClass scheduledClass;
	private final Slot slot;
	private final int weight;
	
	public ClassPreference(ScheduledClass scheduledClass, Slot slot, int weight) {
		this.scheduledClass = scheduledClass;
		this.slot = slot;
		this.weight = weight;
	}

	/**
	 * @return the scheduledClass
	 */
	public ScheduledClass getScheduledClass() {
		return scheduledClass;
	}

	/**
	 * @return the slot
	 */
	public Slot getSlot() {
		return slot;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}
	

}
