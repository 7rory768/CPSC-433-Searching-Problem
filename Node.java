import java.util.ArrayList;

public class Node{
        Slot assigned_slot;
        SlotItem assigned;
        ArrayList<Node> children;
        Node parent;    // needed to backtrack if all children violate hard constraints
    }
}