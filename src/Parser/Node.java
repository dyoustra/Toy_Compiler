package Parser;

public abstract class Node {
    // no token variable, as things like arguments don't have tokens
    public enum Kind {
        ARGUMENTS,
        ARRAY_TYPE,
        BINARY_OPERATOR,
        BLOCK,
        CALL_STATEMENT,
        DOT,
        IF_STATEMENT,
        LITERAL,
        METHOD_DECLARATION,
        VARIABLE_DECLARATION,
        RETURN_STATEMENT,
        TYPE,
        UNARY_OPERATOR,
        VARIABLE,
        WHILE_STATEMENT;
    }

    public Kind kind;
    public Node(Kind kind) {
        this.kind = kind;
    }

}