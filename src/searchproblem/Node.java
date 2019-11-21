package searchproblem;
import java.util.ArrayList;

import searchproblem.classes.Slot;

public class Node{
        Slot assigned_slot;
        ArrayList<Node> children;
        Node parent;    // needed to backtrack if all children violate hard constraints
    }