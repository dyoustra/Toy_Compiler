package SymbolTables;

public class MethodSymbol extends SymbolTableEntry {
    public ParameterSymbol[] params;

    // can't return array type
    public MethodSymbol(String name, Type type, ParameterSymbol[] params) {
        super(name, type);
        this.params = params;
    }
}
