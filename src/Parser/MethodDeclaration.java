package Parser;

import java.util.ArrayList;

public class MethodDeclaration extends Node {

    public Node returnType;
    public Identifier name;
    public ArrayList<VariableDeclaration> parameters;
    public Node body;
    public MethodDeclaration(Node returnType, Identifier name, ArrayList<VariableDeclaration> parameters, Node body) {
        super(Kind.METHOD_DECLARATION);
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

}
