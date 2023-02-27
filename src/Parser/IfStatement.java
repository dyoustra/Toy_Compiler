package Parser;

public class IfStatement extends Node {

    public Node condition; // boolean expression
    public Node body; // statement
    public Node elseBody; // optional statement

    public IfStatement(Node condition, Node body, Node elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    public IfStatement(Node condition, Node body) {
        this.condition = condition;
        this.body = body;
    }

    public String toString() {
        if (elseBody != null) {
            return "if (" + condition + ") " + body + " else " + elseBody;
        } else {
            return "if (" + condition + ") " + body;
        }
    }

}
