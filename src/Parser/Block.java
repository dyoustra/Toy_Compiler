package Parser;

import java.util.ArrayList;

public class Block extends Node {

    public ArrayList<Node> statements;

    public Block() {
        this.statements = new ArrayList<Node>();
    }

    public Block(ArrayList<Node> statements) {
        this.statements = statements;
    }
}
