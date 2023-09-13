// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;
public abstract class Token {

    public enum TokenType {
        CHAR, INT, STRING, IDENTIFIER, SYMBOL, KEYWORD, ERROR, EOF
    }
    private final TokenType type;

    private final int row;
    private final int col;

    public Token(TokenType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public TokenType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public abstract String getValue();

    @Override
    public String toString() {
        return this.getType() + ": " + this.getValue() + " @ line: " + (this.getRow() + 1) + ", col: " + (this.getCol() + 1);
    }
}
