package Scanner;

public class KeywordToken extends Token {

    public enum Keyword {
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

    public final Keyword keyword;

    public KeywordToken(Keyword keyword, int row, int col) {
        super(TokenType.KEYWORD, row, col);
        this.keyword = keyword;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    @Override
    public String getValue() {
        return keyword.name();
    }
}
