package searchproblem;

import java.util.ArrayList;

import searchproblem.classes.*;

public class Parser {

	private final ArrayList<Course> courses = new ArrayList<Course>();
	private final ArrayList<Lab> labs = new ArrayList<Lab>();
	private final ArrayList<Slot> courseSlots = new ArrayList<Slot>(), labSlots = new ArrayList<Slot>();
	private final ArrayList<ClassPair> incompatibleClassPairs = new ArrayList<ClassPair>(), preferredClassPairs = new ArrayList<ClassPair>();
	private final ArrayList<ClassPreference> classPreferences = new ArrayList<ClassPreference>();
	// TODO: Forced partial assignments ?

	public Parser(String fileName) {
		// TODO: Parse file;
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

	/**
	 * @return the incompatibleClassPairs
	 */
	public ArrayList<ClassPair> getIncompatibleClassPairs() {
		return incompatibleClassPairs;
	}

	/**
	 * @return the preferredClassPairs
	 */
	public ArrayList<ClassPair> getPreferredClassPairs() {
		return preferredClassPairs;
	}

	/**
	 * @return the classPreferences
	 */
	public ArrayList<ClassPreference> getClassPreferences() {
		return classPreferences;
	}
	
	
}