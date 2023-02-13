public class ArrayType extends Node {

    public Type baseType;
    public Node index; // ConstantExpression

    public ArrayType(Type baseType, Node index) {
        this.baseType = baseType;
        this.index = index;
    }
}
