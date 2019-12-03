package searchproblem;

import java.io.File;

public class SearchProblemMain {
	public static void main(String[] args) {
		/*
		String fileName = "";
		int minfillCoursePen = 1, minfillTutPen = 1, secOverlapPen = 1, notPairedPen = 1;
		float weightMinfill = 1.0f, weightPref = 1.0f, weightPair = 1.0f, weightSecOverlap = 1.0f;
		
		
        if (args.length != 9){
        	System.out.println("ONLY: " + args.length + " PROVIDED");
            System.out.println("Improper usage, run with: java   input_file_name   minimum_courses_pen(int)   minimum_tut/lab_pen(int)   sections_overlapping_pen(int)   not_paired_pen(int)" +
            "   weight_min_filled(float)   weight_preferred(float)   weight_paired(float)   weight_sections_overlap(float)");
            System.exit(1);
        }
        try {
			fileName = args[0];
			minfillCoursePen = Integer.parseInt(args[1]);
			minfillTutPen = Integer.parseInt(args[2]);
			secOverlapPen = Integer.parseInt(args[3]);
			notPairedPen = Integer.parseInt(args[4]);
	
			weightMinfill = Float.parseFloat(args[5]);
			weightPref = Float.parseFloat(args[6]);
			weightPair = Float.parseFloat(args[7]);
			weightSecOverlap = Float.parseFloat(args[8]);
        } catch(Exception e) {
        	System.out.println("Trouble parsing command line arguments, check the README for usage.");
        	System.exit(1);
        }
		*/
		
		
		// -----------------------------------  HARD CODE -----------------------------------
		String fileName = "shortSampleFixed.txt";
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
		
		Evaluator evaluator = new Evaluator(parser, minfillTutPen, minfillCoursePen,  secOverlapPen, notPairedPen, weightMinfill, weightPref, weightPair, weightSecOverlap);
		Solver solver = new Solver(parser, evaluator);
		solver.solve();
	}
}