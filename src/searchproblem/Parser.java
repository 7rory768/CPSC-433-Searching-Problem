package searchproblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import searchproblem.classes.*;

public class Parser {

	private final ArrayList<Course> courses = new ArrayList<Course>();
	private final ArrayList<Lab> labs = new ArrayList<Lab>();
	private final ArrayList<Slot> courseSlots = new ArrayList<Slot>(), labSlots = new ArrayList<Slot>();
	private final HashMap<ScheduledClass, ScheduledClass> incompatibleClassPairs = new HashMap<>(), preferredClassPairs = new HashMap<>();
	private final ArrayList<ClassPreference> classPreferences = new ArrayList<ClassPreference>();
	// TODO: Forced partial assignments ?
	
	public Parser(File file) {
		if (file.exists()) {
			try {
				Scanner scanner = new Scanner(file);
				ParserSection parserSection = ParserSection.NONE;
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine().trim();
					if (line.trim().equals("")) parserSection = ParserSection.NONE;
					
					if (line.equalsIgnoreCase("Name:")) {
						parserSection = ParserSection.NAME;
					} else if (line.equalsIgnoreCase("Course slots:")) {
						parserSection = ParserSection.COURSE_SLOTS;
					} else if (line.equalsIgnoreCase("Lab slots:")) {
						parserSection = ParserSection.LAB_SLOTS;
					} else if (line.equalsIgnoreCase("Courses:")) {
						parserSection = ParserSection.COURSES;
					} else if (line.equalsIgnoreCase("Labs")) {
						parserSection = ParserSection.LABS;
					} else if (line.equalsIgnoreCase("Not compatible:")) {
						parserSection = ParserSection.NON_COMPATIBLES;
					} else if (line.equalsIgnoreCase("Unwanted:")) {
						parserSection = ParserSection.UNWANTED;
					} else if (line.equalsIgnoreCase("Preferences")) {
						parserSection = ParserSection.PREFERENCES;
					} else if (line.equalsIgnoreCase("Pair:")) {
						parserSection = ParserSection.PAIRS;
					} else if (line.contains(":")) {
						System.out.println("ERROR: Unknown section: \"" + line + "\"");
					}
					
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the courses
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}

	/**
	 * @return the labs
	 */
	public ArrayList<Lab> getLabs() {
		return labs;
	}

	/**
	 * @return the courseSlots
	 */
	public ArrayList<Slot> getCourseSlots() {
		return courseSlots;
	}

	/**
	 * @return the labSlots
	 */
	public ArrayList<Slot> getLabSlots() {
		return labSlots;
	}
	
	public boolean areClassesImcompatible(ScheduledClass class1, ScheduledClass class2) {
		return this.incompatibleClassPairs.get(class1) == class2 || this.incompatibleClassPairs.get(class2) == class1;
	}
	
	/**
	 * @return the classPreferences
	 */
	public ArrayList<ClassPreference> getClassPreferences() {
		return classPreferences;
	}
	
	
}