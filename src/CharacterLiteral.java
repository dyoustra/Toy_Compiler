// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

public class CharacterLiteral extends Token {

    private final char value;

    public CharacterLiteral(char value, int row, int column) {
        super(TokenType.STRING.name(), row, column);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String valueString() {
        return ": " + value;
    }
}
