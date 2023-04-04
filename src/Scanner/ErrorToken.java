package Scanner;

public class ErrorToken extends Token {

    public ErrorToken(int row, int col) {
        super(TokenType.ERROR, row, col);
    }


    @Override
    public String getValue() {
        return "ERROR";
    }
}
