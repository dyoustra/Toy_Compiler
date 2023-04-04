package Parser;

import java.util.ArrayList;

public class Arguments extends Node {

    public ArrayList<Node> children;
    public Arguments() {
        super(Kind.ARGUMENTS);
        this.children = new ArrayList<>();
    }
}
