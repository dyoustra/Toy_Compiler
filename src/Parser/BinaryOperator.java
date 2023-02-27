package Parser;

import Scanner.Token;

public class BinaryOperator extends Node {

    public Token token;
    public Node left;
    public Node right;

    public BinaryOperator(Token token, Node left, Node right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

}
