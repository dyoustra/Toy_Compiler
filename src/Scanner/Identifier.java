// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;

public class Identifier extends Token {

    private final String value;

    public Identifier(String value, int row, int column) {
        super(TokenType.IDENTIFIER, row, column);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
