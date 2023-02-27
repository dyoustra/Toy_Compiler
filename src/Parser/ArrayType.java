package Parser;

public class ArrayType extends Node {

    public Type baseType;
    public Node size; // ConstantExpression

    public ArrayType(Type baseType, Node size) {
        this.baseType = baseType;
        this.size = size;
    }

    public int evaluateSize() {
        return 10; // TODO need to evaluate the size as an int
    }
}
