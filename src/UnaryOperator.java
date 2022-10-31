public class UnaryOperator extends Node {

    public Node child;

    public UnaryOperator(Token token, Node child) {
        super(token);
        this.child = child;
    }

}
