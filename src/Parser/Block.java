package Parser;

import java.util.ArrayList;

public class Block extends Node {

    public ArrayList<Node> statements;

    public Block(ArrayList<Node> statements) {
        super(Kind.BLOCK);
        this.statements = statements;
    }
}
