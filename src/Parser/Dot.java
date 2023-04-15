package Parser;

public class Dot extends Node {

    public Identifier left;
    public Node right; // could be dot or identifier

    public Dot() {
        super(Kind.DOT);
    }
}
