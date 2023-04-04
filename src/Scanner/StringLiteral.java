// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;

public class StringLiteral extends Token {

    private final String value;

    public StringLiteral(String value, int row, int column) {
        super(TokenType.STRING, row, column);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
