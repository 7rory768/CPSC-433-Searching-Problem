
public class Solver{

    // gonna have to take a lot of params that the parser produces
    public void solve(){
        Node root = new Node();
        Node currentNode = root;
        int minPenalty = 0;

        ArrayList<Node> solution = new ArrayList<Node>();
        ArrayList<Node> bestSolution = new ArrayList<Node>();
        /*  this will use Evaluator, and Evaluator to build out the solution


        ----- DEPTH FIRST SEARCH FIRST ---- find any solution, then do breadth
        for every course:
            for every slot:
                if solution + this course to this slot is Validated add to solution
                    should probably split off into another function and recurse, and have it return
                    a boolean of whether this path is valid if not continue to next slot
                break
        
        ---- BREADTH SEARCH ---- use Eval in here
        for every course:
            for every slot:
                push Eval onto ordered list of best assignments
            search down the lowest penalty branch first .. aka assign it in solution
            again recursion probs best for this

        */
    }
}