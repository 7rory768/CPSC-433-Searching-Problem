
public class Course extends SlotItem{
    public string department;
    public int courseNum;
    public int lectureNum;

    public Course(string dep, int cNum, int lNum){
        this.department = dep;
        this.courseNum = cNum;
        this.lectureNum = lNum;
    }
}