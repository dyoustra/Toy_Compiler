package Scanner;

public class EOFToken extends Token {

    public EOFToken(int row, int col) {
        super(TokenType.EOF, row, col);
    }

    @Override
    public String getValue() {
        return "EOF";
    }
}
