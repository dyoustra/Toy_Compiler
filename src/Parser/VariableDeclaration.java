package Parser;

public class VariableDeclaration extends Node {

    public Node varType;
    public Identifier name;
    public Node value; // expression

    public VariableDeclaration(Node varType, Identifier name, Node value) {
        super(Kind.VARIABLE_DECLARATION);
        this.varType = varType;
        this.name = name;
        this.value = value;
    }

    public VariableDeclaration(Node varType, Identifier name) {
        super(Kind.VARIABLE_DECLARATION);
        this.varType = varType;
        this.name = name;
    }
}
