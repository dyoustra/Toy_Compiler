package Scanner;// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

public class NumericLiteral extends Token {

    private final int value;

    public NumericLiteral(int value, int row, int column) {
        super(TokenType.NUMBER.name(), row, column);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String valueString() {
        return ": " + value;
    }
}
