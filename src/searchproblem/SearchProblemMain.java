package searchproblem;

import java.io.File;

public class SearchProblemMain {
	public static void main(String[] args) {
		String fileName;
/*        if (args.length != 2){
            System.out.println("Improper usage, include the name of the file that holds the department instance");
        } else {
		fileName = args[1];*/
		fileName = "deptinst1.txt";
		File file = new File(fileName);
		Parser parser = new Parser(file);
		Solver solver = new Solver(parser);
		solver.solve();
//        }
	}
}