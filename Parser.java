import java.util.ArrayList;
import java.util.HashMap;

public ArrayList<Course> courses;
public HashMap<Course, ArrayList<Lab>> labs;    // correlate every Lab with it's Course
public ArrayList<Slot> slots;
public HashMap<Course, Course> notCompatible;
// there will be forced partial assignments? Theres none in the department examples



public class Parser{

    public void parseFiles(string fileName){
        // will put a bunch of objects into the ArrayLists above,
        // which will be accessed by SearchProblemMain after calling this function

    }
}