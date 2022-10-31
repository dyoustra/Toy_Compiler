// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

public class Identifier extends Token {

    private final String value;

    public Identifier(String value, int row, int column) {
        super(TokenType.IDENTIFIER.name(), row, column);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String valueString() {
        return ": " + value;
    }
}
