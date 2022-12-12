// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

public class Token {

    public enum TokenType {
        CHARACTER, NUMBER, STRING, IDENTIFIER, OPERATOR
    }

    public enum Keywords {
        PROGRAM,
        IF,
        ELSE,
        WHILE,
        RETURN,
        INT,
        CHAR,
        BOOLEAN,
        VOID
    }

    private final String type;

    private final int row;
    private final int col;

    public Token(String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public String getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String valueString  () {
        return "";
    }

    @Override
    public String toString() {
        return this.getType() + this.valueString() + " @ line: " + (this.getRow() + 1) + ", col: " + (this.getCol() + 1);
    }
}
