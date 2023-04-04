// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;

public class CharacterLiteral extends Token {

    private final char value;

    public CharacterLiteral(char value, int row, int column) {
        super(TokenType.STRING, row, column);
        this.value = value;
    }

    public char getCharValue() {
        return value;
    }

    @Override
    public String getValue() {
        return Character.toString(value);
    }
}
