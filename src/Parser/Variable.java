package Parser;

public class Variable extends Node {

    public Node name; // identifier or dot
    public Node index; // expression, in brackets

    public Variable(Node name, Node index) {
        super(Kind.VARIABLE);
        this.name = name;
        this.index = index;
    }

    public Variable(Node name) {
        super(Kind.VARIABLE);
        this.name = name;
    }
}
