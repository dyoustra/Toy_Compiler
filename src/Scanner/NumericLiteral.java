// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;

public class NumericLiteral extends Token {

    private final int value;

    public NumericLiteral(int value, int row, int column) {
        super(TokenType.NUMBER, row, column);
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }

    @Override
    public String getValue() {
        return ": " + value;
    }
}
