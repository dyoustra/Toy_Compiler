// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;

public class IdentifierToken extends Token {

    private final String value;

    public IdentifierToken(String value, int row, int column) {
        super(TokenType.IDENTIFIER, row, column);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
