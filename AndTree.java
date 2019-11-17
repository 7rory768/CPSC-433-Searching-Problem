import java.util.ArrayList;

public class AndTree{
    public Node root;

    private class Node{
        Slot assigned_slot;
        SlotItem assigned;
        ArrayList<Node> children;
        Node parent;    // needed to backtrack if all children violate hard constraints
    }
}