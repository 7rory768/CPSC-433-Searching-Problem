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
		Node current = assignment.getParent();
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

		if(assignedSlot == null)
			return false;
		
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
				if((c.getDepartment() == "CPSC" && c.getCourseNum() == 313))
					return false;
			}
			for(Lab l: parser.getLabs())
			{
				if((l.getDepartment() == "CPSC" && l.getCourseNum() == 313))
					return false;
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
				if((c.getDepartment() == "CPSC" && c.getCourseNum() == 413))
					return false;
			}
			for(Lab l: parser.getLabs())
			{
				if((l.getDepartment() == "CPSC" && l.getCourseNum() == 413))
					return false;
			}
		}

		while(current != null && (current.getCourse() != null || current.getLab() != null))
		{
			
			if(current.getCourse() != null)	{
				currentClass = current.getCourse();
			}
			else {
				currentClass = current.getLab();
			}

			if(current.getCourse() != null && assignedCourse != null
					&& current.getSlot() == assignedSlot 
					&& current.getCourse().getCourseNum() / 100 == 5 && assignedCourse.getCourseNum() / 100 == 5 
					&& current.getCourse().getDepartment().equals(assignedCourse.getDepartment())) {
				return false;
			}
			
			if(isOverlap(current, assignment))
			{
				if((current.getLab() != null && assignment.getCourse() != null))	{
					if(current.getLab().getDepartment().equals(assignment.getCourse().getDepartment()) 
							&& (current.getLab().getCourseNum() == assignment.getCourse().getCourseNum()))
					{
						if(current.getLab().getLectureNum() == 0)
							return false;
						if(current.getLab().getLectureNum() == assignment.getCourse().getLectureNum())
							return false;
					}
				}
				if((assignment.getLab() != null && current.getCourse() != null))
					if(assignment.getLab().getDepartment().equals(current.getCourse().getDepartment()) 
							&& (assignment.getLab().getCourseNum() == current.getCourse().getCourseNum()))
					{
						if(assignment.getLab().getLectureNum() == 0)
							return false;
						if(assignment.getLab().getLectureNum() == current.getCourse().getLectureNum())
							return false;
					}
				if( parser.areClassesIncompatible(currentClass, assignedClass)){	
					return false;
				}
			}


			if(current.getSlot() == assignedSlot)
				slotCount++;


			if(isCourse813)
			{
				if(isOverlap(assignment, current)) {
					if(currentClass.getDepartment() == "CPSC" && currentClass.getCourseNum() == 313)
						return false;
					for(ScheduledClass s: conflicts)
						if(parser.areClassesIncompatible(currentClass, s))
							return false;
				}
			}
			else if(isCourse913)
			{
				if(isOverlap(assignment, current)) {
					if(currentClass.getDepartment() == "CPSC" && currentClass.getCourseNum() == 413) 
						return false;
					for(ScheduledClass s: conflicts)
						if(parser.areClassesIncompatible(currentClass, s))
							return false;
				}
			}


			current = current.getParent();


		}

		if(slotCount > assignedSlot.getMaxCourses())
			return false; 

		return true;
	}


	private boolean isOverlap(Node a, Node b){


		if(a.getLab() != null && b.getCourse() != null)
		{
			if( ( (a.getSlot().getDay() == Day.MONDAY || a.getSlot().getDay() == Day.FRIDAY) && b.getSlot().getDay() == Day.MONDAY )
					|| (a.getSlot().getDay() == Day.TUESDAY  && b.getSlot().getDay() == Day.TUESDAY) ) 
			{
				if (a.getSlot().getSlotTime() == b.getSlot().getSlotTime())
					return true;

				if((a.getSlot().getSlotTime() < getEndTime(b.getSlot(), false) && getEndTime(b.getSlot(), false) - a.getSlot().getSlotTime() < 50 )|| 
						(b.getSlot().getSlotTime() < getEndTime(a.getSlot(), true) && getEndTime(a.getSlot(), true) - b.getSlot().getSlotTime() < 50)){
					return true;

				}	
			}
		}
		if(a.getCourse() != null && b.getLab() != null)
		{
			if( a.getSlot().getDay() == Day.MONDAY  && (b.getSlot().getDay() == Day.MONDAY || b.getSlot().getDay() == Day.FRIDAY) ||
					(a.getSlot().getDay() == Day.TUESDAY  && b.getSlot().getDay() == Day.TUESDAY)) 
			{
				if (a.getSlot().getSlotTime() == b.getSlot().getSlotTime())
					return true;

				if((a.getSlot().getSlotTime() < getEndTime(b.getSlot(), true) && getEndTime(b.getSlot(), true) - a.getSlot().getSlotTime() < 50 )|| 
						(b.getSlot().getSlotTime() < getEndTime(a.getSlot(), false) && getEndTime(a.getSlot(), false) - b.getSlot().getSlotTime() < 50)){

					return true;

				}
			}
		}

		if((a.getCourse() != null && b.getCourse() != null) || (a.getLab() != null && b.getLab() != null))
		{
			return (a.getSlot().getSlotTime() == b.getSlot().getSlotTime() && a.getSlot().getDay() == b.getSlot().getDay());
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

}