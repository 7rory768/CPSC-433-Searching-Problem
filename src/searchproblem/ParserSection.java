package searchproblem;

public enum ParserSection {

	NONE(""),
	NAME("Name:"),
	COURSE_SLOTS("Course slots:"),
	LAB_SLOTS("Lab slots:"),
	COURSES("Courses:"),
	LABS("Labs:"),
	NON_COMPATIBLES("Not compatible:"),
	UNWANTED("Unwanted:"),
	PREFERENCES("Preferences:"),
	PAIRS("Pair:"),
	PARTIAL_ASSIGNMENTS("Partial assignments:");

	 private final String sectionMarker;
	 
	 ParserSection(String sectionMarker) { this.sectionMarker = sectionMarker; }
	 
	 public static ParserSection getParserSection(String sectionMarker) {
	 	for (ParserSection parserSection : values()) {
	 		if (parserSection.sectionMarker.equalsIgnoreCase(sectionMarker)) {
	 			return parserSection;
		    }
	    }
	 	
	 	return null;
	 }
	
}
