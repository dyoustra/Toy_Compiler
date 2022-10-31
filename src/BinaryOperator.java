public class BinaryOperator extends Node {

    public Node left;
    public Node right;

    public BinaryOperator(Token token, Node left, Node right) {
        super(token);
        this.left = left;
        this.right = right;
    }

}
