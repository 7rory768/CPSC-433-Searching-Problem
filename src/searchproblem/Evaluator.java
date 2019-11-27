package searchproblem;

public class Evaluator {
	Parser parser;
	public Evaluator(Parser p) {
		this.parser = p;
	}
	public int evaluate(Node assignment) {
		// return the penalty for this assignment

		return 0;
	}

	// don't worry about implementing this unless we have a bad runtime
	private int fBound(Node assignment) {
		// probably keep track of an average penalty per course/lab assignment
		// then just add (avg_penalty * num_courses/labs_unassigned)
		return 0;
	}
}