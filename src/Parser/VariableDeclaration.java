package Parser;

public class VariableDeclaration extends Node {

    public Node type;
    public Identifier name;
    public Node value; // expression

    public VariableDeclaration(Node type, Identifier name, Node value) {
        super(Kind.VARIABLE_DECLARATION);
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public VariableDeclaration(Node type, Identifier name) {
        super(Kind.VARIABLE_DECLARATION);
        this.type = type;
        this.name = name;
    }
}
