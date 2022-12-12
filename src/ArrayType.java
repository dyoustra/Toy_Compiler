public class ArrayType extends Node {

    public Node baseType;
    public Node index; // ConstantExpression

    public ArrayType(Node baseType, Node index) {
        this.baseType = baseType;
        this.index = index;
    }
}
