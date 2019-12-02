package searchproblem;

import java.io.File;

public class SearchProblemMain {
	public static void main(String[] args) {
		
/*        if (args.length != 10){
            System.out.println("Improper usage, run with: java   input_file_name   minimum_courses_pen(int)   minimum_tut/lab_pen(int)   sections_overlapping_pen(int)   not_paired_pen(int)" +
            "   weight_min_filled(float)   weight_preferred(float)   weight_paired(float)   weight_sections_overlap(float)");
            System.exit(1);
        }
		String fileName = args[1];
		int minfillCoursePen = Integer.parseInt(args[2]);
		int minfillTutPen = Integer.parseInt(args[3]);
		int secOverlapPen = Integer.parseInt(args[4]);
		int notPairedPen = Integer.parseInt(args[5]);

		float weightMinfill = Float.parseFloat(args[6]);
		float weightPref = Float.parseFloat(args[7]);
		float weightPair = Float.parseFloat(args[8]);
		float weightSecOverlap = Float.parseFloat(args[9]);*/
		
		
		
		// ----------------------------------- TEMPORARILY HARD CODE -----------------------------------
		String fileName = "partialAssignments.txt";
		int minfillCoursePen = 1;
		int minfillTutPen = 1;
		int secOverlapPen = 1;
		int notPairedPen = 1;

		float weightMinfill = 1.0f;
		float weightPref = 1.0f;
		float weightPair = 1.0f;
		float weightSecOverlap = 1.0f;	
		
		
		
		File file = new File(fileName);
		Parser parser = new Parser(file);
		
		Evaluator evaluator = new Evaluator(parser, minfillCoursePen, minfillTutPen, secOverlapPen, notPairedPen, weightMinfill, weightPref, weightPair, weightSecOverlap);
		Solver solver = new Solver(parser, evaluator);
		solver.solve();
	}
}