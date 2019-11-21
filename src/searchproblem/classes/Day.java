package searchproblem.classes;

public enum Day {
	
	MONDAY("MO"),
	TUESDAY("TU"),
	FRIDAY("FR");
	
	private final String dayID;
	
	Day(String dayID) { this.dayID = dayID; }
	
	public static Day getDay(String dayID) {
		for (Day day : values()) {
			if (day.dayID.equalsIgnoreCase(dayID)) {
				return day;
			}
		}
		return null;
	}
	

}
