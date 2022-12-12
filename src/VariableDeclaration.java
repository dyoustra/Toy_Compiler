public class VariableDeclaration extends Node {

    public Node type;
    public Literal name;
    public Node value; // expression

    public VariableDeclaration(Node type, Literal name, Node value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public VariableDeclaration(Node type, Literal name) {
        this.type = type;
        this.name = name;
    }
}
