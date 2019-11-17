
public class SearchProblemMain{
    public static void main(string[] args){
        string fileName;
        if (args.length != 2){
            System.out.println("Improper usage, include the name of the file that holds the department instance");
            System.exit(1);
        } else {
            fileName = args[1];
        }

        Parser parser = new Parser();
        parser.parse(fileName);
        Solver solver = new Solver();
        solver.solve(); // pass all of the parser.Labs .. Courses .. Slots etc...
    }
}