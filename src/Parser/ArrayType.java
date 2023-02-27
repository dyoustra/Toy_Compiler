package Parser;

import Scanner.NumericLiteral;

public class ArrayType extends Node {

    public Type baseType;
    public Node size; // ConstantExpression

    public ArrayType(Type baseType, Node size) {
        this.baseType = baseType;
        this.size = size;
    }

    public int evaluateSize() {
        // Paige: for now, we are going to assume that the size is a integer literal. later, if time, we can actually evaluate the constant expression
        try {
            return ((NumericLiteral) ((Literal) this.size).token).getValue();
        } catch (Exception e) {
            System.out.println("Error: Array size must be an integer literal");
            System.exit(1);
        }
        return 0;
    }
}
