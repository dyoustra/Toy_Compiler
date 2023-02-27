package Scanner;// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

public class StringLiteral extends Token {

    private final String value;

    public StringLiteral(String value, int row, int column) {
        super(TokenType.STRING.name(), row, column);
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
