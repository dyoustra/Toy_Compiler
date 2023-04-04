package Parser;

import Scanner.Token;
import SymbolTables.SymbolTableEntry;

public class Literal extends Leaf {

    public SymbolTableEntry symbolTableEntry;
    // stores what type of literal in the token variable

    public Literal(Token token) {
        super(token, Kind.LITERAL);
    }
}
