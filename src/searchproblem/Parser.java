package searchproblem;

import searchproblem.classes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
	
	private final ArrayList<Course> courses = new ArrayList<Course>();
	private final ArrayList<Lab> labs = new ArrayList<Lab>();
	private final ArrayList<Slot> courseSlots = new ArrayList<Slot>(), labSlots = new ArrayList<Slot>();
	private final HashMap<ScheduledClass, ArrayList<ScheduledClass>> incompatibleClassPairs = new HashMap<>(), preferredClassPairs = new HashMap<>();
	private final ArrayList<ClassPreference> classPreferences = new ArrayList<ClassPreference>();
	private final HashMap<ScheduledClass, Slot> partialAssignments = new HashMap<>();
	private String name;
	private int numPairs = 0;
	private boolean does813Exist;
	private boolean does913Exist;
	
	public Parser(File file) {
		does813Exist = false;
		does913Exist = false;
		if (file.exists()) {
			try {
				Scanner scanner = new Scanner(file);
				ParserSection parserSection = ParserSection.NONE;
				int lineNum = 0;
				while (scanner.hasNextLine()) {
					lineNum++;
					String line = scanner.nextLine().trim();
					if (line.equals("")) {
						continue;
					}
					
					ParserSection parserSectionFound = ParserSection.getParserSection(line);
					if (parserSectionFound != null) {
						parserSection = parserSectionFound;
						continue;
					} else if (line.endsWith(":")) {
						System.out.println("ERROR: Unknown section: \"" + line + "\"");
					}
					
					if (parserSection == ParserSection.NAME) {
						this.name = line;
					} else if (parserSection == ParserSection.COURSE_SLOTS || parserSection == ParserSection.LAB_SLOTS) {
						String lineWithoutSpaces = line.replace(" ", "");
						String[] args = lineWithoutSpaces.split(",");
						Day day = Day.getDay(args[0]);
						int slotTime = Integer.valueOf(args[1].replace(":", "")), maximumCourses = Integer.valueOf(args[2]), minimumCourses = Integer.valueOf(args[3]);
						if (day != null) {
							if (parserSection == ParserSection.COURSE_SLOTS) {
								this.courseSlots.add(new Slot(day, slotTime, maximumCourses, minimumCourses));
							} else {
								this.labSlots.add(new Slot(day, slotTime, maximumCourses, minimumCourses));
							}
						}
					} else if (parserSection == ParserSection.COURSES) {
						this.courses.add(createCourse(line));
					} else if (parserSection == ParserSection.LABS) {
						this.labs.add(createLab(line));
					} else if (parserSection == ParserSection.NON_COMPATIBLES) {
						String[] args = line.split(",");
						String course1Text = args[0].trim(), course2Text = args[1].trim();
						ScheduledClass scheduledClass1 = getScheduledClass(course1Text), scheduledClass2 = getScheduledClass(course2Text);
						if (this.incompatibleClassPairs.get(scheduledClass1) == null) {
							this.incompatibleClassPairs.put(scheduledClass1, new ArrayList<ScheduledClass>());
						}
						
						this.incompatibleClassPairs.get(scheduledClass1).add(scheduledClass2);
						
						if (this.incompatibleClassPairs.get(scheduledClass2) == null) {
							this.incompatibleClassPairs.put(scheduledClass2, new ArrayList<ScheduledClass>());
						}
						
						this.incompatibleClassPairs.get(scheduledClass2).add(scheduledClass1);
					} else if (parserSection == ParserSection.UNWANTED) {
						String[] args = line.split(",");
						ScheduledClass scheduledClass = getScheduledClass(args[0]);
						Day day = Day.getDay(args[1].trim());
						int slotTime = Integer.valueOf(args[2].trim().replace(":", ""));
						if (scheduledClass instanceof Course) {
							// TODO: Unsure as to which is more efficient?
							//this.unwantedSlots.put(scheduledClass, getCourseSlot(day, slotTime));
							scheduledClass.addUnwantedSlot(getCourseSlot(day, slotTime));
						} else {
							// TODO: Unsure as to which is more efficient?
							//this.unwantedSlots.put(scheduledClass, getLabSlot(day, slotTime));
							scheduledClass.addUnwantedSlot(getLabSlot(day, slotTime));
						}
					} else if (parserSection == ParserSection.PREFERENCES) {
						String[] args = line.split(",");
						Day day = Day.getDay(args[0].trim());
						int slotTime = Integer.valueOf(args[1].trim().replace(":", ""));
						ScheduledClass scheduledClass = getScheduledClass(args[2].trim());
						Slot slot;
						if (scheduledClass instanceof Course) {
							slot = getCourseSlot(day, slotTime);
						} else {
							slot = getLabSlot(day, slotTime);
						}
						if (slot == null) {
							System.out.println("Warning: Unknown time slot: " + args[1] + "," + args[2] + "(Line " + lineNum + ")");
						}
						int weight = Integer.valueOf(args[3].trim());
						this.classPreferences.add(new ClassPreference(scheduledClass, slot, weight));
					} else if (parserSection == ParserSection.PAIRS) {
						String args[] = line.split(",");
						numPairs++;
						ScheduledClass scheduledClass1 = getScheduledClass(args[0].trim()), scheduledClass2 = getScheduledClass(args[1].trim());
						
						if (this.preferredClassPairs.get(scheduledClass1) == null) {
							this.preferredClassPairs.put(scheduledClass1, new ArrayList<ScheduledClass>());
						}
						
						this.preferredClassPairs.get(scheduledClass1).add(scheduledClass2);
						
						if (this.preferredClassPairs.get(scheduledClass2) == null) {
							this.preferredClassPairs.put(scheduledClass2, new ArrayList<ScheduledClass>());
						}
						
						this.preferredClassPairs.get(scheduledClass2).add(scheduledClass1);
					} else if (parserSection == ParserSection.PARTIAL_ASSIGNMENTS) {
						String args[] = line.split(",");
						ScheduledClass scheduledClass = this.getScheduledClass(args[0].trim());
						Day day = Day.getDay(args[1].trim());
						int slotTime = Integer.valueOf(args[2].trim().replace(":", ""));
						Slot slot;
						if (scheduledClass instanceof Lab) {
							slot = this.getLabSlot(day, slotTime);
						} else {
							slot = this.getCourseSlot(day, slotTime);
						}
						if (slot == null) {
							System.out.println("Error: Unknown time slot: " + args[1] + "," + args[2] + "(Line " + lineNum + ")");
							System.exit(0);
						}
						this.partialAssignments.put(scheduledClass, slot);
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getName() {
		return name;
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
	
	public boolean areClassesIncompatible(ScheduledClass class1, ScheduledClass class2) {
		return (this.incompatibleClassPairs.get(class1) != null && this.incompatibleClassPairs.get(class1).contains(class2)) ||
				(this.incompatibleClassPairs.get(class2) != null && this.incompatibleClassPairs.get(class2).contains(class1));
	}
	
	public int getNumIncompatabilities(ScheduledClass sc) {
		if(incompatibleClassPairs.containsKey(sc))
			return incompatibleClassPairs.get(sc).size();
		else return 0;
	}
	
	public boolean isAPreferredPair(ScheduledClass class1, ScheduledClass class2) {
		return this.preferredClassPairs.get(class1).contains(class2) || this.preferredClassPairs.get(class2).contains(class1);
	}
	
	public ClassPreference getClassPreference(ScheduledClass scheduledClass) {
		for (ClassPreference classPreference : this.classPreferences) {
			if (classPreference.getScheduledClass() == scheduledClass) {
				return classPreference;
			}
		}
		return null;
	}
	
	/**
	 * @return the classPreferences
	 */
	public ArrayList<ClassPreference> getClassPreferences() {
		return classPreferences;
	}
	
	public HashMap<ScheduledClass, ArrayList<ScheduledClass>> getClassPairs(){
		return preferredClassPairs;
	}
	
	private Lab createLab(String text) {
		String[] args = text.split("\\s+");
		String department = args[0];
		int courseNum = Integer.valueOf(args[1]);
		if (text.contains("LEC")) {
			int lectureNum = Integer.valueOf(args[3]);
			int tutorialNum = Integer.valueOf(args[5]);
			return new Lab(department, courseNum, lectureNum, tutorialNum);
		} else {
			int tutorialNum = Integer.valueOf(args[3]);
			return new Lab(department, courseNum, tutorialNum);
		}
	}
	
	private Course createCourse(String text) {
		String[] args = text.split("\\s+");
		String department = args[0];
		int courseNum = Integer.valueOf(args[1]), lectureNum = Integer.valueOf(args[3]);
		
		if (department.equals("CPSC") && courseNum == 313 && !does813Exist) {
			does813Exist = true;
			courses.add(new Course("CPSC", 813, 1));
			courseSlots.add(new Slot(Day.TUESDAY, 1800));
		} else if (department.equals("CPSC") && courseNum == 413 && !does913Exist) {
			does913Exist = true;
			courses.add(new Course("CPSC", 913, 1));
			courseSlots.add(new Slot(Day.TUESDAY, 1800));
		}
		return new Course(department, courseNum, lectureNum);
	}
	
	private ScheduledClass createScheduledClass(String text) {
		if (text.contains("TUT") || text.contains("LAB")) {
			return createLab(text);
		} else {
			return createCourse(text);
		}
	}
	
	public ScheduledClass getScheduledClass(String text) {
		ScheduledClass scheduledClass = createScheduledClass(text);
		if (scheduledClass instanceof Course) {
			for (Course course : this.courses) {
				if (course.equals(scheduledClass)) {
					return course;
				}
			}
		} else {
			for (Lab lab : this.labs) {
				if (lab.equals(scheduledClass)) {
					return lab;
				}
			}
		}
		return scheduledClass;
	}
	
	public Slot getCourseSlot(Day day, int slotTime) {
		for (Slot slot : this.courseSlots) {
			if (slot.getDay() == day && slot.getSlotTime() == slotTime) {
				return slot;
			}
		}
		return null;
	}
	
	public Slot getLabSlot(Day day, int slotTime) {
		for (Slot slot : this.labSlots) {
			if (slot.getDay() == day && slot.getSlotTime() == slotTime) {
				return slot;
			}
		}
		return null;
	}
	
	public Slot getPartialAssignment(ScheduledClass scheduledClass) {
		if (this.partialAssignments.containsKey(scheduledClass)) {
			return this.partialAssignments.get(scheduledClass);
		}
		return null;
	}
	
	public HashMap<ScheduledClass, Slot> getPartialAssignments(){
		return partialAssignments;
	}
	
	public int getNumPairs() {
		return numPairs;
	}
	
}