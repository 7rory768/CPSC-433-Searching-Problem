package searchproblem;

public class SearchProblemMain{
    public static void main(String[] args){
        String fileName;
        if (args.length != 2){
            System.out.println("Improper usage, include the name of the file that holds the department instance");
        } else {
            fileName = args[1];
            Parser parser = new Parser(fileName);
            Solver solver = new Solver(parser);
            solver.solve(); // pass all of the parser.Labs .. Courses .. Slots etc...
        }
    }
}