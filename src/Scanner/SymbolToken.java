package Scanner;

public class SymbolToken extends Token {

    // String will be the value of a ToyScanner symbol
    public final String symbol;

    public SymbolToken(String symbol, int row, int col) {
        super(TokenType.SYMBOL, row, col);
        this.symbol = symbol;
    }

    @Override
    public String getValue() {
        return this.symbol;
    }
}
