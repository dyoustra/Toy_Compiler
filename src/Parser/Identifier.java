package Parser;

import Scanner.IdentifierToken;
import SymbolTables.SymbolTableEntry;

public class Identifier extends Leaf {
    public SymbolTableEntry symbolTableEntry;

    public Identifier(IdentifierToken token) {
        super(token, Kind.IDENTIFIER);
    }
}
