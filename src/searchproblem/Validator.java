package searchproblem;

import searchproblem.classes.*;
import java.util.*;

public class Validator{
	private final Parser parser;

	public Validator(Parser parser)
	{
		this.parser = parser;
	}
	public boolean validate(Node assignment){
		int slotCount = 1;
		Node current = assignment.parent;
		Slot assignedSlot = assignment.getSlot();
		Course assignedCourse = assignment.getCourse();
		Lab assignedLab = assignment.getLab();
		ArrayList<ScheduledClass> conflicts = new ArrayList<>();
		boolean isCourse813 = (assignedCourse != null && assignedCourse.getDepartment().equals("CPSC") && assignedCourse.getCourseNum() == 813);
		boolean isCourse913 = (assignedCourse != null && assignedCourse.getDepartment().equals("CPSC") && assignedCourse.getCourseNum() == 913);

		ScheduledClass currentClass;
		ScheduledClass assignedClass;

		if(assignedCourse != null)
			assignedClass = assignedCourse;
		else
			assignedClass = assignedLab;

		if(assignedCourse != null && assignedSlot.getSlotTime() == 1100 && assignedSlot.getDay() == Day.TUESDAY)	{
			return false;
		}
		if(assignedClass.getLectureNum()/10 == 9 && assignedSlot.getSlotTime() < 1800)
			return false;

		if(assignedClass.getUnwantedSlots().contains(assignedSlot))
			return false;

		if(isCourse813)
		{
			if(!(assignedSlot.getDay() == Day.TUESDAY && assignedSlot.getSlotTime() >= 1800))
			{
				return false;
			}
			for(Course c: parser.getCourses())
			{
				if(c.getDepartment() == "CPSC" && c.getCourseNum() == 313)
					conflicts.add(c);
			}
			for(Lab l: parser.getLabs())
			{
				if(l.getDepartment() == "CPSC" && l.getCourseNum() == 313)
					conflicts.add(l);
			}
		}

		if(isCourse913)
		{
			if(!(assignedSlot.getDay() == Day.TUESDAY && assignedSlot.getSlotTime() >= 1800))
			{
				return false;
			}
			for(Course c: parser.getCourses())
			{
				if(c.getDepartment() == "CPSC" && c.getCourseNum() == 413)
					conflicts.add(c);
			}
			for(Lab l: parser.getLabs())
			{
				if(l.getDepartment() == "CPSC" && l.getCourseNum() == 413)
					conflicts.add(l);
			}

		}

		while(current != null && (current.getCourse() != null || current.getLab() != null))
		{

			if(current.getCourse() != null && assignedCourse != null
					&& current.getSlot() == assignedSlot 
					&& current.getCourse().getCourseNum() / 100 == 5 && assignedCourse.getCourseNum() / 100 == 5 
					&& current.getCourse().getDepartment().equals(assignedCourse.getDepartment())) {
				return false;
			}

			if(isOverlap(current, assignment))
				return false;

			if(current.getSlot() == assignedSlot)
				slotCount++;

			if(current.getCourse() != null)	{
				currentClass = current.getCourse();
			}
			else {
				currentClass = current.getLab();
			}

			if(isCourse813)
			{
				if(current.getSlot().getSlotTime() == assignedSlot.getSlotTime() && current.getSlot().getDay() == Day.TUESDAY) {
					if(currentClass.getDepartment() == "CPSC" && currentClass.getCourseNum() == 313)
						return false;
					for(ScheduledClass s: conflicts)
						if(parser.areClassesIncompatible(currentClass, s))
							return false;
				}
			}
			else if(isCourse913)
			{
				if(current.getSlot().getSlotTime() == assignedSlot.getSlotTime() && current.getSlot().getDay() == Day.TUESDAY) {
					if(currentClass.getDepartment() == "CPSC" && currentClass.getCourseNum() == 413) 
						return false;
					for(ScheduledClass s: conflicts)
						if(parser.areClassesIncompatible(currentClass, s))
							return false;
				}
			}

			if( isSameDay(current, assignment)
					&& current.getSlot().getSlotTime() == assignedSlot.getSlotTime()
					&& ( parser.areClassesIncompatible(currentClass, assignedClass))){	
				return false;
			}
			current = current.parent;


		}
		if(slotCount > assignedSlot.getMaxCourses())
			return false; 

		return true;
	}

	private boolean isOverlap(Node a, Node b){

		if(a.getLab() != null && b.getCourse() != null)	{
			if(a.getLab().getDepartment().equals(b.getCourse().getDepartment()) 
					&& (a.getLab().getCourseNum() == b.getCourse().getCourseNum())
					&& (a.getSlot().getSlotTime() == b.getSlot().getSlotTime()))
			{
				if( (a.getSlot().getDay() == Day.MONDAY || a.getSlot().getDay() == Day.FRIDAY) && b.getSlot().getDay() == Day.MONDAY ||
						(a.getSlot().getDay() == Day.TUESDAY ) && b.getSlot().getDay() == Day.TUESDAY) 
				{
					if(a.getSlot().getSlotTime() < getEndTime(b.getSlot(), true) || b.getSlot().getSlotTime() < getEndTime(a.getSlot(), false)){
						if(a.getLab().getLectureNum() == 0)
							return true;
						else if(a.getLab().getLectureNum() == b.getCourse().getLectureNum())
							return true;

					}	
				}

			}
		}
		else if(a.getCourse() != null && b.getLab() != null)
		{
			if(b.getLab().getDepartment().equals(a.getCourse().getDepartment()) 
					&& (b.getLab().getCourseNum() == a.getCourse().getCourseNum())
					&& (b.getSlot().getSlotTime() == a.getSlot().getSlotTime()))
			{
				if(a.getSlot().getSlotTime() < getEndTime(b.getSlot(), false) || b.getSlot().getSlotTime() < getEndTime(a.getSlot(), true))
				{
					if(b.getLab().getLectureNum() == 0)
						return true;
					else if(b.getLab().getLectureNum() == a.getCourse().getLectureNum())
						return true;
				}	
			}
		}

		return false;
	}

	private int getEndTime(Slot a, boolean aLab)
	{
		int aEndTime = a.getSlotTime();

		if(aEndTime%100 == 30)
			aEndTime += 20;

		switch(a.getDay())
		{
		case MONDAY:
			aEndTime += 100;
			break;
		case TUESDAY:
			if(aLab)
				aEndTime += 100;
			else
				aEndTime += 150;

			break;
		case FRIDAY:
			if(aLab)
				aEndTime += 200;
			else
				aEndTime += 100;
			break;
		default:
			break;
		}

		if(aEndTime%100 == 50)
			aEndTime -= 20;

		return aEndTime;
	}

	private boolean isSameDay(Node a, Node b)
	{
		if(a.getCourse() != null && b.getCourse() != null){
			return a.getSlot().getDay() == b.getSlot().getDay();
		}
		else if(a.getLab() != null && b.getLab() != null){
			return a.getSlot().getDay() == b.getSlot().getDay();
		}
		else if(a.getCourse() != null && b.getLab() != null){
			return b.getSlot().getDay() == a.getSlot().getDay() || (b.getSlot().getDay() == Day.FRIDAY && a.getSlot().getDay() == Day.MONDAY);
		}
		else if(a.getLab() != null && b.getCourse() != null){
			return a.getSlot().getDay() == b.getSlot().getDay() || (a.getSlot().getDay() == Day.FRIDAY && b.getSlot().getDay() == Day.MONDAY);
		}
		else{
			return false;
		}
	}
}