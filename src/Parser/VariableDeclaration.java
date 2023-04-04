package Parser;

public class VariableDeclaration extends Node {

    public Node type;
    public Literal name;
    public Node value; // expression

    public VariableDeclaration(Node type, Literal name, Node value) {
        super(Kind.VARIABLE_DECLARATION);
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public VariableDeclaration(Node type, Literal name) {
        super(Kind.VARIABLE_DECLARATION);
        this.type = type;
        this.name = name;
    }
}
