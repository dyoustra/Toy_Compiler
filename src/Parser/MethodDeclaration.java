package Parser;

import java.util.ArrayList;

public class MethodDeclaration extends Node {

    public Node returnType;
    public Literal name;
    public ArrayList<VariableDeclaration> parameterTypes;
    public Node body;
    public MethodDeclaration(Node returnType, Literal name, ArrayList<VariableDeclaration> parameterTypes, Node body) {
        this.returnType = returnType;
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.body = body;
    }

}
