package Parser;

import SymbolTables.MethodSymbol;

public class ReturnStatement extends Node {

    public MethodSymbol enclosingMethod;
    // could store "return" token for error location info

    public Node value; // expression, can be null for empty return statement

        public ReturnStatement(Node value) {
            super(Kind.RETURN_STATEMENT);
            this.value = value;
        }
}
