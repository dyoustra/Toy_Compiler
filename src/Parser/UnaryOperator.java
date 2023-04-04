package Parser;

import Scanner.Token;

public class UnaryOperator extends Node {

    public Token token;
    public Node child;
    public boolean isPrefix;

    public UnaryOperator(Token token, Node child) {
        super(Kind.UNARY_OPERATOR);
        this.token = token;
        this.child = child;
    }

    public UnaryOperator(Token token, Node child, boolean isPrefix) {
        super(Kind.UNARY_OPERATOR);
        this.token = token;
        this.child = child;
        this.isPrefix = isPrefix;
    }

}
